package edu.upenn.cis350;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Agenda extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda);
        List<EventPOJO> eventList = getEvents();
        LinearLayout eventPane = (LinearLayout) findViewById(R.id.agendaEvents);
        
        for (EventPOJO event : eventList) {
        	LinearLayout eventFrame = new LinearLayout(this);
        	// Vertical Orientation
        	eventFrame.setOrientation(1);
        	eventFrame.setPadding(1, 1, 1, 1);
        	
        	TextView title = new TextView(this);
        	title.setText(event.getEventTitle());
        	eventFrame.addView(title);
        	
        	TextView timeframe = new TextView(this);
        	timeframe.setText(event.getStart() + " - " + event.getEnd());
        	eventFrame.addView(timeframe);
        	
        	TextView description = new TextView(this);
        	description.setText(event.getEventDesc());
        	eventFrame.addView(description);
        	
			final Intent i = new Intent(this, ShowEvent.class);
			i.putExtra("eventPOJO", event);

        	eventFrame.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(i);
				}
        		
        	});
        	eventPane.addView(eventFrame);
        }
    }
    
    public void showEvent(View view) {
    	Intent i = new Intent(this, ShowEvent.class);
    	startActivity(i);
    }
    
    public List<EventPOJO> getEvents(){
    	List<EventPOJO> eventList = new ArrayList<EventPOJO>();
    	
		AndroidOpenDbHelper androidOpenDbHelperObj = new AndroidOpenDbHelper(this);

		// Get a readable SQLite database, because we are going to read all scheduled tasks from the DB
		SQLiteDatabase sqliteDatabase = androidOpenDbHelperObj.getReadableDatabase();

		//(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
		Cursor cursor = sqliteDatabase.query(AndroidOpenDbHelper.TABLE_NAME_EVENTS, null, null, null, null, null, null);
		startManagingCursor(cursor);
		
		while (cursor.moveToNext()) {
			String eventTitle = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_EVENT_TITLE));
			String eventDesc = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_EVENT_DESC));
			int eventSeverity = cursor.getInt(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_EVENT_SEVERITY));

			String eventStart = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_EVENT_START));
			String eventEnd = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_EVENT_END));
			
			String eventContact1 = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_EVENT_CONTACT1));
			String eventContact2 = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_EVENT_CONTACT2));

			String eventActions = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_EVENT_ACTIONS));
			String eventAffils = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_EVENT_AFFILS));
			String eventSystems = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_EVENT_SYSTEMS));

			EventPOJO event = new EventPOJO();
			event.setEventTitle(eventTitle);
			event.setEventDesc(eventDesc);
			event.setSeverity(eventSeverity);
			event.setStart(eventStart);
			event.setEnd(eventEnd);
			event.setContact1(eventContact1);
			event.setContact2(eventContact2);
			event.setEventActions(eventActions);
			String[] affils = eventAffils.split("\t");
			for (String s : affils) {
				if(!s.equals(""))
					event.addToAffils(s);
			}
			String[] systems = eventSystems.split("\t");
			for (String s : systems) {
				if(!s.equals(""))
					event.addToSystems(s);
			}
			
			eventList.add(event);
		}
		
		sqliteDatabase.close();
		
//		Toast.makeText(this, "Values inserted column ID is :" + affectedColumnId, Toast.LENGTH_SHORT).show();
		return eventList;
	}
}
