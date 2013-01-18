package ru.allgage.geofriend;

import java.io.IOException;

import android.os.AsyncTask;
import android.app.*;

public class CloseTask extends AsyncTask<Object, Void, Void> {

	Activity act;
	TaskSocket sock;
	
	public CloseTask(Activity activity) {
		act = activity;
	}
	
	@Override
	protected Void doInBackground(Object... params) {
		// TODO Auto-generated method stub
		sock = new TaskSocket();
		try {
			sock.writeAuth();
			sock.writeMessages("offline");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		act.finish();
		return null;
	}
	
	@Override
	protected void onPostExecute(Void res) {
		sock.close();
	}

}
