package coursesshop.dao;

import coursesshop.model.Course;
import coursesshop.model.enums.AttendanceMode;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {
    public List<Course> findAll() {
        String sql = """
        SELECT c.id, c.name, c.description,c.duration_days,c.price,c.created_at,c.attendance_mode_code
        FROM course c ORDER BY c.name
        """;

        List<Course> courses = new ArrayList<>();
        // Open connection, prepare statement, execute query; all resources auto-close
        try (Connection cn = ConnectionFactory.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Iterate through result rows and map each row to a course object
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                int durationDays = rs.getInt("duration_days");
                BigDecimal price = rs.getBigDecimal("price");
                String code = rs.getString("attendance_mode_code");
                AttendanceMode mode = AttendanceMode.valueOf(code);



                // Build the domain object from the current row
               courses.add(new Course(id, name, description, durationDays, price, mode));
            }

            // Return the full list
            return courses;

        } catch (SQLException e) {
            // Wrap SQL error into a DAO exception
            throw new DaoException("Failed to list courses.", e);
        }
    }

    public List<Course> findByCategory(int categoryId) {
        String sql = """
                    SELECT
                    c.id,
                    c.name,
                    c.description,
                    c.duration_days,
                    c.price,
                    c.created_at,
                    c.attendance_mode_code
                    FROM course c
                    JOIN course_category cc ON cc.course_id = c.id
                    JOIN category cat ON cat.id = cc.category_id
                    WHERE cat.id = ?
                    ORDER BY c.name;
                    """;

        List<Course> courses = new ArrayList<>();
        // Open connection, prepare statement, execute query; all resources auto-close
        try (Connection cn = ConnectionFactory.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);

            // Iterate through result rows and map each row to a course object
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    int durationDays = rs.getInt("duration_days");
                    BigDecimal price = rs.getBigDecimal("price");
                    String code = rs.getString("attendance_mode_code");
                    AttendanceMode mode = AttendanceMode.valueOf(code);

                    courses.add(new Course(id, name, description, durationDays, price, mode));
                }
            }
            // Return the full list
            return courses;

        } catch (SQLException e) {
            // Wrap SQL error into a DAO exception
            throw new DaoException("Failed to list courses.", e);
        }


    }
}
