package ru.allgage.geofriend;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class StatusActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_status, menu);
		return true;
	}
	
	public void returnStatus(View v) {
		
		String data = ((TextView) findViewById(R.id.statusText1)).getText().toString();
		
		Intent in = new Intent();
		in.putExtra("status", data);
		setResult(RESULT_OK, in);
		finish();
	}
}
