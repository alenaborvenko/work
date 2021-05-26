package model;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface InterfaceDAO extends AutoCloseable {
    User findByPassport(String passport);
    boolean addNewCard(String nums) throws SQLException;
    List<Card> findAllCard() throws SQLException;
    boolean topUpBalance(String numAccount, BigDecimal toUp) throws SQLException;
    BigDecimal checkBalance(String numAccount) throws SQLException;
}
