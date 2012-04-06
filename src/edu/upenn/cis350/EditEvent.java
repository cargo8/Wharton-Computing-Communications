package edu.upenn.cis350;

import java.util.Date;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditEvent extends Activity {
	
	private ParseObject event;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Parse.initialize(this, "FWyFNrvpkliSb7nBNugCNttN5HWpcbfaOWEutejH", "SZoWtHw28U44nJy8uKtV2oAQ8suuCZnFLklFSk46");
        setContentView(R.layout.eventform);
        Bundle extras = this.getIntent().getExtras();
        if(extras != null){
        	//event = (EventPOJO)extras.get("eventPOJO");
           	//uname = extras.getString("user");
        	ParseQuery query = new ParseQuery("Event");
        	
        	final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
			query.getInBackground(extras.getString("eventKey"), new GetCallback() {

				@Override
				public void done(ParseObject event1, ParseException e) {
					if (event1 == null) {
						toast.setText(e.getMessage());
						toast.show();
					} else {
						event = event1;
						EditText temp = (EditText)findViewById(R.id.eventTitle);
			        	temp.setText(event.getString("title"));
			        	temp = (EditText)findViewById(R.id.eventDesc);
			        	temp.setText(event.getString("description"));
			        	//temp = (TextView)findViewById(R.id.eventActionsText);
			        	//temp.setText(event.getString("actionItems" + "\n"));
			        	TextView temp2 = (TextView)findViewById(R.id.startDateDisplay);
			        	Date date1 = new Date(event.getLong("startDate"));
			        	temp2.setText(date1.toString());
			        	temp2 = (TextView)findViewById(R.id.endDateDisplay);
			        	Date date2 = new Date(event.getLong("endDate"));
			        	temp2.setText(date2.toString());
			        	populateSpinners();
			        	/*
			        	temp = (TextView)findViewById(R.id.affilsText);
			        	
			        	List<String> affilList = event.getList("affils");
			        	StringBuilder affilText = new StringBuilder();
			        	if(affilList != null){
			        		for(String s : affilList){
			        			affilText.append(s + "\t");
			        		}
			        		temp.setText(affilText.toString());
			        	}
			        	temp = (TextView)findViewById(R.id.systemsText);
			        	
			        	List<String> systemList = event.getList("systems");
			        	StringBuilder systemText = new StringBuilder();
			        	if(systemList != null){
			        		for(String s : systemList){
			        			systemText.append(s + "\t");
			        		}
			        		temp.setText(systemText.toString());
			        	}
			        	*/
			        	/*
			        	temp = (TextView)findViewById(R.id.personText1);
			        	temp.setText(event.getString("contact1"));
			        	temp = (TextView)findViewById(R.id.personText2);
			        	temp.setText(event.getString("contact2"));
			        	
			        	temp2 = (TextView)findViewById(R.id.severityText);
			        	temp.setBackgroundColor(event.getInt("severity"));
			        	temp = (TextView)findViewById(R.id.typeText);
			        	temp.setText(event.getString("type"));
			        	*/
					}
				}
				
			});
        	
        }
    }
    
    private void populateSpinners() {
    	
        final Spinner spinner = (Spinner) findViewById(R.id.personSpinner1);
        final ArrayAdapter <CharSequence> adapter =
        	  new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        final Spinner spinner2 = (Spinner) findViewById(R.id.personSpinner2);
        final ArrayAdapter <CharSequence> adapter2 =
        	  new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        ParseQuery query = new ParseQuery("_User");
        query.orderByAscending("lname");
        
    	final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT); 
    	
    	query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> contactList, ParseException e) {
				if (e == null) {
					int pos1 = 0;
					int pos2 = 0;
					boolean found1 = false;
					boolean found2 = false;
					for(ParseObject obj : contactList){
						// typesafe??
						String formattedName = obj.getString("lname") + ", " + obj.getString("fname");
						adapter.add(formattedName);
						adapter2.add(formattedName);
						if(formattedName.equals(event.getString("contact1")))
							found1 = true;
						if(!found1)
							pos1++;
						
						if(formattedName.equals(event.getString("contact2")))
							found2 = true;
						if(!found2)
							pos2++;
					}
			        spinner.setAdapter(adapter);
			        spinner2.setAdapter(adapter2);
			        spinner.setSelection(pos1);
			        spinner2.setSelection(pos2);
			        
			        
				} else {
					toast.setText("Error: " + e.getMessage());
					toast.show();
					return;
				}
				
			}
    		
    	});
    	
	}

}
