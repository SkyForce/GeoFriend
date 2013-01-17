package ru.allgage.geofriend;

import android.os.AsyncTask;
import android.app.*;

public class CloseTask extends AsyncTask<Activity, Void, Void> {

	@Override
	protected Void doInBackground(Activity... params) {
		// TODO Auto-generated method stub
		synchronized(TaskSocket.socket) {
			TaskSocket.close();
		}
		params[0].finish();
		return null;
	}

}
