package ru.allgage.geofriend;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;

public class LoginActivity extends Activity {
	
	SocketTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}
	
	public void regClick(View v) {
		EditText email = (EditText) findViewById(R.id.editText3);
		if(((CheckBox)v).isChecked())
			email.setVisibility(View.VISIBLE);
		else email.setVisibility(View.INVISIBLE);
	}

	public void sendData(View v) {
		boolean checked = ((CheckBox) findViewById(R.id.checkBox1)).isChecked();
		String s1 = ((EditText) findViewById(R.id.editText1)).getText().toString();
		String s2 = ((EditText) findViewById(R.id.editText2)).getText().toString();
		String s3 = null;
		
		task = new SocketTask((TextView) findViewById(R.id.textView1), this);
		
		if(checked) {
			s3 = ((EditText) findViewById(R.id.editText3)).getText().toString();
			task.execute(s1,s2,s3);
		}
		else {
			task.execute(s1,s2);
		}
	}
}
