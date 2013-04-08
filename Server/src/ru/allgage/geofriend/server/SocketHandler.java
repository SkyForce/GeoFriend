package ru.allgage.geofriend.server;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Client socket handler thread.
 */
public class SocketHandler implements Runnable {
	private final String ERROR_HEADER = "error";
	private final String LOGGED_IN_HEADER = "logged in";
	private final String USER_STATUS_HEADER = "status";

	private final Socket socket;
	private final UserDAO userDAO;
	private final StatusDAO statusDAO;
	private final CoordinateDAO coordinateDAO;

	private User loggedUser;

	final Sender sender;

	/**
	 * Creates a socket handler.
	 *
	 * @param socket    client socket.
	 * @param userDAO   user data access object.
	 * @param statusDAO status data access object.
	 */
	SocketHandler(Socket socket, UserDAO userDAO, StatusDAO statusDAO, CoordinateDAO coordinateDAO) {
		this.socket = socket;
		this.userDAO = userDAO;
		this.statusDAO = statusDAO;
		this.coordinateDAO = coordinateDAO;

		sender = new Sender("AIzaSyBjswMkWZaA3SSSXvbvcEMpqFUxR8p_E1M");
		System.out.println(socket.getInetAddress().toString());
	}

	/**
	 * Runs the handler.
	 */
	@Override
	public void run() {
		try (Socket socket = this.socket;
			 DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
			 DataInputStream din = new DataInputStream(socket.getInputStream())) {
			socket.setKeepAlive(true);

			String user = din.readUTF();
			String pass = din.readUTF();

			String command = din.readUTF();

			loggedUser = userDAO.load(user, pass);

			if (command.equals("login")) {
				if (loggedUser == null) {
					writeError(dout, "invalid login or password");
					return;
				}
				writeLoggedIn(dout);

			} else if (command.equals("register")) {
				String email = din.readUTF();
                String info = din.readUTF();
				loggedUser = userDAO.create(user, pass, email, info);
				if (loggedUser == null) {
					writeError(dout, "cannot register user");
					return;
				}
				writeLoggedIn(dout);
			} else if (loggedUser == null) {
				writeError(dout, "invalid login or password");
				return;
			} else if (command.equals("updateStatus")) {
				double lat = din.readDouble();
				double lng = din.readDouble();
				String status = din.readUTF();
				if (statusDAO.create(loggedUser, lat, lng, status)) {
					writeMessages(dout, "success");
					Message msg = new Message.Builder()
                            .addData("command", "updateStatus")
							.addData("user", loggedUser.getLogin())
							.addData("lat", String.valueOf(lat))
							.addData("lng", String.valueOf(lng))
							.addData("status", status)
							.build();
					List<String> list = userDAO.getOnlineIDs();
					if (!list.isEmpty() && sender != null)
						sender.send(msg, list, 2);
				} else {
					writeError(dout, "error updating status");
				}
			} else if (command.equals("updatePosition")) {
				double lat = din.readDouble();
				double lng = din.readDouble();
				if (coordinateDAO.update(loggedUser, lat, lng)) {
                    writeMessages(dout, "success");
                    Message msg = new Message.Builder()
                            .addData("command", "updatePosition")
                            .addData("user", loggedUser.getLogin())
                            .addData("lat", String.valueOf(lat))
                            .addData("lng", String.valueOf(lng))
                            .build();
                    List<String> list = userDAO.getOnlineIDs();
                    if (!list.isEmpty())
                        sender.send(msg, list, 2);
				} else {
					writeError(dout, "Error updating coordinates");
				}
			} else if (command.equals("getOnlineStatuses")) {
				writeStatuses(dout, statusDAO.getOnlineStatuses());
			} else if (command.equals("updateAllStatuses")) {
				long timestamp = din.readLong();
				writeStatuses(dout, statusDAO.getActualStatuses(timestamp));
			} else if (command.equals("getUserStatuses")) {
				String userLogin = din.readUTF();
                writeMessages(dout, userDAO.getInfo(userLogin));
				writeStatuses(dout, statusDAO.getHistoricalStatuses(userLogin));
			} else if (command.equals("add device")) {
				String regID = din.readUTF();
				userDAO.updateRegID(regID, loggedUser.getId());
				dout.writeUTF("ok");
			} else if (command.equals("offline")) {
				userDAO.setOffline(loggedUser.getId());
				Message msg = new Message.Builder()
                        .addData("command", "offline")
						.addData("user", loggedUser.getLogin())
						.build();
				List<String> list = userDAO.getOnlineIDs();
				try {
					if (!list.isEmpty())
						sender.send(msg, list, 2);
				} catch (IOException e) {
					e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
				}
			} else {
				writeError(dout, "invalid command sequence");
				return;
			}


			Random rnd = new Random();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	/**
	 * Writes one or many strings to the stream, flushing it afterwards.
	 *
	 * @param stream   data stream.
	 * @param messages messages to write.
	 * @throws IOException thrown in case something going wrong.
	 */
	private void writeMessages(DataOutputStream stream, String... messages) throws IOException {
		for (String message : messages) {
			stream.writeUTF(message);
		}

		stream.flush();
	}

	/**
	 * Writes the proper-formed error to the data stream.
	 *
	 * @param stream  data stream.
	 * @param message error message.
	 * @throws IOException thrown in case something going wrong.
	 */
	private void writeError(DataOutputStream stream, String message) throws IOException {
		writeMessages(stream, ERROR_HEADER, message);
	}

	/**
	 * Writes the "logged in" message to the data stream.
	 *
	 * @param stream data stream.
	 * @throws IOException thrown in case something going wrong.
	 */
	private void writeLoggedIn(DataOutputStream stream) throws IOException {
		writeMessages(stream, LOGGED_IN_HEADER);
	}

	/**
	 * Writes the status collection to the data stream.
	 *
	 * @param stream      the data stream.
	 * @param coordinates the coordinates collection.
	 * @throws IOException thrown in case something going wrong.
	 */
	private void writeStatuses(DataOutputStream stream, Collection<Coordinate> coordinates) throws IOException {
		for (Coordinate coordinate : coordinates) {
			writeCoordinate(stream, coordinate);
		}

		writeMessages(stream, "end");
	}

	/**
	 * Writes the coordinate message to the data stream.
	 *
	 * @param stream     data stream.
	 * @param coordinate user coordinate.
	 * @throws IOException thrown in case something going wrong.
	 */
	private void writeCoordinate(DataOutputStream stream, Coordinate coordinate) throws IOException {
		stream.writeUTF(USER_STATUS_HEADER);
		stream.writeUTF(coordinate.getUser().getLogin());
		stream.writeDouble(coordinate.getLatitude());
		stream.writeDouble(coordinate.getLongitude());
		stream.writeUTF(coordinate.getStatus().getText());
		stream.writeBoolean(coordinate.getUser().isOnline());
		stream.flush();
	}
}
