package coursesshop.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConnectionFactory {

    private static final String PROPS_FILE = "db.properties";
    private static final Properties PROPS = loadProps();

    private ConnectionFactory() {}

    private static Properties loadProps() {
        Properties p = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPS_FILE)) {
            p.load(fis);
            return p;
        } catch (IOException e) {
            throw new DaoException("Cannot load " + PROPS_FILE + " (place it next to src/).", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    PROPS.getProperty("db.url"),
                    PROPS.getProperty("db.user"),
                    PROPS.getProperty("db.password")
            );
        } catch (SQLException e) {
            throw new DaoException("Cannot connect to MySQL. Check db.properties.", e);
        }
    }
}
