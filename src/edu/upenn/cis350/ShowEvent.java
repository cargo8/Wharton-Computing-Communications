package edu.upenn.cis350;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


//TODO (kuyumcu) Half of this activity will consist of the messages related to a particular event
public class ShowEvent extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showevent);
        Bundle extras = this.getIntent().getExtras();
        if(extras != null){
        	TextView temp = (TextView)findViewById(R.id.eventTitleText);
        	temp.setText((String)extras.get("eventTitle"));
        	temp = (TextView)findViewById(R.id.eventDescText);
        	temp.setText((String)extras.get("eventDesc"));
        	temp = (TextView)findViewById(R.id.eventActionsText);
        	temp.setText((String)extras.get("eventActions"));
        	temp = (TextView)findViewById(R.id.startDateDisplay2);
        	temp.setText((String)extras.get("startDate"));
        	temp = (TextView)findViewById(R.id.endDateDisplay2);
        	temp.setText((String)extras.get("endDate"));
        }
    }
    
    public void onBackToHomeClick(View view){
    	//TODO closen: transition to main activity
    	Intent i = new Intent(this, WhartonComputingCommunicationsActivity.class);
    	startActivity(i);
    }
	
}
