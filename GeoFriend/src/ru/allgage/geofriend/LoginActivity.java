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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;

public class LoginActivity extends Activity {
	
	LoginTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		SharedPreferences preferences = getSharedPreferences("settings", 0);
		String login = preferences.getString("login", "");
		String pass = preferences.getString("password", "");
		((EditText) findViewById(R.id.editText1)).setText(login);
		((EditText) findViewById(R.id.editText2)).setText(pass);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}
	
	public void regClick(View v) {
		EditText email = (EditText) findViewById(R.id.editText3);
		EditText info = (EditText) findViewById(R.id.editText4);
		if(((CheckBox)v).isChecked()) {
			email.setVisibility(View.VISIBLE);
			info.setVisibility(View.VISIBLE);
		}
		else {
			email.setVisibility(View.INVISIBLE);
			info.setVisibility(View.INVISIBLE);
		}
	}

	public void sendData(View v) {
		boolean checked = ((CheckBox) findViewById(R.id.checkBox1)).isChecked();
		String s1 = ((EditText) findViewById(R.id.editText1)).getText().toString();
		String s2 = ((EditText) findViewById(R.id.editText2)).getText().toString();
		String s3 = null, s4 = null;
		
		Editor ed = getSharedPreferences("settings", 0).edit();
		ed.putString("login", s1);
		ed.putString("password", s2);
		ed.commit();
		
		task = new LoginTask((TextView) findViewById(R.id.textView1), this);
		
		if(checked) {
			s3 = ((EditText) findViewById(R.id.editText3)).getText().toString();
			s4 = ((EditText) findViewById(R.id.editText4)).getText().toString();
			task.execute(s1,s2,s3,s4);
		}
		else {
			task.execute(s1,s2);
		}
	}
}
