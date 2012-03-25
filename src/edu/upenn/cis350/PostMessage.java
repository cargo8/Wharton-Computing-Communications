package edu.upenn.cis350;

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
	
	private String uname;
	private EventPOJO event;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.postmessage);
		Bundle extras = this.getIntent().getExtras();
		if(extras != null){
			uname = (String)extras.get("user");
			event = (EventPOJO)extras.get("eventPOJO");
		}
	}
	 
	// onClick function for Post button
	public void onPostClick(View view){
		TextView tv = (TextView)findViewById(R.id.messageBox);
		MessagePOJO msg = new MessagePOJO();
		msg.setAuthor(uname);
		msg.setText(tv.getText().toString());
		msg.setTimestamp(System.currentTimeMillis() + "");
		event.addToMessages(msg);
		msg = insertMessage(msg);
		Intent i = new Intent(this, ShowEvent.class);
		i.putExtra("eventPOJO", event);
		i.putExtra("message", msg);
		i.putExtra("user", uname);
		startActivity(i);
	}
	
	// inserts the message into the SQLite DB.
	// TODO: Change to MySQL DB
	public MessagePOJO insertMessage(MessagePOJO message) {
    	// First we have to open our DbHelper class by creating a new object of that
    	AndroidOpenDbHelper androidOpenDbHelperObj = new AndroidOpenDbHelper(this);

    	// Then we need to get a writable SQLite database, because we are going to insert some values
    	// SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
    	SQLiteDatabase sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();
    	// Try to create database for comments if not already there
    	androidOpenDbHelperObj.createMessagesTable(sqliteDatabase);

    	// ContentValues class is used to store a set of values that the ContentResolver can process.
    	ContentValues contentValues = new ContentValues();

    	// Get values from the POJO class and passing them to the ContentValues class
    	contentValues.put(AndroidOpenDbHelper.COLUMN_NAME_MESSAGE_TEXT, message.getText());
    	contentValues.put(AndroidOpenDbHelper.COLUMN_NAME_MESSAGE_AUTHOR, message.getAuthor());
    	contentValues.put(AndroidOpenDbHelper.COLUMN_NAME_MESSAGE_TIMESTAMP, message.getTimestamp());
    	contentValues.put(AndroidOpenDbHelper.COLUMN_NAME_MESSAGE_EVENT, event.getEventID());

    	// Now we can insert the data in to relevant table
    	// I am going pass the id value, which is going to change because of our insert method, to a long variable to show in Toast
    	long insertedId = sqliteDatabase.insert(AndroidOpenDbHelper.TABLE_NAME_MESSAGES, null, contentValues);
    	Cursor cursor = sqliteDatabase.query(androidOpenDbHelperObj.TABLE_NAME_MESSAGES,
    			null, androidOpenDbHelperObj.COLUMN_NAME_MESSAGE_ID + " = " + insertedId, null,
    			null, null, null);
    	cursor.moveToFirst();
    	int num = cursor.getInt(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_MESSAGE_ID));
    	String text = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_MESSAGE_TEXT));
    	String author = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_MESSAGE_AUTHOR));
    	String timestamp = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COLUMN_NAME_MESSAGE_TIMESTAMP));

    	MessagePOJO msg = new MessagePOJO();
    	msg.setMessageId(num);
    	msg.setText(text);
    	msg.setAuthor(author);
    	msg.setTimestamp(timestamp);

    	sqliteDatabase.close();

    	// I am not going to do the retrieve part in this post. So this is just a notification for satisfaction 
    	Toast.makeText(this, "Message posted.", Toast.LENGTH_SHORT).show();
    	return msg;
    }

}
