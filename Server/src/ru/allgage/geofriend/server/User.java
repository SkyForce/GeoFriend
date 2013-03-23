package ru.allgage.geofriend.server;

/**
 * User class.
 */
public class User {
	private final int id;
	private final String login;
	private final String email;
	private final boolean isOnline;
    private final String info;

	User(int id, String login, String email, boolean isOnline, String info) {
		this.id = id;
		this.login = login;
		this.email = email;
		this.isOnline = isOnline;
        this.info = info;
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

    public String getInfo() {
        return info;
    }
}
