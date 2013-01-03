package ru.allgage.geofriend;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class MapActivity extends FragmentActivity {
	private GoogleMap mMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		 
		mMap = SupportMapFragment.newInstance().getMap();

		IntentFilter filter = new IntentFilter();
		filter.addAction("ru.allgage.geofriend.DATA_BROADCAST");
		registerReceiver(new UpdateHandler(mMap), filter);
		
		Intent dataIntent = new Intent();
		dataIntent.setAction("ru.allgage.geofriend.WAIT");
		dataIntent.putExtra("lol", "lol");
		sendBroadcast(dataIntent);
	}

}

class UpdateHandler extends BroadcastReceiver {

	private GoogleMap gmap;

	UpdateHandler(GoogleMap map) {
		gmap = map;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String data = intent.getStringExtra("loc");
		String[] loc = data.split(":");
		gmap.addMarker(new MarkerOptions()
						.position(new LatLng(Double.parseDouble(loc[1]),Double.parseDouble(loc[2])))
						.title(loc[0])
						.snippet(loc[3]));
	}
	
}
