package edu.upenn.cis350;

import java.util.List;

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
        	EventPOJO event = (EventPOJO)extras.get("eventPOJO");
        	System.out.println(event.getEventTitle());
        	TextView temp = (TextView)findViewById(R.id.eventTitleText);
        	temp.setText(event.getEventTitle());
        	temp = (TextView)findViewById(R.id.eventDescText);
        	temp.setText(event.getEventDesc());
        	temp = (TextView)findViewById(R.id.eventActionsText);
        	temp.setText(event.getEventActions());
        	temp = (TextView)findViewById(R.id.startDateDisplay2);
        	temp.setText(event.getStart());
        	temp = (TextView)findViewById(R.id.endDateDisplay2);
        	temp.setText(event.getEnd());
        	temp = (TextView)findViewById(R.id.affilsText);
        	/*
        	CharSequence[] temp2 = extras.getCharSequenceArray("affils");
        	boolean[] temp3 = extras.getBooleanArray("affilsChecked");
        	StringBuilder affilText = new StringBuilder();
        	if(temp2 != null && temp3 != null){
        		for(int i = 0; i < temp2.length; i++){
        			if(temp3[i])
        				affilText.append(temp2[i] + "\t");
        		}
        	}
        	*/
        	List<String> affilList = event.getAffils();
        	StringBuilder affilText = new StringBuilder();
        	for(String s : affilList){
        		affilText.append(s + "\t");
        	}
        	temp.setText(affilText.toString());
        	temp = (TextView)findViewById(R.id.systemsText);
        	/*
        	temp2 = extras.getCharSequenceArray("systems");
        	temp3 = extras.getBooleanArray("systemsChecked");
        	StringBuilder systemText = new StringBuilder();
        	if(temp2 != null && temp3 != null){
        		for(int i = 0; i < temp2.length; i++){
        			if(temp3[i])
        				systemText.append(temp2[i] + "\t");
        		}
        	}
        	*/
        	List<String> systemList = event.getSystems();
        	StringBuilder systemText = new StringBuilder();
        	for(String s : systemList){
        		systemText.append(s + "\t");
        	}
        	temp.setText(systemText.toString());
        	temp = (TextView)findViewById(R.id.personText1);
        	temp.setText(event.getContact1());
        	temp = (TextView)findViewById(R.id.personText2);
        	temp.setText(event.getContact2());
        	temp = (TextView)findViewById(R.id.severityText);
        	temp.setBackgroundColor(event.getSeverity());
        	
        }
    }
    
    public void onBackToAgendaClick(View view){
    	Intent i = new Intent(this, Agenda.class);
    	startActivity(i);
    }
    
    public void onMessageClick(View view){
    	TextView v = (TextView)view;
    	String message = v.getText().toString();
    	Intent i = new Intent(this, ShowComments.class);
    	i.putExtra("message", message);
    	startActivity(i);
    }
	
}
