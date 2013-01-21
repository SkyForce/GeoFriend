package ru.allgage.geofriend;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	public GCMIntentService() {         
		 super("119606192268");  
	}
	
	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMessage(Context arg0, Intent intent) {
		// TODO Auto-generated method stub
		String command = intent.getStringExtra("command");
		String log = intent.getStringExtra("user");
		
		if(command.equals("offline")) {
			new UpdateMap().execute("offline", log);	
		}
		else if(command.equals("updateStatus")){
			double lat = Double.parseDouble(intent.getStringExtra("lat"));
			double lng = Double.parseDouble(intent.getStringExtra("lng"));
			String text = intent.getStringExtra("status");
			new UpdateMap().execute("updateStatus", log, lat, lng, text);
		}
		else if(command.equals("updatePosition")) {
			double lat = Double.parseDouble(intent.getStringExtra("lat"));
			double lng = Double.parseDouble(intent.getStringExtra("lng"));
			new UpdateMap().execute("updatePosition", log, lat, lng);
		}
	}

	@Override
	protected void onRegistered(Context arg0, String regID) {
		// TODO send data to server
		synchronized(Monitor.getInstance()) {
			TaskSocket sock = new TaskSocket();
			try {
				sock.writeAuth();
				sock.writeMessages("add device", regID);
				sock.in.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Monitor.getInstance().flag = false;
			Monitor.getInstance().notifyAll();
		}
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

}
