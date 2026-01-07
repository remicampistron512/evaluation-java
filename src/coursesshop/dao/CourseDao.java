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

    private Course mapCourse(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        int durationDays = rs.getInt("duration_days");
        BigDecimal price = rs.getBigDecimal("price");

        String code = rs.getString("attendance_mode_code");
        AttendanceMode mode = AttendanceMode.valueOf(code);

        return new Course(id, name, description, durationDays, price, mode);
    }

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
                courses.add(mapCourse(rs));
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

                    courses.add(mapCourse(rs));
                }
            }
            // Return the full list
            return courses;

        } catch (SQLException e) {
            // Wrap SQL error into a DAO exception
            throw new DaoException("Failed to list courses.", e);
        }


    }

    public List<Course> findByKeyword(String keyword) {
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
                    WHERE c.name LIKE ?
                    OR c.description LIKE ?
                    ORDER BY c.name;
                    """;

        List<Course> courses = new ArrayList<>();
        // Open connection, prepare statement, execute query; all resources auto-close
        try (Connection cn = ConnectionFactory.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            // Iterate through result rows and map each row to a course object
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    courses.add(mapCourse(rs));
                }
            }
            // Return the full list
            return courses;

        } catch (SQLException e) {
            // Wrap SQL error into a DAO exception
            throw new DaoException("Failed to list courses.", e);
        }


    }

    public List<Course> findByAttendance(AttendanceMode mode) {
        String sql = """
        SELECT id, name, description, duration_days, price, created_at, attendance_mode_code
        FROM course
        WHERE attendance_mode_code = ?
        ORDER BY name
        """;

        List<Course> courses = new ArrayList<>();

        try (Connection cn = ConnectionFactory.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, mode.name()); // "ONSITE" / "REMOTE"

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapCourse(rs));
                }
            }
            return courses;

        } catch (SQLException e) {
            throw new DaoException("Failed to list courses by attendance mode.", e);
        }
    }
}
