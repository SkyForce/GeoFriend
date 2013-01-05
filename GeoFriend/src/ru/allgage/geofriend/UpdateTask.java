package ru.allgage.geofriend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.AsyncTask;


public class UpdateTask extends AsyncTask<Void, String, Void> {

	private GoogleMap map;
	DataOutputStream dout;
    DataInputStream din;
	
	public UpdateTask(GoogleMap mMap) {
		// TODO Auto-generated constructor stub
		map = mMap;
		din = TaskSocket.in;
		dout = TaskSocket.out;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		
		try {
			synchronized(TaskSocket.socket) {
				dout.writeUTF("ready");
				dout.flush();
				while(true) {
					String data = din.readUTF();
					publishProgress(data);
					dout.writeUTF("ok");
					dout.flush();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	protected void onProgressUpdate(String... data) {
		String[] loc = data[0].split(":");
		map.addMarker(new MarkerOptions()
						.position(new LatLng(Double.parseDouble(loc[1]),Double.parseDouble(loc[2])))
						.title(loc[0])
						.snippet(loc[3]));
    }

}
