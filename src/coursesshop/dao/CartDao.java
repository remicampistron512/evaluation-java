package coursesshop.dao;

import coursesshop.model.Cart;
import coursesshop.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class CartDao {

  public Cart createCart(int userId) throws SQLException {
    String sql = "INSERT into cart(user_id,created_at,updated_at) VALUES ?,?,?";
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = LocalDateTime.now();

    int cartId;
    // Acquire a DB connection; try-with-resources ensures it is closed automatically
    try (Connection cn = ConnectionFactory.getConnection()) {
      // Start an explicit transaction so both inserts succeed/fail together
      cn.setAutoCommit(false);



      // Insert into users and request the generated primary key
      try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        // Bind parameters to prevent SQL injection and handle escaping properly
        ps.setInt(1, userId);
        ps.setObject(2, createdAt);
        ps.setObject(3, updatedAt);

        // Execute the insert
        ps.executeUpdate();

        // Read the auto-generated key (id) returned by the DB
        try (ResultSet keys = ps.getGeneratedKeys()) {
          // If no key is returned, the insert is inconsistent with expectations
          if (!keys.next()) throw new DaoException("Cart created but no generated key returned.");
          cartId = keys.getInt(1);
        }
      }



      // Commit the transaction (both inserts are persisted)
      cn.commit();
      // Restore default auto-commit mode (optional but keeps connection state clean)
      cn.setAutoCommit(true);

      // Return a domain object reflecting the new persisted cart
      return new Cart(cartId,userId, createdAt, updatedAt);

    } catch (SQLException e) {
      // Wrap low-level SQL exceptions into a DAO-layer exception
      throw new DaoException("Failed to create User.", e);
    }
  }

  public Cart addToCart() {
    return new Cart();
  }
}
