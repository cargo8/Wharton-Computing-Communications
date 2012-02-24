package edu.upenn.cis350;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


//TODO (kuyumcu) Half of this activity will consist of the messages related to a particular event
public class ShowEvent extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showevent);
        Bundle extras = this.getIntent().getExtras();
        if(extras != null){
        	
        }
    }
    
    public void onBackToHomeClick(View view){
    	//TODO closen: transition to main activity
    	Intent i = new Intent(this, WhartonComputingCommunicationsActivity.class);
    	startActivity(i);
    }
	
}
