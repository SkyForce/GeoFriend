package ru.allgage.geofriend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TaskSocket {
	public static Socket socket;
	public static DataInputStream in;
	public static DataOutputStream out;
	
	public static void setSocket(Socket sock) {
		if(socket != null) {
			close();
		}
		socket = sock;
		try {
			socket.setKeepAlive(true);
			in = new DataInputStream(sock.getInputStream());
			out = new DataOutputStream(sock.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes one or many strings to the stream, flushing it afterwards.
	 *
	 * @param messages messages to write.
	 * @throws IOException thrown in case something going wrong.
	 */
	public static void writeMessages(String... messages) throws IOException {
		for (String message : messages) {
			out.writeUTF(message);
		}

		out.flush();
	}


	/**
	 * Writes the status message to the data stream.
	 *
	 * @param userName  user name of the status owner.
	 * @param latitude  latitude of the message.
	 * @param longitude longtitude of the message.
	 * @param message   the message text.
	 * @throws IOException thrown in case something going wrong.
	 */
	public static void writeStatus(
			String userName,
			double latitude,
			double longitude,
			String message) throws IOException {
		// TODO: Proper format double.
		String datagram = String.format("%s:%d:%d:%s", userName, latitude, longitude, message);
		writeMessages(datagram);
	}
	
	public static void close() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
