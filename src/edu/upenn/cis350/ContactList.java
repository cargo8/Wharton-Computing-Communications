package edu.upenn.cis350;

import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContactList extends Activity {

	/**
	 *  Called when the activity is first created.
	 *  Makes query to fetch and populate contact list
	 *  
	 **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "FWyFNrvpkliSb7nBNugCNttN5HWpcbfaOWEutejH", "SZoWtHw28U44nJy8uKtV2oAQ8suuCZnFLklFSk46");
        setContentView(R.layout.contacts);
        
        ParseQuery query = new ParseQuery("_User");
        query.orderByDescending("fullName");
        
    	final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT); 
    	
    	query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> contactList, ParseException e) {
				if (e == null) {
					LinearLayout panel = (LinearLayout) findViewById(R.id.contactList);
					for (ParseObject c : contactList) {
						LinearLayout contactFrame = createContactFrame(c);
						panel.addView(contactFrame);
					}
					toast.setText("Retrieved " + contactList.size() + " contacts");
					toast.show();
				} else {
					toast.setText("Error: " + e.getMessage());
					toast.show();
					return;
				}
				
			}
    		
    	});
    }
    
    /**
     * Creates a LinearLayout frame for the Contact to display
     * @param contact ParseObject representing contact to be displayed
     * @return LinearLayout frame for contact to be displayed
     */
    public LinearLayout createContactFrame(ParseObject contact) {
    	final LinearLayout frame = new LinearLayout(this);
    	// Vertical Orientation
    	frame.setOrientation(1);
    	frame.setPadding(1, 1, 1, 1);
    	
    	TextView name = new TextView(this);
    	name.setText(contact.getString("fullName"));
    	
    	frame.addView(name);
    	
		return frame;
    }
}
