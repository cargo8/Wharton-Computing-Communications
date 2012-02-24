package edu.upenn.cis350;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Agenda extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda);
    }
    
    public void onBackToHomeClick(View view){
    	//TODO closen: transition to main activity
    	Intent i = new Intent(this, WhartonComputingCommunicationsActivity.class);
    	startActivity(i);
    }
	
}
