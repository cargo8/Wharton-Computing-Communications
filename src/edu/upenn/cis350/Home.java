package edu.upenn.cis350;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseUser;

/* This activity is what the app will start with after logging in.
 * For now, it just shows a few buttons to create a new event
 * and to show the agenda.
 * TODO: Make it look more visually appealing
 */
public class Home extends Activity {
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Parse.initialize(this, "FWyFNrvpkliSb7nBNugCNttN5HWpcbfaOWEutejH", "SZoWtHw28U44nJy8uKtV2oAQ8suuCZnFLklFSk46");
        setContentView(R.layout.home);
    }
    
    // onClick function for createEvent button
    public void onCreateEventClick(View view){
    	Intent i = new Intent(this, CreateNewEvent.class);
    	startActivity(i);
    }
    
    // onClick function for showAgenda button
    public void onShowAgenda(View view) {
    	Intent i = new Intent(this, Agenda.class);
    	startActivity(i);
    }
    
    @Override
    public void onBackPressed() {
    	ParseUser.logOut();
    	Intent i = new Intent(this, WhartonComputingCommunicationsActivity.class);
    	startActivity(i);
    }    
}
