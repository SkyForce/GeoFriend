package ru.allgage.geofriend.server;

import java.util.Date;
import java.sql.Timestamp;

/**
 * SQL helper methods.
 */
public abstract class SQLHelper {
	/**
	 * Converts java.util.Date to java.sql.Timestamp.
	 * @param date java.util.Date instance.
	 * @return java.sql.Timestamp instance.
	 */
	public static Timestamp convertDate(Date date) {
		return new Timestamp(date.getTime());
	}
}
