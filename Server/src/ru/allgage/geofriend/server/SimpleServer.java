package ru.allgage.geofriend.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main server class.
 */
public class SimpleServer {
	static List<Integer> loggedUsers = new ArrayList<Integer>();

    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
		Properties properties = new Properties();
		try (InputStream stream = new FileInputStream("server.properties")) {
			properties.load(stream);
		} catch (IOException exception) {
			exception.printStackTrace();
			System.exit(1);
		}

		String connectionString = properties.getProperty("connection_string");
		int port = Integer.parseInt(properties.getProperty("port"));

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connect = DriverManager.getConnection(connectionString);
		UserDAO userDAO = new UserDAO(connect);
		userDAO.clearOnlines();
        StatusDAO statusDAO = new StatusDAO(connect);

		ServerSocket server = new ServerSocket(port);
		ExecutorService pool = Executors.newCachedThreadPool();
		while (true) {
			Socket sock = server.accept();
			pool.submit(new SocketHandler(sock, userDAO, statusDAO));
		}
	}
}
