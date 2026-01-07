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
import java.util.function.Consumer;

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

    private List<Course> queryCourses(String sql, Consumer<PreparedStatement> binder) {
        List<Course> courses = new ArrayList<>();

        try (Connection cn = ConnectionFactory.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            if (binder != null) {
                binder.accept(ps); // set parameters
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapCourse(rs));
                }
            }
            return courses;

        } catch (SQLException e) {
            throw new DaoException("Failed to query courses.", e);
        }
    }

    public List<Course> findAll() {
        String sql = """
        SELECT c.id, c.name, c.description,c.duration_days,c.price,c.created_at,c.attendance_mode_code
        FROM course c ORDER BY c.name
        """;

        return queryCourses(sql, null);
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

        return queryCourses(sql, ps -> {
            try { ps.setInt(1, categoryId); }
            catch (SQLException e) { throw new DaoException("Failed to bind parameters.", e); }
        });

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

        String pattern = "%" + keyword + "%";
        return queryCourses(sql, ps -> {
            try {
                ps.setString(1, pattern);
                ps.setString(2, pattern);
            } catch (SQLException e) {
                throw new DaoException("Failed to bind parameters.", e);
            }
        });


    }

    public List<Course> findByAttendance(AttendanceMode mode) {
        String sql = """
        SELECT id, name, description, duration_days, price, created_at, attendance_mode_code
        FROM course
        WHERE attendance_mode_code = ?
        ORDER BY name
        """;

        return queryCourses(sql, ps -> {
            try { ps.setString(1, mode.name()); }
            catch (SQLException e) { throw new DaoException("Failed to bind parameters.", e); }
        });
    }
}
