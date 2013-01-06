package ru.allgage.geofriend.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * Client socket handler thread.
 */
public class SocketHandler implements Runnable {
	final Socket socket;
	final UserDAO userDAO;

	boolean isLogged = false;

	/**
	 * Creates a socket handler.
	 * @param socket client socket.
	 * @param userDAO user data access object.
	 */
	SocketHandler(Socket socket, UserDAO userDAO) {
		this.socket = socket;
		this.userDAO = userDAO;
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

			String auth = din.readUTF();

			if(auth.equals("login")) {
				String user = din.readUTF();
				String pass = din.readUTF();

				isLogged = userDAO.exist(user, pass);
			} else if(auth.equals("register")) {
				String user = din.readUTF();
				String pass = din.readUTF();
				String email = din.readUTF();

				isLogged = userDAO.create(user, pass, email);
			}

			if(isLogged) {
				dout.writeUTF("logged in");
			}
			else {
				dout.writeUTF("error");
				return;
			}
			dout.flush();
			String str = din.readUTF();
			Random rnd = new Random();

			while(true) {
				dout.writeUTF(String.format("Twice:%s:%s:lol", String.valueOf(rnd.nextInt(180)-90), String.valueOf(rnd.nextInt(360)-180)));
				dout.flush();
				din.readUTF();
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
