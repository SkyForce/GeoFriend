package ru.allgage.geofriend.server;

import java.sql.Date;

/**
 * SQL helper methods.
 */
public abstract class SQLHelper {
	/**
	 * Converts java.util.Date to java.sql.Date.
	 * @param date java.util.Date instance.
	 * @return java.sql.Date instance.
	 */
	public static Date convertDate(java.util.Date date) {
		return new Date(date.getTime());
	}
}
