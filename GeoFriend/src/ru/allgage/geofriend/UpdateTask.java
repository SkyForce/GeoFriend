package ru.allgage.geofriend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.AsyncTask;


public class UpdateTask extends AsyncTask<Void, Object, Void> {

	private GoogleMap map;
	DataOutputStream dout;
    DataInputStream din;
	
	public UpdateTask(GoogleMap mMap) {
		// TODO Auto-generated constructor stub
		map = mMap;
		din = TaskSocket.in;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		
		try {
			synchronized(TaskSocket.socket) {
				TaskSocket.writeMessages("updateAllStatuses");
				String isEnd;
				while(!(isEnd = din.readUTF()).equals("end")) {
					String login = din.readUTF();
					double lat = din.readDouble();
					double lng = din.readDouble();
					String txt = din.readUTF();
					publishProgress(login, lat, lng, txt);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	protected void onProgressUpdate(Object... status) {
		map.addMarker(new MarkerOptions()
						.position(new LatLng((Double)status[1],(Double)status[2]))
						.title((String)status[0])
						.snippet((String)status[3]));
    }

}
