package ru.allgage.geofriend.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * Status data access object.
 */
public class StatusDAO {
	private Connection connection;

	/**
	 * Create data access object instance.
	 *
	 * @param connection server connection.
	 */
	public StatusDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Creates the status in the database.
	 *
	 * @param user      user.
	 * @param latitude  latitude.
	 * @param longitude longitude.
	 * @param text      message text.
	 * @throws SQLException thrown on query fail.
	 */
	public boolean create(User user, double latitude, double longitude, String text) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO statuses (user_id, time, status, lat, lng) VALUES (?, ?, ?, ?, ?)")) {
			statement.setInt(1, user.getId());
			statement.setDate(2, SQLHelper.convertDate(new Date()));
			statement.setString(3, text);
			statement.setDouble(4, latitude);
			statement.setDouble(5, longitude);

			return statement.executeUpdate() > 0;
		}
	}
}
