package coursesshop.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Factory class responsible for creating JDBC {@link Connection} instances.
 *
 * <p>Database configuration is loaded once at class initialization time from a
 * {@code db.properties} file. This class centralizes connection creation so DAOs do not
 * need to manage driver configuration details.
 * </p>
 *
 * <h2>Expected properties</h2>
 * <ul>
 *   <li>{@code db.url} - JDBC URL (e.g., {@code jdbc:mariadb://localhost:3306/courses_shop})</li>
 *   <li>{@code db.user} - database username</li>
 *   <li>{@code db.password} - database password</li>
 * </ul>
 *
 * <p>Any {@link IOException} or {@link SQLException} is wrapped into a {@link DaoException}
 * to keep error handling consistent across the DAO layer.
 * </p>
 */
public final class ConnectionFactory {

  /** Name/location of the properties file used to configure DB access. */
  private static final String PROPS_FILE = "db.properties";

  /**
   * Properties loaded from {@link #PROPS_FILE} at class initialization.
   *
   * <p>Loaded once to avoid re-reading the file for every connection request.
   * </p>
   */
  private static final Properties PROPS = loadProps();

  /**
   * Private constructor to prevent instantiation (utility class).
   */
  private ConnectionFactory() {}

  /**
   * Loads database properties from the {@link #PROPS_FILE} file.
   *
   * @return a populated {@link Properties} instance
   * @throws DaoException if the file cannot be read
   */
  private static Properties loadProps() {
    Properties p = new Properties();

    // try-with-resources ensures the file input stream is closed properly.
    try (FileInputStream fis = new FileInputStream(PROPS_FILE)) {
      p.load(fis);
      return p;

    } catch (IOException e) {
      // Wrap IO issues into a DAO exception (configuration problem).
      throw new DaoException("Cannot load " + PROPS_FILE + " (place it next to src/).", e);
    }
  }

  /**
   * Creates and returns a new JDBC connection using the values loaded from {@link #PROPS_FILE}.
   *
   * <p>The caller is responsible for closing the returned connection (typically handled via
   * try-with-resources in DAOs/services).
   * </p>
   *
   * @return an open JDBC {@link Connection}
   * @throws DaoException if a database connection cannot be established
   */
  public static Connection getConnection() {
    try {
      // Create a new connection using configured URL, username, and password.
      return DriverManager.getConnection(
          PROPS.getProperty("db.url"),
          PROPS.getProperty("db.user"),
          PROPS.getProperty("db.password")
      );

    } catch (SQLException e) {
      // Wrap SQL issues into a DAO exception (connectivity/authentication problem).
      throw new DaoException("Cannot connect to MySQL. Check db.properties.", e);
    }
  }
}
