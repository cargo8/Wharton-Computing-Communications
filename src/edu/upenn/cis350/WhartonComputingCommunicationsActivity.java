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

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
    }
    
    public void onCreateEventClick(View view){
    	//TODO closen: transition to CreateNewEvent activity
    	Intent i = new Intent(this, CreateNewEvent.class);
    	startActivityForResult(i, ACTIVITY_CreateNewEvent);
    }
}