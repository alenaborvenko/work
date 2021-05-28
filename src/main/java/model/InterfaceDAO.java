package model;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface InterfaceDAO extends AutoCloseable {
    User findByPassport(String passport) throws SQLException;
    boolean addNewCard(String nums, String passport) throws SQLException;
    List<Card> findAllCard(String passport) throws SQLException;
    boolean topUpBalance(String numAccount, BigDecimal toUp, String passport) throws SQLException;
    BigDecimal checkBalance(String numAccount, String passport) throws SQLException;
}
