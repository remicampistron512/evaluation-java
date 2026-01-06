package coursesshop.dao;

import coursesshop.model.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    public List<Category> findAll() {
        String sql = """
        SELECT c.id, c.name
        FROM category c ORDER BY c.id
        """;

        List<Category> categories = new ArrayList<>();
        // Open connection, prepare statement, execute query; all resources auto-close
        try (Connection cn = ConnectionFactory.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Iterate through result rows and map each row to a category object
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                // Build the domain object from the current row
                categories.add(new Category(id, name));
            }

            // Return the full list
            return categories;

        } catch (SQLException e) {
            // Wrap SQL error into a DAO exception
            throw new DaoException("Failed to list categories.", e);
        }
    }
}
