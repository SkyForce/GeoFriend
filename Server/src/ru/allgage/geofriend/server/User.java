package ru.allgage.geofriend.server;

/**
 * User class.
 */
public class User {
	private final int id;
	private final String login;
	private final String email;
	private final boolean isOnline;

	User(int id, String login, String email, boolean isOnline) {
		this.id = id;
		this.login = login;
		this.email = email;
		this.isOnline = isOnline;
	}

	int getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}

	public String getEmail() {
		return email;
	}

	public boolean isOnline() {
		return isOnline;
	}
}
