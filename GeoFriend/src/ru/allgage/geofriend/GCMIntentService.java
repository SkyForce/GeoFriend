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
		String log = intent.getStringExtra("user");
		String offline = intent.getStringExtra("offline");
		if(offline == null) {
			double lat = Double.parseDouble(intent.getStringExtra("lat"));
			double lng = Double.parseDouble(intent.getStringExtra("lng"));
			String text = intent.getStringExtra("status");
			new UpdateMap().execute(log, lat, lng, text);
		}
		else {
			new UpdateMap().execute(log, offline);
		}
	}

	@Override
	protected void onRegistered(Context arg0, String regID) {
		// TODO send data to server
		TaskSocket sock = new TaskSocket();
		try {
			sock.writeAuth();
			sock.writeMessages("add device", regID);
			sock.in.readUTF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

}
