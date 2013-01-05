package ru.allgage.geofriend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;


public class SocketTask extends AsyncTask<String, String, Void> {
	Socket sock;
	DataOutputStream dout;
    DataInputStream din;
    String msg = "";
    boolean isLogged = false;
    TextView view;
    Context parent;
    String str = "";
    SocketTask(TextView v, Context cont) {
    	view = v;
    	parent = cont;
    }
    
	
	@Override
	public Void doInBackground(String... str) {
		// TODO Auto-generated method stub
		try {
			sock = new Socket("192.168.1.102",7777);
			dout = new DataOutputStream(sock.getOutputStream());
			din = new DataInputStream(sock.getInputStream());
			if(str.length == 2) {
				dout.writeUTF("login");
				dout.writeUTF(str[0]);
				dout.writeUTF(str[1]);
			}
			else {
				dout.writeUTF("register");
				dout.writeUTF(str[0]);
				dout.writeUTF(str[1]);
				dout.writeUTF(str[2]);
			}
			dout.flush();
			String res = din.readUTF();
			if(res.equals("error")) {
				publishProgress(res);
				return null;
			}
			else {
				Monitor mon = Monitor.getInstance();
				synchronized(mon) {
					publishProgress(res);
					mon.wait();
				}
			}
			dout.writeUTF("ready");
			dout.flush();
			while(true) {
				String data = din.readUTF();
				publishProgress("update",data);
				dout.writeUTF("ok");
				dout.flush();
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				din.close();
				dout.close();
				sock.close();
			} catch (IOException e) {
			}
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(String... v) {
		if(v[0].equals("logged in")) {
			Intent intent = new Intent(parent, MapActivity.class);
			parent.startActivity(intent);
		}
		if(v[0].equals("update")) {
			Intent dataIntent = new Intent();
			dataIntent.setAction("ru.allgage.geofriend.DATA_BROADCAST");
			dataIntent.putExtra("loc", v[1]);
			parent.sendBroadcast(dataIntent);
		}
	}
	
}
