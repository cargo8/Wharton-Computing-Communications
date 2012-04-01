package edu.upenn.cis350;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/* Displays all information related to a particular Event as well as messages 
 * related to that event. Upon clicking on a message, the user will see
 * a new view where they can comment on a particular message.
 */
public class ShowEvent extends Activity {
	
	private String uname;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showevent);
		Parse.initialize(this, "FWyFNrvpkliSb7nBNugCNttN5HWpcbfaOWEutejH", "SZoWtHw28U44nJy8uKtV2oAQ8suuCZnFLklFSk46");
        Bundle extras = this.getIntent().getExtras();
        if(extras != null){
        	//event = (EventPOJO)extras.get("eventPOJO");
           	//uname = extras.getString("user");
        	ParseQuery query = new ParseQuery("Event");
        	
        	final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
			query.getInBackground(extras.getString("eventKey"), new GetCallback() {

				@Override
				public void done(ParseObject event, ParseException e) {
					if (event == null) {
						toast.setText(e.getMessage());
						toast.show();
					} else {
						TextView temp = (TextView)findViewById(R.id.eventTitleText);
			        	temp.setText(event.getString("title"));
			        	temp = (TextView)findViewById(R.id.eventDescText);
			        	temp.setText(event.getString("description"));
			        	temp = (TextView)findViewById(R.id.eventActionsText);
			        	temp.setText(event.getString("actionItems"));
			        	temp = (TextView)findViewById(R.id.startDateDisplay2);
			        	temp.setText(event.getString("startDate"));
			        	temp = (TextView)findViewById(R.id.endDateDisplay2);
			        	temp.setText(event.getString("endDate"));
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
			        	
			        	temp = (TextView)findViewById(R.id.severityText);
			        	temp.setBackgroundColor(event.getInt("severity"));
			        	temp = (TextView)findViewById(R.id.typeText);
			        	temp.setText(event.getString("type"));
					}
				}
				
			});
        	
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
        	
        	/*
        	temp = (TextView)findViewById(R.id.personText1);
        	temp.setText(event.getContact1());
        	temp = (TextView)findViewById(R.id.personText2);
        	temp.setText(event.getContact2());
        	*/

        	
        }
        populateMessages();
    }
    
    @Override
    public void onBackPressed() {
       Intent i = new Intent(this, Agenda.class);
       /*
       if(event != null)
    	   i.putExtra("eventPOJO", event);
       i.putExtra("user", uname);
       */
       startActivity(i);
    }
    
    // onClick function of backToAgenda button (deprected)
    public void onBackToAgendaClick(View view){
    	Intent i = new Intent(this, Agenda.class);
    	startActivity(i);
    }
    
    // populates the messages in the bottom half of the view from the DB
    public void populateMessages() {
    	List<MessagePOJO> messages = getMessages();
        
        LinearLayout messagesPane = (LinearLayout) findViewById(R.id.messagesPane);
        messagesPane.removeAllViews();
        for (final MessagePOJO m : messages) {
        	LinearLayout messageFrame = new LinearLayout(this);
        	messageFrame.setOrientation(1);
        	messageFrame.setPadding(1, 1, 1, 1);
        	
        	LinearLayout header = new LinearLayout(this);
        	header.setOrientation(0);
        	
        	TextView posted = new TextView(this);
        	posted.setText("Posted by ");
        	
        	TextView author = new TextView(this);
        	author.setText(m.getAuthor());
        	author.setTypeface(Typeface.DEFAULT_BOLD);
        	
        	TextView timestamp = new TextView(this);
        	Long time = Long.parseLong(m.getTimestamp());
        	SimpleDateFormat formatter = new SimpleDateFormat();
        	timestamp.setText(" at " + formatter.format(new Date(time)) + '\n');
        	
        	header.addView(posted);
        	header.addView(author);
        	header.addView(timestamp);
        	
        	TextView messageText = new TextView(this);
        	messageText.setText(m.getText());
        	messageText.setTypeface(Typeface.DEFAULT_BOLD);
        	
        	messageFrame.addView(messageText);
        	messageFrame.addView(header);
        	
        	messageFrame.setOnClickListener(new LinearLayout.OnClickListener() {  
                public void onClick(View v){
                    onMessageClick(m);
                }
             });
        	
        	messagesPane.addView(messageFrame);
        }
    }
    
    // gets the messages from the database 
    // TODO: get the messages from the EventPOJO instead
    public List<MessagePOJO> getMessages() {
    	
    	List<MessagePOJO> messageList = new ArrayList<MessagePOJO>();
    	
    	// First we have to open our DbHelper class by creating a new object of that
    	AndroidOpenDbHelper dbHelper = new AndroidOpenDbHelper(this);

    	// Then we need to get a writable SQLite database, because we are going to insert some values
    	// SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	dbHelper.createCommentsTable(db);
    	dbHelper.createMessagesTable(db);
    	/*
    	Cursor cursor = db.query(AndroidOpenDbHelper.TABLE_NAME_MESSAGES, null, 
    			AndroidOpenDbHelper.COLUMN_NAME_MESSAGE_EVENT + "=" + event.getEventID(), null, null, null, null);
    	startManagingCursor(cursor);
    	
    	while (cursor.moveToNext()) {
    		int num = cursor.getInt(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_MESSAGE_ID));
    		String text = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_MESSAGE_TEXT));
    		String author = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_MESSAGE_AUTHOR));
    		String timestamp = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_MESSAGE_TIMESTAMP));
    		
    		MessagePOJO message = new MessagePOJO();
    		message.setMessageId(num);
    		message.setText(text);
    		message.setAuthor(author);
    		message.setTimestamp(timestamp);
    		messageList.add(message);
    	}
    	
    	db.close();
    	*/
    	return messageList;
    	
    }

    //TODO: Remove this if it's safe.
    public void onMessageClick(View view) {
    	Intent i = new Intent(this, ShowComments.class);
    	MessagePOJO message = new MessagePOJO();
    	message.setText("Message");
    	message.setAuthor("Joe Cruz");
    	message.setTimestamp("March 18, 2012");
    	i.putExtra("messagePOJO", message);
    	i.putExtra("user", uname);
    	startActivity(i);
    }
    
    // method called when a user clicks on a message layout
    public void onMessageClick(MessagePOJO message){
    	Intent i = new Intent(this, ShowComments.class);
    	i.putExtra("messagePOJO", message);
    	i.putExtra("user", uname);
    	startActivity(i);
    }
    
    // onClickFunction of postMessage button
    public void onPostMessage(View view){
    	Intent i = new Intent(this, PostMessage.class);
    	i.putExtra("user", uname);
		//i.putExtra("eventPOJO", event);
    	startActivity(i);
    }
	
}
