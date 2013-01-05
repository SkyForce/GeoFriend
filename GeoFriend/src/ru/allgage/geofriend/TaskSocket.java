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
		socket = sock;
		try {
			in = new DataInputStream(sock.getInputStream());
			out = new DataOutputStream(sock.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
