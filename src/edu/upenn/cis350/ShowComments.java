package edu.upenn.cis350;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShowComments extends Activity {

	//TODO(kuyumcu)
//	MessagePOJO msg;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showcomments);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null){
        	// TODO(kuyumcu): Swap in Message data when implemented
        	/* Message Object Start */
//        	MessagePOJO msg = (MessagePOJO)extras.get("messagePOJO");
//        	TextView temp = (TextView)findViewById(R.id.commentText);
//        	temp.setText(msg.getText());
        	
        	/* Dummy Code */
        	LinearLayout layout = (LinearLayout) findViewById(R.id.commentMessagePane);
            layout.setBackgroundColor(Color.GRAY);

            TextView temp = (TextView)findViewById(R.id.messageText);
            temp.setTextColor(Color.WHITE);
            temp.setText((String)extras.get("message"));
            
        	temp = (TextView) findViewById(R.id.messageTimestamp);
        	temp.setTextColor(Color.WHITE);
        	
        	temp = (TextView) findViewById(R.id.messageAuthor);
        	temp.setTextColor(Color.WHITE);
        	temp.setText("Joe Cruz");
        	
        	SimpleDateFormat formatter = new SimpleDateFormat();
        	temp.setText(formatter.format(new Date(System.currentTimeMillis()-86400000)));
        }
        
        List<CommentPOJO> comments = new ArrayList<CommentPOJO>();
        for (CommentPOJO c : comments) {
        	LinearLayout commentFrame = new LinearLayout(this);
        	commentFrame.setOrientation(1);
        	commentFrame.setPadding(1, 1, 1, 1);
        	
        	LinearLayout header = new LinearLayout(this);
        	header.setOrientation(0);
        	
        	TextView author = new TextView(this);
        	author.setText(c.getAuthor());
        	author.setTypeface(Typeface.DEFAULT_BOLD);
        	
        	TextView timestamp = new TextView(this);
        	timestamp.setText(" at " + c.getTimestamp());
        	
        	header.addView(author);
        	header.addView(timestamp);
        	
        	TextView commentText = new TextView(this);
        	commentText.setText(c.getText());
        	
        	commentFrame.addView(header);
        	commentFrame.addView(commentText);
        }
    }

    public void onPostComment(View view) {
    	CommentPOJO comment = new CommentPOJO();
    	Intent intent = new Intent(this, ShowComments.class);
    	
    	EditText commentText = (EditText) findViewById(R.id.newCommentText);
    	if (commentText.getText().toString() == "") {
    		Toast.makeText(this, "Please enter a comment.", Toast.LENGTH_SHORT);
    		return;
    	}
    	comment.setText(commentText.getText().toString());
    	
    	//TODO(jmow): Save author name when logins are enabled
    	comment.setAuthor("Joe Cruz");
    	comment.setTimestamp(System.currentTimeMillis() + "");
    	
    	insertComment(comment);
    	view.invalidate();
    }
    
    public void insertComment(CommentPOJO comment) {
    	// First we have to open our DbHelper class by creating a new object of that
    			AndroidOpenDbHelper androidOpenDbHelperObj = new AndroidOpenDbHelper(this);

    			// Then we need to get a writable SQLite database, because we are going to insert some values
    			// SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
    			SQLiteDatabase sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();

    			// ContentValues class is used to store a set of values that the ContentResolver can process.
    			ContentValues contentValues = new ContentValues();

    			// Get values from the POJO class and passing them to the ContentValues class
    			contentValues.put(AndroidOpenDbHelper.COLUMN_NAME_COMMENT_TEXT, comment.getText());
    			contentValues.put(AndroidOpenDbHelper.COLUMN_NAME_COMMENT_AUTHOR, comment.getAuthor());
    			contentValues.put(AndroidOpenDbHelper.COLUMN_NAME_COMMENT_TIMESTAMP, comment.getTimestamp());


    			// Now we can insert the data in to relevant table
    			// I am going pass the id value, which is going to change because of our insert method, to a long variable to show in Toast
    			long affectedColumnId = sqliteDatabase.insert(AndroidOpenDbHelper.TABLE_NAME_COMMENTS, null, contentValues);

    			// It is a good practice to close the database connections after you have done with it
    			sqliteDatabase.close();

    			// I am not going to do the retrieve part in this post. So this is just a notification for satisfaction 
    			Toast.makeText(this, "Comment Posted. DB Column ID :" + affectedColumnId, Toast.LENGTH_SHORT).show();

    }
    
}
