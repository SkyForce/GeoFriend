package ru.allgage.geofriend.server;

/**
 * Status class.
 */
public class Status {
	private final Integer id;
	private final User user;
	private final String text;

	Status(Integer id, User user, String text) {
		this.id = id;
		this.user = user;
		this.text = text;
	}

	public Integer getId() {
		return id;
	}

	public User getUser() {
		return user;
	}


	public String getText() {
		return text;
	}
}
