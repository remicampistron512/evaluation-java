package coursesshop.dao;

import coursesshop.model.Cart;
import coursesshop.model.CartItem;
import coursesshop.model.User;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CartDao {

  public Cart createCart(int userId) throws SQLException {
    String sql = "INSERT into cart(crt_created_at,crt_updated_at,use_id) VALUES (?,?,?)";
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

        ps.setObject(1, createdAt);
        ps.setObject(2, updatedAt);
        ps.setInt(3, userId);

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
      throw new DaoException("Failed to create Cart.", e);
    }
  }


    public Cart addToCart(int cartId, int courseId, int quantity) throws SQLException {
      if (quantity <= 0) {
        throw new DaoException("Quantity must be > 0.");
      }

      String selectExistingItemSql =
          "SELECT ci.cit_id, ci.cit_quantity " +
              "FROM cart_item ci " +
              "JOIN cart_cart_item cci ON cci.cit_id = ci.cit_id " +
              "WHERE cci.crt_id = ? AND ci.cou_id = ? " +
              "LIMIT 1";

      String selectCoursePriceSql =
          "SELECT cou_price FROM course WHERE cou_id = ?";

      String insertCartItemSql =
          "INSERT INTO cart_item(cit_quantity, cit_unit_price, cou_id) VALUES (?,?,?)";

      String insertCartCartItemSql =
          "INSERT INTO cart_cart_item(cit_id, crt_id) VALUES (?,?)";

      String updateExistingItemSql =
          "UPDATE cart_item SET cit_quantity = cit_quantity + ? WHERE cit_id = ?";

      String updateCartUpdatedAtSql =
          "UPDATE cart SET crt_updated_at = ? WHERE crt_id = ?";

      String selectCartSql =
          "SELECT crt_id, use_id, crt_created_at, crt_updated_at FROM cart WHERE crt_id = ?";

      LocalDateTime now = LocalDateTime.now();

      try (Connection cn = ConnectionFactory.getConnection()) {
        cn.setAutoCommit(false);

        try {
          // 1) Check cart exists (optional but strongly recommended)
          int userId;
          LocalDateTime createdAt;
          LocalDateTime updatedAt;

          try (PreparedStatement ps = cn.prepareStatement(selectCartSql)) {
            ps.setInt(1, cartId);
            try (ResultSet rs = ps.executeQuery()) {
              if (!rs.next()) {
                throw new DaoException("Cart not found: crt_id=" + cartId);
              }
              userId = rs.getInt("use_id");
              Timestamp tsCreated = rs.getTimestamp("crt_created_at");
              Timestamp tsUpdated = rs.getTimestamp("crt_updated_at");
              createdAt = tsCreated != null ? tsCreated.toLocalDateTime() : now;
              updatedAt = tsUpdated != null ? tsUpdated.toLocalDateTime() : now;
            }
          }

          // 2) Try to find existing cart_item for this course in this cart
          Integer existingCartItemId = null;

          try (PreparedStatement ps = cn.prepareStatement(selectExistingItemSql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
              if (rs.next()) {
                existingCartItemId = rs.getInt("cit_id");
              }
            }
          }

          if (existingCartItemId != null) {
            // 3a) Update quantity
            try (PreparedStatement ps = cn.prepareStatement(updateExistingItemSql)) {
              ps.setInt(1, quantity);
              ps.setInt(2, existingCartItemId);
              ps.executeUpdate();
            }
          } else {
            // 3b) Read course price
            BigDecimal unitPrice;
            try (PreparedStatement ps = cn.prepareStatement(selectCoursePriceSql)) {
              ps.setInt(1, courseId);
              try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                  throw new DaoException("Course not found: cou_id=" + courseId);
                }
                unitPrice = rs.getBigDecimal("cou_price");
              }
            }

            // Insert cart_item
            int cartItemId;
            try (PreparedStatement ps = cn.prepareStatement(insertCartItemSql, Statement.RETURN_GENERATED_KEYS)) {
              ps.setInt(1, quantity);
              ps.setBigDecimal(2, unitPrice);
              ps.setInt(3, courseId);
              ps.executeUpdate();

              try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) {
                  throw new DaoException("Cart item inserted but no generated key returned.");
                }
                cartItemId = keys.getInt(1);
              }
            }

            // Link to cart via join table
            try (PreparedStatement ps = cn.prepareStatement(insertCartCartItemSql)) {
              ps.setInt(1, cartItemId);
              ps.setInt(2, cartId);
              ps.executeUpdate();
            }
          }

          // 4) Update cart updated_at
          try (PreparedStatement ps = cn.prepareStatement(updateCartUpdatedAtSql)) {
            ps.setTimestamp(1, Timestamp.valueOf(now));
            ps.setInt(2, cartId);
            ps.executeUpdate();
          }

          cn.commit();
          cn.setAutoCommit(true);

          // Return updated cart metadata (items are not loaded here)
          return new Cart(cartId, userId, createdAt, now);

        } catch (SQLException | RuntimeException ex) {
          cn.rollback();
          throw ex;
        }

      } catch (SQLException e) {
        throw new DaoException("Failed to add item to cart.", e);
      }
    }

  public Cart getCartByUserId(int userId) {
    String cartSql = """
        SELECT crt_id, use_id, crt_created_at, crt_updated_at
        FROM cart
        WHERE use_id = ?
        """;

    String itemsSql = """
        SELECT ci.cit_id, ci.cou_id, ci.cit_quantity, ci.cit_unit_price
        FROM cart_item ci
        JOIN cart_cart_item cci ON cci.cit_id = ci.cit_id
        WHERE cci.crt_id = ?
        ORDER BY ci.cit_id
        """;

    try (Connection cn = ConnectionFactory.getConnection();
        PreparedStatement cartPs = cn.prepareStatement(cartSql)) {

      cartPs.setInt(1, userId);

      try (ResultSet rs = cartPs.executeQuery()) {
        if (!rs.next()) {
          return null; // no cart for this user
        }

        int cartId = rs.getInt("crt_id");

        Timestamp tsCreated = rs.getTimestamp("crt_created_at");
        Timestamp tsUpdated = rs.getTimestamp("crt_updated_at");

        LocalDateTime createdAt = tsCreated != null ? tsCreated.toLocalDateTime() : null;
        LocalDateTime updatedAt = tsUpdated != null ? tsUpdated.toLocalDateTime() : null;

        Cart cart = new Cart(cartId, userId, createdAt, updatedAt);

        // Load items
        try (PreparedStatement itemsPs = cn.prepareStatement(itemsSql)) {
          itemsPs.setInt(1, cartId);

          try (ResultSet itemsRs = itemsPs.executeQuery()) {
            while (itemsRs.next()) {
              CartItem item = new CartItem(
                  itemsRs.getInt("cit_id"),
                  itemsRs.getInt("cou_id"),
                  itemsRs.getInt("cit_quantity"),
                  itemsRs.getBigDecimal("cit_unit_price")
              );
              cart.addItem(item);
            }
          }
        }

        return cart;
      }

    } catch (SQLException e) {
      throw new DaoException("Failed to get cart by user id: " + userId, e);
    }
  }
}

