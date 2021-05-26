package model;

import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnect implements AutoCloseable{

    private Connection connection;

    public DBConnect() {
        connection = getConnection();
    }

    public DBConnect(Connection connection) throws FileNotFoundException {
        this.connection = connection;
        init();
    }

    private void init() throws FileNotFoundException {
        try {
            RunScript.execute(connection, new FileReader("./src/main/resources/create.ddl"));
            RunScript.execute(connection, new FileReader("./src/main/resources/insertFirst.dml"));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void initConnection() {
        try (InputStream in = DBConnect.class.getClassLoader()
                .getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            JdbcConnectionPool pool = JdbcConnectionPool.create(
                    config.getProperty("url"),
                    config.getProperty("user"),
                    config.getProperty("password")
            );
            connection = pool.getConnection();
            init();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null) {
                connection.close();
        }
    }
}
