package ru.allgage.geofriend.server;

import java.util.Date;

/**
 * Status class.
 */
public class Status {
	private int id;
	private User user;
	private Date dateTime;
	private double latitude;
	private double longitude;
	private String text;

	Status(Integer id, User user, Date dateTime, double latitude, double longitude, String text) {
		this.id = id;
		this.user = user;
		this.dateTime = dateTime;
		this.latitude = latitude;
		this.longitude = longitude;
		this.text = text;
	}

	int getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getText() {
		return text;
	}

}
