package edu.upenn.cis350;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;
import android.widget.Toast;

public class ShowContact extends Activity {
	
	String contactId;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_contact);
		Parse.initialize(this, "FWyFNrvpkliSb7nBNugCNttN5HWpcbfaOWEutejH", "SZoWtHw28U44nJy8uKtV2oAQ8suuCZnFLklFSk46");

		 Bundle extras = this.getIntent().getExtras();
	        
	        if (extras == null){
	        	Toast.makeText(this, "Could not load contact.", Toast.LENGTH_LONG);
	        	return;
	        } else {
	        	contactId = extras.getString("contactID");
	        	ParseQuery query = new ParseQuery("_User");
	        	query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
	        	
	        	query.getInBackground(contactId, new GetCallback() {

					@Override
					public void done(ParseObject contact, ParseException e) {
			        	TextView temp = (TextView) findViewById(R.id.contactHeader);
						String tempString = contact.getString("fullName");

						temp.setText(tempString);
						
						temp = (TextView) findViewById(R.id.contactPhone1);
						tempString = contact.getString("phone1");
						if ("".equals(tempString)) tempString = "None";
						temp.setText(tempString);
						Linkify.addLinks(temp, Linkify.PHONE_NUMBERS);
						temp.setLinkTextColor(temp.getTextColors().getDefaultColor());
						
						temp = (TextView) findViewById(R.id.contactPhone2);
						tempString = contact.getString("phone2");
						if ("".equals(tempString)) tempString = "None";
						temp.setText(tempString);
						Linkify.addLinks(temp, Linkify.PHONE_NUMBERS);
						temp.setLinkTextColor(temp.getTextColors().getDefaultColor());

						temp = (TextView) findViewById(R.id.contactEmail1);
						tempString = contact.getString("email1");
						if ("".equals(tempString)) tempString = "None";
						temp.setText(tempString);
						Linkify.addLinks(temp, Linkify.EMAIL_ADDRESSES);
						temp.setLinkTextColor(temp.getTextColors().getDefaultColor());

						temp = (TextView) findViewById(R.id.contactEmail2);
						tempString = contact.getString("email2");
						if ("".equals(tempString)) tempString = "None";
						temp.setText(tempString);
						Linkify.addLinks(temp, Linkify.EMAIL_ADDRESSES);
						temp.setLinkTextColor(temp.getTextColors().getDefaultColor());

					}
	        		
	        	});
	        }
    }

}
