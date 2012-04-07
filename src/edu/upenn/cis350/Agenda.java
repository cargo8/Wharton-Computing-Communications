package edu.upenn.cis350;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


/* This activity shows the events in a list form.
 * Events are separated by type - Emergency and Scheduled.
 * Clicking on an event goes to the ShowEvent view for that Event.
 */
public class Agenda extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda);
		Parse.initialize(this, "FWyFNrvpkliSb7nBNugCNttN5HWpcbfaOWEutejH", "SZoWtHw28U44nJy8uKtV2oAQ8suuCZnFLklFSk46");
        
		ParseQuery query = new ParseQuery("Event");
    	query.orderByAscending("startDate");
    	
    	// Only show events with end date greater than now
    	Long now = System.currentTimeMillis();
    	query.whereGreaterThanOrEqualTo("endDate", now);
    	
    	query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
    	
    	final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT); 
    			
    	final LinearLayout eventPane = (LinearLayout) findViewById(R.id.agendaEvents);
        final LinearLayout emergencyPane = (LinearLayout) findViewById(R.id.agendaEmergency);
        final LinearLayout resolvedPane = (LinearLayout) findViewById(R.id.resolvedEvents);
        final ProgressDialog dialog = ProgressDialog.show(this, "", 
                "Loading. Please wait...", true);
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
    	            	} else if ("Resolved".equals(type)){
    	            		resolvedPane.addView(eventFrame);
    	            	}
    	        	}
    	        	 dialog.cancel();
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
    	eventFrame.setPadding(15, 15, 15, 15);
    	
    	LinearLayout titleFrame = new LinearLayout(this);
    	titleFrame.setOrientation(0);
    	
    	TextView severity = new TextView(this);
    	severity.setWidth(35);
    	severity.setHeight(35);
    	severity.setText("    ");
    	severity.setBackgroundColor(event.getNumber("severity").intValue());
    	
    	TextView title = new TextView(this);
    	title.setText("   " + event.getString("title"));
    	// textSize="16.0sp"
    	title.setTextSize((float)18.0);
    	// gravity="center_horizontal"
    	//title.setGravity(0x01);
    	// textStyle="bold"
    	title.setTypeface(Typeface.DEFAULT_BOLD);
    	
    	titleFrame.addView(severity);
    	titleFrame.addView(title);
    	eventFrame.addView(titleFrame);
    	
    	TextView timeframe = new TextView(this);
    	SimpleDateFormat formatter = new SimpleDateFormat();
    	Date date1 = new Date(event.getLong("startDate"));
    	Date date2 = new Date(event.getLong("endDate"));
    	timeframe.setText("Start: " + formatter.format(date1) + 
    			", Est. Finish: " + formatter.format(date2));
    	timeframe.setTextSize((float)12.0);
    	eventFrame.addView(timeframe);
    	
    	TextView description = new TextView(this);
    	String desc = event.getString("description");
    	if ("".equals(desc)) {
        	description.setText("No description.");
    	} else {
    		description.setText(desc);
    	}
    	description.setTextSize((float)14.0);
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
    
    @Override
    /**
     * Returns to Home screen on back press no matter what
     */
    public void onBackPressed() {
       Intent i = new Intent(this, Home.class);
       startActivity(i);
    }
}
