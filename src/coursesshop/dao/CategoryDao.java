package coursesshop.dao;

import coursesshop.model.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for {@link Category} entities.
 *
 * <p>This DAO provides read operations for the {@code category} reference table.
 * </p>
 */
public class CategoryDao {

  /**
   * Retrieves all categories from the database, ordered by category id.
   *
   * @return a list of {@link Category} objects (possibly empty, never {@code null})
   * @throws DaoException if a database access error occurs
   */
  public List<Category> findAll() {
    // Query all categories; keep output stable by ordering by primary key.
    String sql = """
            SELECT c.id, c.name
            FROM category c
            ORDER BY c.id
            """;

    // Container for the resulting categories.
    List<Category> categories = new ArrayList<>();

    // try-with-resources ensures JDBC resources are always closed properly.
    try (Connection cn = ConnectionFactory.getConnection();
        PreparedStatement ps = cn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

      // Iterate through rows and map each row to a Category domain object.
      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");

        // Build the domain object from the current row.
        categories.add(new Category(id, name));
      }

      // Return the full list (may be empty if the table is empty).
      return categories;

    } catch (SQLException e) {
      // Wrap JDBC exceptions into a DAO-layer exception for consistent error handling.
      throw new DaoException("Failed to list categories.", e);
    }
  }
}
