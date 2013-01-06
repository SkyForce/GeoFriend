package ru.allgage.geofriend.server;

import ru.allgage.geofriend.server.UserDAO;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Никита
 * Date: 30.12.12
 * Time: 21:10
 * To change this template use File | Settings | File Templates.
 */
public class SocketHandler implements Runnable {
    Socket sock;
    boolean isLogged = false;
    UserDAO userDAO;

    SocketHandler(Socket sock, UserDAO userDAO) {
        this.sock = sock;
        this.userDAO = userDAO;
        System.out.println(sock.getInetAddress().toString());
    }

    public void run() {
        try (Socket socket = sock;
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
            /**String msg="";

            while(!msg.equals("exit")) {
                msg = din.readUTF();
                System.out.println(msg);
                dout.writeUTF("Got your msg "+msg);
                dout.flush();
            }**/

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
