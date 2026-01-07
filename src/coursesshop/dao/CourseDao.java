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

/**
 * Data Access Object (DAO) for {@link Course} entities.
 *
 * <p>This class encapsulates all JDBC access to the {@code course} table and related tables
 * (e.g., {@code course_category}, {@code category}).
 * </p>
 *
 * <p>All methods return fully populated {@link Course} domain objects and wrap SQL errors
 * into a {@link DaoException}.
 * </p>
 */
public class CourseDao {

  /**
   * Maps the current row of a {@link ResultSet} into a {@link Course} object.
   *
   * <p>This method centralizes the row-to-object mapping logic to avoid duplication across
   * queries.
   * </p>
   *
   * @param rs the result set positioned on a valid row
   * @return a {@link Course} built from the current row
   * @throws SQLException             if a JDBC access error occurs while reading columns
   * @throws IllegalArgumentException if {@code attendance_mode_code} is not a valid
   *                                  {@link AttendanceMode} name
   */
  private Course mapCourse(ResultSet rs) throws SQLException {
    // Read scalar columns
    int id = rs.getInt("id");
    String name = rs.getString("name");
    String description = rs.getString("description");
    int durationDays = rs.getInt("duration_days");
    BigDecimal price = rs.getBigDecimal("price");

    // Convert attendance mode code into enum (expects "ONSITE" or "REMOTE")
    String code = rs.getString("attendance_mode_code");
    AttendanceMode mode = AttendanceMode.valueOf(code);

    // Build and return the domain object
    return new Course(id, name, description, durationDays, price, mode);
  }

  /**
   * Executes a SELECT query that returns courses and maps each row into a {@link Course}.
   *
   * <p>A caller may optionally provide a {@code binder} to bind parameters on the prepared
   * statement.
   * </p>
   *
   * @param sql    the SQL query to execute (should SELECT the columns required by
   *               {@link #mapCourse(ResultSet)})
   * @param binder optional binder used to set prepared statement parameters; may be {@code null}
   * @return a list of {@link Course} objects (possibly empty, never {@code null})
   * @throws DaoException if a JDBC error occurs
   */
  private List<Course> queryCourses(String sql, Consumer<PreparedStatement> binder) {
    List<Course> courses = new ArrayList<>();

    // try-with-resources ensures Connection, PreparedStatement and ResultSet are closed properly
    try (Connection cn = ConnectionFactory.getConnection();
        PreparedStatement ps = cn.prepareStatement(sql)) {

      // Bind query parameters if the caller provided a binder
      if (binder != null) {
        binder.accept(ps);
      }

      // Execute and map all rows
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          courses.add(mapCourse(rs));
        }
      }

      return courses;

    } catch (SQLException e) {
      // Translate JDBC exceptions into a domain-specific DAO exception
      throw new DaoException("Failed to query courses.", e);
    }
  }

  /**
   * Returns all courses, ordered by course name.
   *
   * @return a list of all {@link Course} objects (possibly empty, never {@code null})
   * @throws DaoException if a JDBC error occurs
   */
  public List<Course> findAll() {
    String sql = """
        SELECT c.id,
               c.name,
               c.description,
               c.duration_days,
               c.price,
               c.created_at,
               c.attendance_mode_code
        FROM course c
        ORDER BY c.name
        """;

    return queryCourses(sql, null);
  }

  /**
   * Returns all courses that belong to the given category.
   *
   * @param categoryId the category identifier (PK of {@code category.id})
   * @return a list of {@link Course} objects in the category (possibly empty, never {@code null})
   * @throws DaoException if a JDBC error occurs or if parameters cannot be bound
   */
  public List<Course> findByCategory(int categoryId) {
    String sql = """
        SELECT c.id,
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
        ORDER BY c.name
        """;

    // Bind the category id to the single placeholder
    return queryCourses(sql, ps -> {
      try {
        ps.setInt(1, categoryId);
      } catch (SQLException e) {
        throw new DaoException("Failed to bind parameters.", e);
      }
    });
  }

  /**
   * Returns courses whose name or description contains the provided keyword.
   *
   * <p>Uses {@code LIKE} with wildcards (e.g., {@code %keyword%}).
   * </p>
   *
   * @param keyword the keyword to search for (must not be {@code null})
   * @return a list of matching {@link Course} objects (possibly empty, never {@code null})
   * @throws DaoException if a JDBC error occurs or if parameters cannot be bound
   */
  public List<Course> findByKeyword(String keyword) {
    String sql = """
        SELECT c.id,
               c.name,
               c.description,
               c.duration_days,
               c.price,
               c.created_at,
               c.attendance_mode_code
        FROM course c
        WHERE c.name LIKE ?
           OR c.description LIKE ?
        ORDER BY c.name
        """;

    String pattern = "%" + keyword + "%";

    // Bind the search pattern to both placeholders
    return queryCourses(sql, ps -> {
      try {
        ps.setString(1, pattern);
        ps.setString(2, pattern);
      } catch (SQLException e) {
        throw new DaoException("Failed to bind parameters.", e);
      }
    });
  }

  /**
   * Returns all courses matching the specified attendance mode.
   *
   * @param mode the attendance mode to filter by (e.g., {@link AttendanceMode#ONSITE} or
   *             {@link AttendanceMode#REMOTE})
   * @return a list of {@link Course} objects matching the mode (possibly empty, never {@code null})
   * @throws DaoException if a JDBC error occurs or if parameters cannot be bound
   */
  public List<Course> findByAttendance(AttendanceMode mode) {
    String sql = """
        SELECT id,
               name,
               description,
               duration_days,
               price,
               created_at,
               attendance_mode_code
        FROM course
        WHERE attendance_mode_code = ?
        ORDER BY name
        """;

    // attendance_mode_code is stored as a String equal to the enum name ("ONSITE"/"REMOTE")
    return queryCourses(sql, ps -> {
      try {
        ps.setString(1, mode.name());
      } catch (SQLException e) {
        throw new DaoException("Failed to bind parameters.", e);
      }
    });
  }
}
