package ru.allgage.geofriend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;


public class LoginTask extends AsyncTask<String, String, String> {
	Socket sock;
	DataOutputStream dout;
    DataInputStream din;
    String msg = "";
    boolean isLogged = false;
    TextView view;
    Context parent;

    LoginTask(TextView v, Context cont) {
    	view = v;
    	parent = cont;
    }
    
	
	@Override
	public String doInBackground(String... str) {
		// TODO Auto-generated method stub
		try {
			sock = new Socket("192.168.1.101",7777);
			synchronized(sock) {
				TaskSocket.setSocket(sock);
				din = TaskSocket.in;
				publishProgress("sending data to server");
				if(str.length == 2) {
					TaskSocket.writeMessages("login", str[0], str[1]);
				}
				else {
					TaskSocket.writeMessages("register", str[0], str[1], str[2]);
				}
				String res = din.readUTF();
				return res;
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	protected void onPostExecute(String result) {
        view.setText(result);
        if(result.equals("logged in")) {
        	Intent intent = new Intent(parent, MapActivity.class);
			parent.startActivity(intent);
        } else
			try {
				sock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
	
}
