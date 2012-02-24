package edu.upenn.cis350;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class WhartonComputingCommunicationsActivity extends Activity {
	
	// fields for changing activities
	public static final int ACTIVITY_CreateNewEvent = 1;
	private static final int ACTIVITY_ShowEvent = 2;
	private static final int ACTIVITY_AGENDA = 3;
	private static final int ACTIVITY_SHOWCOMMENTS = 4;

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
    }
    
    public void onCreateEventClick(View view){
    	Intent i = new Intent(this, CreateNewEvent.class);
    	startActivityForResult(i, ACTIVITY_CreateNewEvent);
    }
    
    public void onShowEventClick(View view){
    	Intent i = new Intent(this, ShowEvent.class);
    	startActivityForResult(i, ACTIVITY_ShowEvent);
    }
    
    public void onShowAgenda(View view) {
    	Intent i = new Intent(this, Agenda.class);
    	startActivityForResult(i, ACTIVITY_AGENDA);
    }
    
    public void onShowComments(View view) {
    	Intent i = new Intent(this, ShowComments.class);
    	startActivityForResult(i, ACTIVITY_SHOWCOMMENTS);
    }
}