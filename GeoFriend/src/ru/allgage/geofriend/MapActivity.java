package ru.allgage.geofriend;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MapActivity extends FragmentActivity {
	private static final String SENDER_ID = "119606192268";
	private GoogleMap mMap;
	LocationManager locationManager;
	LocationListener listener;
	String status;
	long lastTime = 0;
	HashMap<String, Marker> markers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		 
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mMap = mapFragment.getMap();
		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		if(mMap != null) {
			UpdateMap.setMap(mMap);
			new UpdateMap().execute(0);
		}
		
		listener = new LocationListener() {

		    @Override
		    public void onLocationChanged(Location location) {
		        new SendTask().execute(location.getLatitude(), location.getLongitude(), status);
		    }

			@Override
			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub
				
			}
		};
		
		/**locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		        600000,          // 600-second interval.
		        0,             // 10 meters.
		        listener);**/
		SharedPreferences preferences = getSharedPreferences("settings", 0);
		status = preferences.getString("status", "");
		
		//locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 240000, 20, listener);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
	    {
			new CloseTask(this).execute();
	    }
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 5f, listener);
		
		markers = new HashMap<String, Marker>();
		//toCallAsynchronous();
		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		
		if (regId.equals("")) {
		  GCMRegistrar.register(this, SENDER_ID);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.item1:
	        Intent intent = new Intent(this, StatusActivity.class);
	        startActivityForResult(intent, 1);
	        return true;
	    case R.id.item2:
	    	locationManager.removeUpdates(listener);
	    	GCMRegistrar.unregister(this);
	    	new CloseTask(this).execute();
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) return;
	    status = data.getStringExtra("status");
	    Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    if(loc != null)
	    	new SendTask().execute(loc.getLatitude(), loc.getLongitude(), status);
	    
	    Editor ed = getSharedPreferences("settings", 0).edit();
	    ed.putString("status", status);
	    ed.commit();
	}
	
	
	@Override
    public void onBackPressed()
    {
		moveTaskToBack(true);
    }

}

