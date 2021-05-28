package model;

import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class DAOImplTest {
    public static Connection init() {
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
            assertTrue(dao.addNewCard("10000000000000000000", user.getPassport()));
            assertTrue(dao.findAllCard(user.getPassport()).size() == 2);
            List<Card> expected = new ArrayList<>(Arrays.asList(
                    new Card(1000000000000000L, new Account(1, "10000000000000000000", user, BigDecimal.valueOf(100).setScale(2))),
                    new Card(1000000000000002L, new Account(1, "10000000000000000000", user, BigDecimal.valueOf(100).setScale(2)))
            ));
            assertEquals(expected, dao.findAllCard(user.getPassport()));
        }
    }

    @Test(expected = Exception.class)
    public void whenAddNewCardException() throws Exception{
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            dao.addNewCard("", "");
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
            assertFalse(dao.addNewCard("10000000000000000020", user.getPassport()));
            assertTrue(dao.findAllCard(user.getPassport()).size() == 1);
        }
    }

    @Test
    public void whenFindAllWithoutUser() throws Exception {
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            assertEquals(new ArrayList<>(), dao.findAllCard(""));
        }
    }

    @Test
    public void whenFindAllOneCard() throws Exception {
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            User user = dao.findByPassport("5010 800820");
            Account account = new Account(1, "10000000000000000000", user, BigDecimal.valueOf(100).setScale(2));
            assertEquals(new ArrayList<>(Arrays.asList(
                    new Card(1000000000000000L, account)
            )),dao.findAllCard(user.getPassport()));
        }
    }

    @Test
    public void whenFindAllFewCard() throws Exception {
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            User user = dao.findByPassport("5010 800820");
            dao.addNewCard("10000000000000000000", user.getPassport());
            assertTrue(dao.findAllCard(user.getPassport()).size() == 2);
        }
    }

    @Test
    public void whenTopUpBalanceAccount() throws Exception {
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            User user = dao.findByPassport("5010 800820");
            assertTrue(dao.topUpBalance("10000000000000000000", BigDecimal.valueOf(8000), user.getPassport()));
            assertEquals(dao.checkBalance("10000000000000000000", user.getPassport()), BigDecimal.valueOf(8100).setScale(2));
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void whenTopUpBalanceAccountNotAccount() throws Exception {
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            User user = dao.findByPassport("5010 800820");
            dao.topUpBalance("10000000000000003000",BigDecimal.valueOf(8000).setScale(2), user.getPassport());
//            assertFalse(dao.topUpBalance("10000000000000003000", BigDecimal.valueOf(8000).setScale(2), user.getPassport()));
        }
    }

    @Test
    public void whenCheckBalance() throws Exception {
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            User user = dao.findByPassport("5010 800820");
            assertEquals(dao.checkBalance("10000000000000000000", user.getPassport()), BigDecimal.valueOf(100).setScale(2));
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void whenCheckBalanceNotAccount() throws Exception {
        try (DAOImpl dao = new DAOImpl(new DBConnect(ConnectionRollBack.create(this.init())))) {
            User user = dao.findByPassport("5010 800820");
            dao.checkBalance("10000000000000000300", user.getPassport());
            //assertEquals(dao.checkBalance("10000000000000000300", user.getPassport()), BigDecimal.ZERO);
        }
    }
}
