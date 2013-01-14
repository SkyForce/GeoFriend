package ru.allgage.geofriend.server;

/**
 * User class.
 */
public class User {
	private int id;
	private String login;
	private String email;
    private boolean isOnline;
    private String reg_id;

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

    public String getRegId() {
        return reg_id;
    }

}
