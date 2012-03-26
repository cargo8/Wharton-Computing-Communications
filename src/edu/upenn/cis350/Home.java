package edu.upenn.cis350;

import com.parse.Parse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/* This activity is what the app will start with after logging in.
 * For now, it just shows a few buttons to create a new event
 * and to show the agenda.
 * TODO: Make it look more visually appealing
 */
public class Home extends Activity {

	private String userKey;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
        	userKey = extras.getString("userKey");
        }

    }
    
    // onClick function for createEvent button
    public void onCreateEventClick(View view){
    	Intent i = new Intent(this, CreateNewEvent.class);
		i.putExtra("userKey", userKey);
    	startActivity(i);
    }
    
    // onClick function for showAgenda button
    public void onShowAgenda(View view) {
    	Intent i = new Intent(this, Agenda.class);
		i.putExtra("userKey", userKey);
    	startActivity(i);
    }
    
    @Override
    public void onBackPressed() {
       Intent i = new Intent(this, WhartonComputingCommunicationsActivity.class);
       startActivity(i);
    }    
}
