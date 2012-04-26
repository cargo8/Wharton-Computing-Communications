package edu.upenn.cis350;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

/* This activity displays the comments related to a particular message.
 * Each message has its own comments related to it.
 */
public class ShowMessage extends Activity {

	private String msgId;
	private ParseObject message;
	private ProgressDialog dialog;
	private List<ListItem> items = new ArrayList<ListItem>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_message);
		Parse.initialize(this, Settings.APPLICATION_ID, Settings.CLIENT_ID);

		Bundle extras = this.getIntent().getExtras();

		if (extras == null){
			Toast.makeText(this, "Could not load event.", Toast.LENGTH_LONG);
			return;
		} else {
			msgId = extras.getString("messageID");
			ParseQuery msgQuery = new ParseQuery("Message");
			final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
			dialog = ProgressDialog.show(this, "", 
					"Loading. Please wait...", true);
			dialog.setCancelable(true);
			msgQuery.getInBackground(msgId, new GetCallback() {

				@Override
				public void done(ParseObject msg, ParseException e) {
					if (msg == null) {
						toast.setText("Error: " + e.getMessage());
						toast.show();
						return;
					} else {
						message = msg;
						items.add(new ListItem(message, ListItem.Type.MESSAGE));
						getComments(msg);
					}
				}

			});

		}
	}


	/**
	 * Post a new comment to this message
	 * 
	 * @param view
	 */
	public void onPostComment(View view) {
		final ParseObject comment = new ParseObject("Comment");

		final EditText commentText = (EditText) findViewById(R.id.newCommentText);
		if (commentText.getText().toString().equals("")) {
			Toast.makeText(this, "Please enter a comment.", Toast.LENGTH_SHORT).show();
			return;
		}
		message.increment("count");
		comment.put("text", commentText.getText().toString());
		comment.put("message", message);
		comment.put("author", ParseUser.getCurrentUser());
		comment.put("timestamp", System.currentTimeMillis());
		//comment.put("authorName", ParseUser.getCurrentUser().get("fullName"));


		final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

		comment.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e != null) {
					toast.setText("Error: " + e.getMessage());
					toast.show();
					return;
				} else {
					message.saveInBackground();
					toast.setText("Comment posted");
					toast.show();
					PushUtils.createCommentPush(message, comment);
					commentText.setText("");
					
					Context context = getApplicationContext();
					if (!PushService.getSubscriptions(context).contains("push_" + message.getObjectId())) {
						PushService.subscribe(context, "push_" + message.getObjectId(), Login.class);
					}
					clearItems();
					getComments(message);
					
				}
			}
		});
	}
	
	public void clearItems(){
		items = new ArrayList<ListItem>();
		items.add(new ListItem(message, ListItem.Type.MESSAGE));
	}

	/**
	 * Get list of comments for this message
	 * 
	 * @param message ParseObject representing the message to get comments for
	 * @return List of ParseObjects representing comments
	 */
	public void getComments(ParseObject message) {
		final ListView cmtList = (ListView) findViewById(R.id.commentsList);
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
					//LinearLayout commentsPane = (LinearLayout) findViewById(R.id.commentsPane);
					//commentsPane.removeAllViews();
					for (ParseObject c : comments) {
						items.add(new ListItem(c, ListItem.Type.COMMENT));
					}
					cmtList.setAdapter(new CommentAdapter(getApplicationContext(), items));
					dialog.cancel();
				}
			}

		});
	}

	/**
	 * Method that gets called when Menu is created
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.show_message_menu, menu);
		return true;
	}

	/**
	 * Method that gets called when the menuitem is clicked
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.refresh){
			clearItems();
			getComments(message);
			return true;
		} else if (id == R.id.messageSubscribe) {
			if (!PushService.getSubscriptions(this).contains("push_" + message.getObjectId())) {
				PushService.subscribe(this, "push_" + message.getObjectId(), Login.class);
				return true;
			}
			return false;
		} else if (id == R.id.messageUnsubscribe) {
			PushService.unsubscribe(this, "push_" + message.getObjectId());
			return true;
		}
		return false;
	}
	
	
	/**
	 * Adapter for formatting the ShowEvent view
	 * 
	 * @author closen
	 * 
	 */
	private class CommentAdapter extends ArrayAdapter<ListItem> {

		private List<ListItem> listItems;

		public CommentAdapter(Context context, List<ListItem> items) {
			super(context, 0, items);
			this.listItems = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final ListItem item = listItems.get(position);

			if (item != null) {
				if (item.getType().equals(ListItem.Type.MESSAGE)) {
					final ParseObject message = (ParseObject) item.getData();
					v = vi.inflate(R.layout.comment_header_list_item, null);

					LinearLayout pane = (LinearLayout) v.findViewById(R.id.commentMessagePane);
					pane.setBackgroundColor(Color.GRAY);
					TextView temp = (TextView) v.findViewById(R.id.messageText);
					if (temp != null) {
						temp.setTextColor(Color.WHITE);
						temp.setText(message.getString("text"));
					}
					
					temp = (TextView) v.findViewById(R.id.messageAuthor);
					if (temp != null) {
						temp.setText(message.getString("authorName"));
						temp.setTypeface(Typeface.DEFAULT_BOLD);
						temp.setTextColor(Color.WHITE);
					}
					
					temp = (TextView) v.findViewById(R.id.messageTimestamp);
					if (temp != null) {
						Long time = message.getLong("timestamp");
						final SimpleDateFormat formatter = new SimpleDateFormat("MMMM d 'at' h:mm a ");
						temp.setText(formatter.format(new Date(time)));
					}
					
					v.setOnClickListener(null);
					v.setOnLongClickListener(null);
					v.setLongClickable(false);
				} else if (item.getType().equals(ListItem.Type.COMMENT)) {
					final ParseObject message = (ParseObject) item.getData();
					v = vi.inflate(R.layout.comment_list_item, null);

					TextView temp = (TextView) v.findViewById(R.id.listCommentText);
					if (temp != null) {
						temp.setText(message.getString("text"));
					}
					
					temp = (TextView) v.findViewById(R.id.listCommentAuthor);
					if (temp != null) {
						//temp.setText(message.getString("authorName"));
						temp.setTypeface(Typeface.DEFAULT_BOLD);

					}
					
					temp = (TextView) v.findViewById(R.id.listCommentTimestamp);
					if (temp != null) {
						Long time = message.getLong("timestamp");
						final SimpleDateFormat formatter = new SimpleDateFormat("MMMM d 'at' h:mm a ");
						temp.setText(formatter.format(new Date(time)));
					}
				} 
			}
			return v;
		}
	}
}
