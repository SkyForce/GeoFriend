package ru.allgage.geofriend.server;

import java.sql.*;

/**
 * Coordinate data access object.
 */
public class CoordinateDAO {
	private Connection connection;

	/**
	 * Create data access object instance.
	 *
	 * @param connection server connection.
	 */
	public CoordinateDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Update the user coordinates in the database.
	 *
	 * @param user      user.
	 * @param latitude  latitude.
	 * @param longitude longitude.
	 * @throws String thrown on query fail.
	 */
	public boolean update(User user, double latitude, double longitude) throws SQLException {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        int status_id = 0;
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT coordinates.status_id " +
                "FROM coordinates " +
                        "WHERE user_id = ? " +
                        "ORDER BY coordinates.time DESC " +
                        "LIMIT 1")) {
            statement.setInt(1, user.getId());
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                status_id = rs.getInt("status_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if(status_id != 0) {
            try (PreparedStatement coordinateStatement = connection.prepareStatement(
                    "INSERT INTO coordinates (user_id, status_id, time, lat, lng) VALUES ( " +
                            "?, " +
                            "?, " +
                            "?, " +
                            "?, " +
                            "?)")) {
                Integer userId = user.getId();
                coordinateStatement.setInt(1, userId);
                coordinateStatement.setInt(2, status_id);
                coordinateStatement.setTimestamp(3, timestamp);
                coordinateStatement.setDouble(4, latitude);
                coordinateStatement.setDouble(5, longitude);

                return coordinateStatement.executeUpdate() > 0;
            }
        }
        return false;
	}
}
