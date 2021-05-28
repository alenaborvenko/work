package model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Классю имплементирующий интерфейс работы с базой данной
 */
public class DAOImpl implements InterfaceDAO {
    private final DBConnect model;
    private final Connection connection;
    private User user;

    public DAOImpl() {
        this.model = new DBConnect();
        model.initConnection();
        this.connection = model.getConnection();
    }

    public DAOImpl(DBConnect model) {
        this.model = model;
        this.connection = model.getConnection();
    }

    /**
     * Метод для поиска пользователя в базе данных
     * @param passport - строка с номером и серией паспорта
     * @return - объект класса User
     * @throws IllegalArgumentException - если пользователь не найден
     * @throws SQLException - ошибки работы с базой
     */
    public User findByPassport(String passport) throws IllegalArgumentException,SQLException {
        User rsl = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER WHERE PASSPORT = ?")) {
            statement.setString(1, passport);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String surname = resultSet.getString("surname");
                    rsl = new User(id, name, surname, passport);
                    resultSet.close();
                } else {
                    throw new IllegalArgumentException("Passport not found!");
                }
            }
        }
            user = rsl;
            return user;
    }

    /**
     * Метод добавляющий новую карту к существующему пользователю счету
     * @param numAccount - номер счета к которому требуется добавить карту
     * @param passport - серия и номер паспорта
     * @return - true если карта добавлена false - если номер счета не найден
     * @throws SQLException - ошибки запроса к базе
     */
    @Override
    public boolean addNewCard(String numAccount, String passport) throws SQLException {
        int rsl = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID FROM ACCOUNT WHERE NUM = ? " +
                "AND USER_ID = (SELECT ID FROM USER WHERE PASSPORT = ?)")){
            preparedStatement.setString(1, numAccount);
            preparedStatement.setString(2, passport);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int idAcc = resultSet.getInt(1);
                    try (PreparedStatement prepared= connection.prepareStatement(
                            "INSERT INTO CARD (ACCOUNT_ID) VALUES (?)")) {
                        prepared.setInt(1, idAcc);
                        rsl = prepared.executeUpdate();
                    }
                } else {
                    throw new IllegalArgumentException("Account not found");
                }
            }
        }
        return rsl > 0;
    }

    /**
     * метод генерации нового счета
     * @return - строка с номером счета
     */
    private String genereted() {
        StringBuilder rsl = new StringBuilder();
        Random random = new Random();
        rsl.append(1 + random.nextInt(9));
        for (int i = 0; i < 18; i++) {
            rsl.append(random.nextInt(10));
        }
        return rsl.toString();
    }

    /**
     * метод получающий список всех счетов пользователя
     * @param passport - серия и  номер паспорта
     * @return - список объектов типа карт
     * @throws SQLException - ошибки запроса к базе
     */
    @Override
    public List<Card> findAllCard(String passport) throws SQLException {
        List<Card> cardList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT CARD.ID, ACCOUNT.ID, ACCOUNT.NUM, ACCOUNT.BALANCE FROM CARD " +
                        "INNER JOIN ACCOUNT on" +
                        "    ACCOUNT.ID = CARD.ACCOUNT_ID " +
                        "WHERE USER_ID = (SELECT ID FROM USER WHERE PASSPORT = ?)")) {
            preparedStatement.setString(1, passport);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long idCard = resultSet.getLong(1);
                int idAcc = resultSet.getInt(2);
                String numAccount = resultSet.getString(3);
                BigDecimal balance = resultSet.getBigDecimal(4);
                Account account = new Account(idAcc, numAccount, user, balance);
                Card card = new Card(idCard, account);
                cardList.add(card);
            }
        }
        return cardList;
    }

    /**
     * Метод зачисления дс на счет клиенту
     * @param numAccount - номер счета
     * @param toUp - сумма зачисления
     * @param passport - сенрия и номер паспорта
     * @return - true - если сумма увеличена
     * @throws IllegalArgumentException - если счет не найден у пользователя
     * @throws SQLException - ошибки запроса к базе
     */
    @Override
    public boolean topUpBalance(String numAccount, BigDecimal toUp, String passport) throws IllegalArgumentException, SQLException {
        int rsl = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE ACCOUNT SET BALANCE = BALANCE + ?" +
                        "WHERE NUM =? AND ACCOUNT.USER_ID = (SELECT ID FROM USER WHERE PASSPORT = ?);"
        )) {
            preparedStatement.setBigDecimal(1, toUp);
            preparedStatement.setString(2, numAccount);
            preparedStatement.setString(3, passport);
            rsl = preparedStatement.executeUpdate();
            if (rsl == 0) {
                throw new IllegalArgumentException("Account not found");
            }

        }
        return rsl > 0;
    }

    /**
     * метод проверящий остаток по счету
     * @param numAccount - номер счета
     * @param passport - серия и номер паспорта
     * @return - остаток
     * @throws IllegalArgumentException - если номер счета не найден
     * @throws SQLException - ошибки запроса к базе
     */
    @Override
    public BigDecimal checkBalance(String numAccount, String passport) throws IllegalArgumentException, SQLException {
        BigDecimal rsl = BigDecimal.ZERO;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT BALANCE FROM ACCOUNT WHERE NUM = ? " +
                "AND USER_ID = (SELECT ID FROM USER WHERE PASSPORT = ?)")) {
            preparedStatement.setString(1, numAccount);
            preparedStatement.setString(2, passport);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    rsl = resultSet.getBigDecimal(1);
                } else {
                    throw new IllegalArgumentException("Account not found");
                }
            }
            //resultSet.close();
        }
        return rsl;
    }


    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
