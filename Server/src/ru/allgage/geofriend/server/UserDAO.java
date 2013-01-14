package ru.allgage.geofriend.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                "UPDATE users SET isonline = 0 WHERE id = (?)")) {
            statement.setInt(1,id);
            int n = 0;
            try (PreparedStatement st = connection.prepareStatement(
                    "SELECT id FROM statuses WHERE user_id = (?) AND time = (SELECT MAX(time) FROM statuses WHERE user_id = (?))"))  {
                st.setInt(1, id);
                st.setInt(2, id);
                ResultSet rs = st.executeQuery();
                if(rs.next())
                    n = rs.getInt(1);
            }
            try (PreparedStatement st = connection.prepareStatement(
                    "UPDATE statuses SET time = (?) WHERE id = (?)"))  {
                st.setTimestamp(1,new Timestamp(System.currentTimeMillis()));
                if(n > 0)
                    st.setInt(2, n);
                st.executeUpdate();
            }
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateRegID(String id, int uid) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE users SET reg_id = (?) WHERE id = (?)")) {
            statement.setString(1,id);
            statement.setInt(2,uid);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public List<String> getOnlineIDs() {
        List<String> list = new ArrayList<String>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT reg_id FROM users WHERE isonline = 1 AND not isnull(reg_id)")) {
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                list.add(rs.getString("reg_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return list;
    }
}
