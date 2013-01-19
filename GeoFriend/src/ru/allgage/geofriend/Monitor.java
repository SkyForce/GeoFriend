package ru.allgage.geofriend;

public class Monitor {
	private static Monitor mon = new Monitor();
	public static boolean flag = true;
	
	public static Monitor getInstance() {
		return mon;
	}

}
