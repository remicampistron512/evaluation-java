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
                // Adjust this line if your Course constructor is different
                courses.add(new Course(id, name, description, durationDays, price, mode));
            }

            // Return the full list
            return courses;

        } catch (SQLException e) {
            // Wrap SQL error into a DAO exception
            throw new DaoException("Failed to list courses.", e);
        }
    }
}
