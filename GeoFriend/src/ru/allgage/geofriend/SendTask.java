package ru.allgage.geofriend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class SendTask extends AsyncTask<Object, String, String> {

	TaskSocket sock;
	@Override
	protected String doInBackground(Object... arg) {
		try {
			synchronized(Monitor.getInstance()) {
				if(Monitor.flag) Monitor.getInstance().wait();
				sock = new TaskSocket();
				sock.writeAuth();
				
				sock.out.writeUTF("updateStatus");
				sock.out.writeDouble((Double)arg[0]);
				sock.out.writeDouble((Double)arg[1]);
				sock.out.writeUTF((String)arg[2]);
				sock.out.flush();
				String res = sock.in.readUTF();
				return res;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "error";
	}
	
	protected void onPostExecute(String result) {
        // TODO: add pop-up
		sock.close();
    }

}
