package ru.allgage.geofriend;

import java.io.IOException;
import java.util.HashMap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.AsyncTask;

public class UpdateMap extends AsyncTask<Object, Object, Void> {

	private static GoogleMap gmap;
    private static HashMap<String, Marker> markers;
	
	public static void setMap(GoogleMap mp) {
		gmap = mp;
		markers = new HashMap<String, Marker>();
	}
	
	@Override
	protected Void doInBackground(Object... status) {
		// TODO Auto-generated method stub
		if(gmap != null) {
			if(status.length == 1) {
				synchronized(TaskSocket.socket) {
					try {
						TaskSocket.out.writeUTF("getOnlineStatuses");
						String isEnd;
						while(!(isEnd = TaskSocket.in.readUTF()).equals("end")) {
							String login = TaskSocket.in.readUTF();
							double lat = TaskSocket.in.readDouble();
							double lng = TaskSocket.in.readDouble();
							String txt = TaskSocket.in.readUTF();
							boolean isOnline = TaskSocket.in.readBoolean();
							publishProgress(login, lat, lng, txt, isOnline);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			else if(status.length == 4) {
				publishProgress(status);			    
			}
			else {
				publishProgress(status);
			}
		}
		return null;
	}
	
	protected void onProgressUpdate(Object... status) {
		synchronized(markers) {
			if(status.length >= 4) {
				Marker mrk = markers.get(status[0]);
				if(mrk == null) {
					mrk = (gmap.addMarker(new MarkerOptions()
									.position(new LatLng((Double)status[1],(Double)status[2]))
									.title((String)status[0])
									.snippet((String)status[3])));
					markers.put(mrk.getTitle(), mrk);
					
				}
				else {
					mrk.setPosition(new LatLng((Double)status[1],(Double)status[2]));
					mrk.setSnippet((String)status[3]);
				}
				
				mrk.showInfoWindow();			    
			}
			else {
				Marker mrk = markers.get(status[0]);
				if(mrk != null) {
					mrk.hideInfoWindow();
					mrk.remove();
					markers.remove(status[0]);
				}
			}
		}
	}
		

}
