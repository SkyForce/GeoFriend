package ru.allgage.geofriend;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class UserActivity extends Activity {

	ArrayList<String> statuses;
	String login;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		login = getIntent().getStringExtra("login");
		
		setTitle(login);
		
		ListView lvMain = (ListView) findViewById(R.id.listView1);
		EditText edMain = (EditText) findViewById(R.id.editText1);
		
		new GetStatusesTask(lvMain, edMain, this).execute(login);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_user, menu);
		return true;
	}

}
