package ru.allgage.geofriend;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class GetStatusesTask extends AsyncTask<String, String, Void> {

	ArrayList<String> statuses;
	ListView lv; 
	Context context;
	TaskSocket sock;
	String info;
	EditText inf;
	
	GetStatusesTask(ListView l, EditText ed, Context cont) {
		lv = l;
		statuses = new ArrayList<String>();
		context = cont;
		inf = ed;
	}
	
	@Override
	protected Void doInBackground(String... arg) {
		// TODO Auto-generated method stub
		synchronized(Monitor.getInstance()) {
			try {
				sock = new TaskSocket();
				sock.writeAuth();
				sock.out.writeUTF("getUserStatuses");
				sock.out.writeUTF(arg[0]);
				sock.out.flush();
				info = sock.in.readUTF();
				String status = sock.in.readUTF();
				while(!status.equals("end")) {
					sock.in.readUTF(); // login
					double lat = sock.in.readDouble();
					double lng = sock.in.readDouble();
					String text = sock.in.readUTF();
					statuses.add(text);
					boolean isOn = sock.in.readBoolean();
					status = sock.in.readUTF();
				}
			}
			catch(IOException e) {
				
			}
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Void arg) {
		// создаем адаптер
		inf.setText(info);
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
	        android.R.layout.simple_list_item_1, statuses);
	    
	    // присваиваем адаптер списку
	    lv.setAdapter(adapter);
	    sock.close();
    }

}
