package ru.allgage.geofriend.server;

/**
 * User class.
 */
public class User {
	private Integer id;
	private String login;
	private String email;

	User(Integer id, String login, String email) {
		this.id = id;
		this.login = login;
		this.email = email;
	}

	Integer getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}

	public String getEmail() {
		return email;
	}
}
