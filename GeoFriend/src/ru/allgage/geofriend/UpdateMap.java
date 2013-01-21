package ru.allgage.geofriend;

import java.io.IOException;
import java.util.HashMap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.AsyncTask;

public class UpdateMap extends AsyncTask<Object, Object, Void> {

	private static GoogleMap gmap;
    private static HashMap<String, Marker> markers;
    TaskSocket sock;
    private static boolean isFirst = true;
    
	public static void setMap(GoogleMap mp) {
		gmap = mp;
		markers = new HashMap<String, Marker>();
		isFirst = true;
	}
	
	@Override
	protected Void doInBackground(Object... status) {
		// TODO Auto-generated method stub
		if(gmap != null) {
			String command = (String) status[0];
			if(command.equals("allStatuses")) {
				try {
					sock = new TaskSocket();
					sock.writeAuth();
					sock.out.writeUTF("getOnlineStatuses");
					String isEnd;
					while(!(isEnd = sock.in.readUTF()).equals("end")) {
						String login = sock.in.readUTF();
						double lat = sock.in.readDouble();
						double lng = sock.in.readDouble();
						String txt = sock.in.readUTF();
						boolean isOnline = sock.in.readBoolean();
						publishProgress("updateStatus", login, lat, lng, txt);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				publishProgress(status);			    
			}
		}
		return null;
	}
	
	protected void onProgressUpdate(Object... status) {
		synchronized(markers) {
			String command = status[0].toString();
			if(command.equals("offline")) {
				Marker mrk = markers.get(status[1].toString());
				if(mrk != null) {
					mrk.hideInfoWindow();
					mrk.remove();
					markers.remove(status[1]);
				}	    
			}
			else if(command.equals("updateStatus")) {
				if(isFirst) {
					gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng((Double)status[2], (Double)status[3]), 14f), 2000, null);
					isFirst = false;
				}
				Marker mrk = markers.get(status[1].toString());
				if(mrk == null) {
					mrk = (gmap.addMarker(new MarkerOptions()
									.position(new LatLng((Double)status[2],(Double)status[3]))
									.title((String)status[1])
									.snippet((String)status[4])));
					markers.put(mrk.getTitle(), mrk);
					
				}
				else {
					mrk.setPosition(new LatLng((Double)status[2],(Double)status[3]));
					mrk.setSnippet(status[4].toString());
				}
				
				mrk.showInfoWindow();		
			}
			else if(command.equals("updatePosition")) {
				Marker mrk = markers.get(status[1].toString());
				if(mrk != null) {
					mrk.setPosition(new LatLng((Double)status[2],(Double)status[3]));
				}
			}
		}
	}
	
	protected void onPostExecute(String result) {
        // TODO: add pop-up
		sock.close();
    }
		

}
