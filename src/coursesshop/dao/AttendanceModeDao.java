package coursesshop.dao;

import coursesshop.model.enums.AttendanceMode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceModeDao {

    public List<AttendanceMode> listAttendanceModeCodes() {
        String sql = "SELECT code FROM attendance_mode ORDER BY code";
        List<AttendanceMode> modes = new ArrayList<>();

        try (Connection cn = ConnectionFactory.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String code = rs.getString("code");
                modes.add(AttendanceMode.valueOf(code)); // ONSITE / REMOTE
            }
            return modes;

        } catch (SQLException e) {
            throw new DaoException("Failed to list attendance modes.", e);
        }
    }
}
