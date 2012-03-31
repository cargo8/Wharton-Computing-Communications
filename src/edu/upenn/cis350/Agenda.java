package edu.upenn.cis350;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/* This activity shows the events in a list form.
 * Events are separated by type - Emergency and Scheduled.
 * Clicking on an event goes to the ShowEvent view for that Event.
 */
public class Agenda extends Activity {
	
	private String userKey;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda);
		Parse.initialize(this, "FWyFNrvpkliSb7nBNugCNttN5HWpcbfaOWEutejH", "SZoWtHw28U44nJy8uKtV2oAQ8suuCZnFLklFSk46");
        
//        List<EventPOJO> eventList = getEvents();
		ParseQuery query = new ParseQuery("Event");
    	query.orderByAscending("startDate");
    	
    	final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT); 
    			
    	final LinearLayout eventPane = (LinearLayout) findViewById(R.id.agendaEvents);
        final LinearLayout emergencyPane = (LinearLayout) findViewById(R.id.agendaEmergency);
        
    	query.findInBackground(new FindCallback() {
    	    
    		@Override
    		public void done(List<ParseObject> eventList, ParseException e) {
    	        if (e == null) {
    	        	for (ParseObject event : eventList) {
    	        		
        	        	LinearLayout eventFrame = createEventFrame(event);
        	        	
    	        		String type = event.getString("type");
    	            	if ("Emergency".equals(type)) {
    	                	emergencyPane.addView(eventFrame);
    	            	} else if ("Scheduled".equals(type)) {
    	            		eventPane.addView(eventFrame);
    	            	} else {
    	            		eventPane.addView(eventFrame);
    	            	}
    	        	}
    	        	
    	             toast.setText("Retrieved " + eventList.size() + " events");
    	             toast.show();
    	        } else {
    	             toast.setText("Error: " + e.getMessage());
    	             toast.show();
    	        }
    	    }

    	});
    }
    
    /**
     * Creates the layout for an Event Panel in the Agenda View
     * 
     * @param event A ParseObject representing an Event
     */
    public LinearLayout createEventFrame(ParseObject event) {
    	final LinearLayout eventFrame = new LinearLayout(this);
    	// Vertical Orientation
    	eventFrame.setOrientation(1);
    	eventFrame.setPadding(1, 1, 1, 1);
    	
    	LinearLayout titleFrame = new LinearLayout(this);
    	titleFrame.setOrientation(0);
    	
    	TextView severity = new TextView(this);
    	severity.setWidth(35);
    	severity.setHeight(35);
    	severity.setText("    ");
    	severity.setBackgroundColor(event.getNumber("severity").intValue());
    	
    	TextView title = new TextView(this);
    	title.setText(event.getString("title"));
    	// textSize="16.0sp"
    	title.setTextSize((float)16.0);
    	// gravity="center_horizontal"
    	//title.setGravity(0x01);
    	// textStyle="bold"
    	title.setTypeface(Typeface.DEFAULT_BOLD);
    	
    	titleFrame.addView(severity);
    	titleFrame.addView(title);
    	eventFrame.addView(titleFrame);
    	
    	TextView timeframe = new TextView(this);
    	timeframe.setText("Start: " + event.getString("startDate") + 
    			", Est. Finish: " + event.getString("endDate"));
    	timeframe.setTextSize((float)12.0);
    	eventFrame.addView(timeframe);
    	
    	TextView description = new TextView(this);
    	description.setText(event.getString("description"));
    	description.setTextSize((float)12.0);
    	eventFrame.addView(description);
    	
		final Intent i = new Intent(this, ShowEvent.class);
		i.putExtra("eventKey", event.getObjectId());

    	eventFrame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//TODO(jmow): Change background on click
//				eventFrame.setBackgroundColor(Color.GRAY);
				startActivity(i);
			}
    		
    	});
    	
    	return eventFrame;
    }
    
    // gets all events from SQLite DB
    public List<EventPOJO> getEvents(){
    	List<EventPOJO> eventList = new ArrayList<EventPOJO>();
    	
		AndroidOpenDbHelper dbHelper = new AndroidOpenDbHelper(this);

		// Get a readable SQLite database, because we are going to read all scheduled tasks from the DB
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		dbHelper.createEventsTable(db);
    	dbHelper.createMessagesTable(db);
		
		//(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
		Cursor cursor = db.query(AndroidOpenDbHelper.TABLE_NAME_EVENTS, null, null, null, null, null, null);
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
			String eventType = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_EVENT_TYPE));
			int eventID = cursor.getInt(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_EVENT_ID));
			
			EventPOJO event = new EventPOJO();
			event.setEventTitle(eventTitle);
			event.setEventDesc(eventDesc);
			event.setSeverity(eventSeverity);
			event.setStart(eventStart);
			event.setEnd(eventEnd);
			event.setContact1(eventContact1);
			event.setContact2(eventContact2);
			event.setEventActions(eventActions);
			event.setType(eventType);
			event.setEventID(eventID);
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
		
		db.close();
		
//		Toast.makeText(this, "Values inserted column ID is :" + affectedColumnId, Toast.LENGTH_SHORT).show();
		return eventList;
	}
    
    @Override
    // changes behavior when back button is pressed
    public void onBackPressed() {
       Intent i = new Intent(this, Home.class);
       startActivity(i);
    }
}
