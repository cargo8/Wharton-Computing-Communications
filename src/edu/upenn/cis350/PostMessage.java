package edu.upenn.cis350;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/* This activity is shown when the user wants to post a new message.
 * It includes functionality to insert the message into the database and
 * transition to the ShowEvent view for this Event.
 */
public class PostMessage extends Activity {
	
	private ParseObject event;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.postmessage);
		Parse.initialize(this, "FWyFNrvpkliSb7nBNugCNttN5HWpcbfaOWEutejH", "SZoWtHw28U44nJy8uKtV2oAQ8suuCZnFLklFSk46");
		Bundle extras = this.getIntent().getExtras();
		if(extras != null){
			ParseQuery query = new ParseQuery("Event");
			query.getInBackground(extras.getString("eventKey"), new GetCallback() {

				@Override
				public void done(ParseObject arg0, ParseException arg1) {
					// TODO Auto-generated method stub
					event = arg0;
				}
			});
		}
	}
	 
	// onClick function for Post button
	public void onPostClick(View view){
		TextView tv = (TextView)findViewById(R.id.messageBox);
		ParseObject mes = new ParseObject("Message");
		mes.put("author", ParseUser.getCurrentUser());
		mes.put("text", tv.getText().toString());
		mes.put("timestamp", System.currentTimeMillis());
		mes.put("event", event.getObjectId());
    	final Toast success = Toast.makeText(this, "Message posted.", Toast.LENGTH_SHORT);
    	final Toast failure = Toast.makeText(this, "Message NOT posted.", Toast.LENGTH_SHORT);

    	final Intent i = new Intent(this, ShowEvent.class);

		mes.saveInBackground(new SaveCallback(){

			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				if(e == null){
					success.show();
					i.putExtra("eventKey", event.getObjectId());
					startActivity(i);
				}
				else{
					failure.setText(e.getMessage());
					failure.show();
				}

			}
		
		});
	}

}
