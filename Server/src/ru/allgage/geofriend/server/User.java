package ru.allgage.geofriend.server;

/**
 * User class.
 */
public class User {
	private int id;
	private String login;
	private String email;

	User(int id, String login, String email) {
		this.id = id;
		this.login = login;
		this.email = email;
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
}
