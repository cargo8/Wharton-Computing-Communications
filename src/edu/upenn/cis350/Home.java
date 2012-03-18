package edu.upenn.cis350;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Home extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Bundle extras = this.getIntent().getExtras();

    }
    
    public void onCreateEventClick(View view){
    	Intent i = new Intent(this, CreateNewEvent.class);
    	startActivity(i);
    }
    
    public void onShowEventClick(View view){
    	Intent i = new Intent(this, ShowEvent.class);
    	startActivity(i);
    }
    
    public void onShowAgenda(View view) {
    	Intent i = new Intent(this, Agenda.class);
    	startActivity(i);
    }
    
    public void onShowComments(View view) {
    	Intent i = new Intent(this, ShowComments.class);
    	startActivity(i);
    }
}
