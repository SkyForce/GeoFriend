package ru.allgage.geofriend;

import com.google.android.gms.maps.model.LatLng;

public class User {
	private LatLng location;
	private String login;
	
	User(String log, LatLng loc) {
		login = log;
		location = loc;
	}
	
	public String getLogin() {
		return login;
	}
	
}
