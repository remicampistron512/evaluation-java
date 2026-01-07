package coursesshop.dao;

import coursesshop.model.enums.AttendanceMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) responsible for retrieving {@link AttendanceMode} values
 * from the {@code attendance_mode} reference table.
 * <p>
 * In the database, {@code attendance_mode.code} is expected to match the Java enum names
 * (e.g., {@code ONSITE}, {@code REMOTE}). This DAO reads the codes and converts them to
 * {@link AttendanceMode}.
 * </p>
 */
public class AttendanceModeDao {

  /**
   * Returns all available attendance modes from the database as enum values.
   * <p>
   * The result is ordered by the {@code code} column (alphabetical order).
   * </p>
   *
   * @return a list of attendance modes (possibly empty, never {@code null})
   * @throws DaoException if a database access error occurs
   * @throws IllegalArgumentException if the database contains a code that does not match
   *                                  any {@link AttendanceMode} enum constant
   */
  public List<AttendanceMode> listAttendanceModeCodes() {
    // Select only the "code" column; it must correspond to enum names.
    String sql = "SELECT code FROM attendance_mode ORDER BY code";

    // Container for the resulting enum values.
    List<AttendanceMode> modes = new ArrayList<>();

    // try-with-resources automatically closes Connection, PreparedStatement, and ResultSet.
    try (Connection cn = ConnectionFactory.getConnection();
        PreparedStatement ps = cn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

      // Iterate through each row and convert the DB code into an AttendanceMode enum.
      while (rs.next()) {
        String code = rs.getString("code");
        modes.add(AttendanceMode.valueOf(code)); // expected: "ONSITE" / "REMOTE"
      }

      // Return the complete list (maybe empty if the table is empty).
      return modes;

    } catch (SQLException e) {
      // Wrap JDBC exceptions into a DAO-layer exception for consistent error handling.
      throw new DaoException("Failed to list attendance modes.", e);
    }
  }
}
