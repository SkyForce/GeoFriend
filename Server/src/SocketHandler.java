import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    Statement stmt;

    SocketHandler(Socket sock, Statement stmt) {
        this.sock = sock;
        this.stmt = stmt;
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
                if(user != null && pass != null) {
                    ResultSet rs = stmt.executeQuery("SELECT password FROM users WHERE login = '"+user+"'");
                    while(rs.next()) {
                        String res = rs.getString(1);
                        if(res != null && pass.equals(res)) isLogged = true;
                    }
                }
            }
            else if(auth.equals("register")) {
                String user = din.readUTF();
                String pass = din.readUTF();
                String email = din.readUTF();
                ResultSet rs = stmt.executeQuery(String.format("SELECT password FROM users WHERE login = '%s'",user));
                if(!rs.next()) {
                    int n = stmt.executeUpdate(String.format("INSERT INTO users VALUES('%s', '%s', '%s')", user, pass, email));
                    if(n > 0) isLogged = true;
                }
            }

            if(isLogged) {
                dout.writeUTF("logged in");
            }
            else {
                dout.writeUTF("error");
                return;
            }

            String str = din.readUTF();

            dout.writeUTF("Twice:56:56:lol");

            while(true) {}
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
        }

    }
}
