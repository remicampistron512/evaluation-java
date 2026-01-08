package coursesshop.dao;

import coursesshop.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

/**
 * Data Access Object (DAO) for {@link User} entities.
 *
 * <p>This DAO provides inserts for the {@code user}  table.
 * </p>
 */

public class UserDao {

  /**
   * Insert a new user in the database.
   *
   * @return a {@link User} object
   * @throws DaoException if a database access error occurs
   */
  public User register(String firstName, String lastName, String login, String password) {

      LocalDateTime createdAt = LocalDateTime.now();
      // SQL statement to insert the base "user" record
      String sqlUser = "INSERT INTO user_(first_name, last_name,login,password,created_at) "
          + "VALUES (?, ?, ?, ?, ?)";

      // Acquire a DB connection; try-with-resources ensures it is closed automatically
      try (Connection cn = ConnectionFactory.getConnection()) {
        // Start an explicit transaction so both inserts succeed/fail together
        cn.setAutoCommit(false);

        int userId;

        // Insert into users and request the generated primary key
        try (PreparedStatement ps = cn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
          // Bind parameters to prevent SQL injection and handle escaping properly
          ps.setString(1, firstName);
          ps.setString(2, lastName);
          ps.setString(3, login);
          ps.setString(4, password);
          ps.setObject(5, createdAt);

          // Execute the insert
          ps.executeUpdate();

          // Read the auto-generated key (id) returned by the DB
          try (ResultSet keys = ps.getGeneratedKeys()) {
            // If no key is returned, the insert is inconsistent with expectations
            if (!keys.next()) throw new DaoException("User inserted but no generated key returned.");
            userId = keys.getInt(1);
          }
        }



        // Commit the transaction (both inserts are persisted)
        cn.commit();
        // Restore default auto-commit mode (optional but keeps connection state clean)
        cn.setAutoCommit(true);

        // Return a domain object reflecting the new persisted customer
        return new User(userId, firstName, lastName,login,password);

      } catch (SQLException e) {
        // Wrap low-level SQL exceptions into a DAO-layer exception
        throw new DaoException("Failed to create User.", e);
      }
    }

  }

