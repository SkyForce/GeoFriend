package ru.allgage.geofriend;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class UserActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		setTitle(getIntent().getStringExtra("login"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_user, menu);
		return true;
	}

}