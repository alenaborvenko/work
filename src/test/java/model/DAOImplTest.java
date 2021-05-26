package model;

import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

import static org.junit.Assert.*;

public class DAOImplTest {
    public Connection init() {
        try (InputStream in = DAOImpl.class.getClassLoader()
                .getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            return DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("user"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void whenAddNewCard() throws Exception{
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            User user = dao.findByPassport("5010 800820");
            assertTrue(dao.addNewCard("10000000000000000000"));
            assertTrue(dao.findAllCard().size() == 2);
            List<Card> expected = new ArrayList<>(Arrays.asList(
                    new Card(1, new Account(1, "10000000000000000000", user, BigDecimal.valueOf(100).setScale(2))),
                    new Card(3, new Account(1, "10000000000000000000", user, BigDecimal.valueOf(100).setScale(2)))
            ));
            assertEquals(expected, dao.findAllCard());
        }
    }

    @Test(expected = Exception.class)
    public void whenAddNewCardException() throws Exception{
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            dao.addNewCard("");
        }
    }

    @Test
    public void whenfindByPassport() throws Exception{
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            User user = dao.findByPassport("5010 800820");
            User expected = new User(1, "Petya", "PetyaIO", "5010 800820");
            assertEquals(expected, user);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenNotFoundByPassport() throws Exception{
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            User user = dao.findByPassport("5010 800");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenNotAddNewCard() throws Exception {
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            User user = dao.findByPassport("5010 800820");
            assertFalse(dao.addNewCard("10000000000000000020"));
            assertTrue(dao.findAllCard().size() == 1);
        }
    }

    @Test(expected = Exception.class)
    public void whenFindAllWithoutUser() throws Exception {
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            dao.findAllCard();
        }
    }

    @Test
    public void whenFindAllOneCard() throws Exception {
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            User user = dao.findByPassport("5010 800820");
            Account account = new Account(1, "10000000000000000000", user, BigDecimal.valueOf(100).setScale(2));
            assertEquals(new ArrayList<>(Arrays.asList(
                    new Card(1, account)
            )),dao.findAllCard());
        }
    }

    @Test
    public void whenFindAllFewCard() throws Exception {
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            User user = dao.findByPassport("5010 800820");
            dao.addNewCard("10000000000000000000");
            assertTrue(dao.findAllCard().size() == 2);
        }
    }

    @Test
    public void whenTopUpBalanceAccount() throws Exception {
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            User user = dao.findByPassport("5010 800820");
            assertTrue(dao.topUpBalance("10000000000000000000", BigDecimal.valueOf(8000)));
            assertEquals(dao.checkBalance("10000000000000000000"), BigDecimal.valueOf(8100).setScale(2));
        }
    }

    @Test
    public void whenTopUpBalanceAccountNotAccount() throws Exception {
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            User user = dao.findByPassport("5010 800820");
            assertFalse(dao.topUpBalance("10000000000000003000", BigDecimal.valueOf(8000).setScale(2)));
        }
    }

    @Test
    public void whenCheckBalance() throws Exception {
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            User user = dao.findByPassport("5010 800820");
            assertEquals(dao.checkBalance("10000000000000000000"), BigDecimal.valueOf(100).setScale(2));
        }
    }

    @Test
    public void whenCheckBalanceNotAccount() throws Exception {
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            User user = dao.findByPassport("5010 800820");
            assertEquals(dao.checkBalance("10000000000000000300"), BigDecimal.ZERO);
        }
    }
}
