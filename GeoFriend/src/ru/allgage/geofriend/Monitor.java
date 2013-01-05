package ru.allgage.geofriend;

public class Monitor {
	private static Monitor mon = new Monitor();
	public static Monitor getInstance() {
		return mon;
	}
}
