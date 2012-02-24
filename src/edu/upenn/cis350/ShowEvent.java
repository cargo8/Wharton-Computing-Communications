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
        	temp = (TextView)findViewById(R.id.affilsText);
        	CharSequence[] temp2 = extras.getCharSequenceArray("affils");
        	boolean[] temp3 = extras.getBooleanArray("affilsChecked");
        	StringBuilder affilText = new StringBuilder();
        	if(temp2 != null && temp3 != null){
        		for(int i = 0; i < temp2.length; i++){
        			if(temp3[i])
        				affilText.append(temp2[i] + "\t");
        		}
        	}
        	temp.setText(affilText.toString());
        	temp = (TextView)findViewById(R.id.systemsText);
        	temp2 = extras.getCharSequenceArray("systems");
        	temp3 = extras.getBooleanArray("systemsChecked");
        	StringBuilder systemText = new StringBuilder();
        	if(temp2 != null && temp3 != null){
        		for(int i = 0; i < temp2.length; i++){
        			if(temp3[i])
        				systemText.append(temp2[i] + "\t");
        		}
        	}
        	temp.setText(systemText.toString());
        	temp = (TextView)findViewById(R.id.personText1);
        	temp.setText((String)extras.get("person1"));
        	temp = (TextView)findViewById(R.id.personText2);
        	temp.setText((String)extras.get("person2"));
        	temp = (TextView)findViewById(R.id.severityText);
        	temp.setBackgroundColor(extras.getInt("sevColor"));
        	
        }
    }
    
    public void onBackToHomeClick(View view){
    	//TODO closen: transition to main activity
    	Intent i = new Intent(this, WhartonComputingCommunicationsActivity.class);
    	startActivity(i);
    }
    
    public void onMessageClick(View view){
    	TextView v = (TextView)view;
    	
    }
	
}
