package ru.allgage.geofriend.server;

import java.sql.*;

/**
 * User data access object.
 */
public class UserDAO {
	private Connection connection;

	/**
	 * Create data access object instance.
	 *
	 * @param connection server connection.
	 */
	public UserDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Load specified user from the database.
	 *
	 * @param login    user name.
	 * @param password user password.
	 * @return user if user exists; null otherwise.
	 * @throws SQLException thrown on query fail.
	 */
	public User load(String login, String password) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(
				"SELECT id, login, email FROM users WHERE login = ? AND password = MD5(?)")) {
			statement.setString(1, login);
			statement.setString(2, password);

			try (ResultSet resultSet = statement.executeQuery()) {
				resultSet.next();
				Integer id = resultSet.getInt("id");
				String userLogin = resultSet.getString("login");
				String email = resultSet.getString("email");

                try(Statement st = connection.createStatement()) {
                    st.executeUpdate("UPDATE users SET isonline = 1 WHERE id = "+String.valueOf(id));
                }

				return new User(id, userLogin, email, true);
			}
		}
	}

	/**
	 * Creates the user.
	 *
	 * @param login    user name.
	 * @param password user password.
	 * @param email    user e-mail.
	 * @return created user; null if user was not created.
	 */
	public User create(String login, String password, String email) {
		try (PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO users (login, password, email) VALUES(?, MD5(?), ?)")) {
			statement.setString(1, login);
			statement.setString(2, password);
			statement.setString(3, email);

			if (statement.executeUpdate() > 0) {
				return load(login, password); // TODO: optimize
			} else {
				return null;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
			return null;
		}
	}

    public boolean clearOnlines() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE users SET isonline = 0")) {
            return statement.executeUpdate() > 0;
        }
    }

    public boolean setOffline(int id){
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE users SET isonline = 0 WHERE id = "+String.valueOf(id))) {
            try (PreparedStatement st = connection.prepareStatement(
                    "UPDATE statuses SET time = (?) WHERE id = "+String.valueOf(id))) {
                st.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                st.executeUpdate();
            }
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
