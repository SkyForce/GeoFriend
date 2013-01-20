package ru.allgage.geofriend.server;

import java.util.Date;

/**
 * Coordinate class.
 */
public class Coordinate {
	private final Integer id;
	private final User user;
	private final Status status;
	private final Date dateTime;
	private final Double latitude;
	private final Double longitude;

	Coordinate(Integer id, User user, Status status, Date dateTime, double latitude, double longitude) {
		this.id = id;
		this.user = user;
		this.status = status;
		this.dateTime = dateTime;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Integer getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public Status getStatus() {
		return status;
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
}
