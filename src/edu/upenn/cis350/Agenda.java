package edu.upenn.cis350;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;


public class Agenda extends Activity {

	private LinearLayout[] events;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda);
        
        events = new LinearLayout[10];
        events[0] = (LinearLayout) findViewById(R.id.event1);
        events[1] = (LinearLayout) findViewById(R.id.event2);
        events[2] = (LinearLayout) findViewById(R.id.event3);
        events[3] = (LinearLayout) findViewById(R.id.event4);
        events[4] = (LinearLayout) findViewById(R.id.event5);
        events[5] = (LinearLayout) findViewById(R.id.event6);
        events[6] = (LinearLayout) findViewById(R.id.event7);
        events[7] = (LinearLayout) findViewById(R.id.event8);
        events[8] = (LinearLayout) findViewById(R.id.event9);
        events[9] = (LinearLayout) findViewById(R.id.event10);
        
    }
    
    public void showEvent(View view) {
    	Intent i = new Intent(this, ShowEvent.class);
    	startActivity(i);
    }
}
