package edu.upenn.cis350;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parse.FindCallback;
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
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/* This activity displays the comments related to a particular message.
 * Each message has its own comments related to it.
 */
public class ShowComments extends Activity {

	private String msgId;
	private ParseObject message;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showcomments);
		Parse.initialize(this, "FWyFNrvpkliSb7nBNugCNttN5HWpcbfaOWEutejH", "SZoWtHw28U44nJy8uKtV2oAQ8suuCZnFLklFSk46");

        Bundle extras = this.getIntent().getExtras();
        
        if (extras == null){
        	Toast.makeText(this, "Could not load event.", Toast.LENGTH_LONG);
        	return;
        } else {
        	msgId = extras.getString("messageID");
        	ParseQuery msgQuery = new ParseQuery("Message");
        	final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        	msgQuery.getInBackground(msgId, new GetCallback() {

				@Override
				public void done(ParseObject msg, ParseException e) {
					if (msg == null) {
						toast.setText("Error: " + e.getMessage());
						toast.show();
						return;
					} else {
						message = msg;
						LinearLayout layout = (LinearLayout) findViewById(R.id.commentMessagePane);
			            layout.setBackgroundColor(Color.GRAY);

			            TextView temp = (TextView)findViewById(R.id.messageText);
			            temp.setTextColor(Color.WHITE);
			            temp.setText(msg.getString("text"));
			            
			        	temp = (TextView) findViewById(R.id.messageAuthor);
			        	temp.setTextColor(Color.WHITE);
			        	//TODO(kuyumcu): change to ParseUser
			        	String author = msg.getString("author");
			        	temp.setText("Posted by " + author + " at ");
			        	
			        	temp = (TextView) findViewById(R.id.messageTimestamp);
			        	temp.setTextColor(Color.WHITE);
			        	SimpleDateFormat formatter = new SimpleDateFormat();
			        	temp.setText(formatter.format(new Date(msg.getLong("timestamp"))));
			        	
			            getParseComments(msg);
					}
				}
        		
        	});
        	
        }
    }

    // onClick function of postComment button
    /**
     * Post a new comment to this message
     * 
     * @param view
     */
    public void onPostComment(View view) {
    	ParseObject comment = new ParseObject("Comment");
    	
    	final EditText commentText = (EditText) findViewById(R.id.newCommentText);
    	if (commentText.getText().toString().equals("")) {
    		Toast.makeText(this, "Please enter a comment.", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	comment.put("text", commentText.getText().toString());
    	
    	//TODO: check if message is null, necessary?
    	comment.put("message", message);
    	comment.put("author", ParseUser.getCurrentUser());
    	comment.put("timestamp", System.currentTimeMillis());
    	
    	final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    	comment.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e != null) {
					toast.setText("Error: " + e.getMessage());
					toast.show();
					return;
				} else {
					toast.setText("Comment posted");
					toast.show();
			    	commentText.setText("");
			    	getParseComments(message);
				}
			}
    		
    	});
    }
    
    /**
     * Creates a UI frame to display comments
     * 
     * @param comment A ParseObject representing a comment to be displayed
     * @return LinearLayout representing the comment
     */
    public LinearLayout createCommentFrame(ParseObject comment) {
    	LinearLayout commentFrame = new LinearLayout(this);
    	commentFrame.setOrientation(1);
    	commentFrame.setPadding(1, 1, 1, 1);
    	
    	LinearLayout header = new LinearLayout(this);
    	header.setOrientation(0);
    	
    	TextView author = new TextView(this);
    	author.setText(comment.getString("author"));
    	author.setTypeface(Typeface.DEFAULT_BOLD);
    	
    	TextView timestamp = new TextView(this);
    	long time = comment.getLong("timestamp");
    	SimpleDateFormat formatter = new SimpleDateFormat();
    	timestamp.setText(" at " + formatter.format(new Date(time)));
    	
    	header.addView(author);
    	header.addView(timestamp);
    	
    	TextView commentText = new TextView(this);
    	commentText.setText(comment.getString("text"));
    	
    	commentFrame.addView(header);
    	commentFrame.addView(commentText);
    	
    	return commentFrame;
    }
    
    /**
     * Get list of comments for this message
     * 
     * @param message ParseObject representing the message to get comments for
     * @return List of ParseObjects representing comments
     */
    public void getParseComments(ParseObject message) {
    	ParseQuery commentQuery = new ParseQuery("Comment");
    	commentQuery.addAscendingOrder("timestamp");
    	commentQuery.whereEqualTo("message", message);
    	
    	final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    	commentQuery.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> comments, ParseException e) {
				if (e != null) {
					toast.setText("Error: " + e.getMessage());
					toast.show();
					return;
				} else {
					LinearLayout commentsPane = (LinearLayout) findViewById(R.id.commentsPane);
			        commentsPane.removeAllViews();
			        for (ParseObject c : comments) {
			        	LinearLayout commentFrame = createCommentFrame(c);
			        	commentsPane.addView(commentFrame);
			        }
				}
			}
    		
    	});
    }
    
}
