package edu.upenn.cis350;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
	private boolean subscribed;

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

			updateSubscriptionStatus(msgId);

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

						final TextView authorView = (TextView) findViewById(R.id.messageAuthor);
						authorView.setTextColor(Color.WHITE);
						msg.getParseUser("author").fetchIfNeededInBackground(new GetCallback(){

							@Override
							public void done(ParseObject arg0, ParseException arg1) {
								ParseUser user = (ParseUser)arg0;
								String author = user.getString("fullName");
								authorView.setText(author + " - ");
							}

						});

						temp = (TextView) findViewById(R.id.messageTimestamp);
						temp.setTextColor(Color.WHITE);
						SimpleDateFormat formatter = new SimpleDateFormat("MMMM d 'at' h:mm a ");
						temp.setText(formatter.format(new Date(msg.getLong("timestamp"))));

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
						PushService.subscribe(context, "push_" + message.getObjectId(), ShowNotifications.class);
						ParseObject subscription = new ParseObject("Subscription");
						subscription.put("userId", ParseUser.getCurrentUser().getObjectId());
						subscription.put("subscriptionId", message.getObjectId());
						subscription.saveEventually();
					}
					getComments(message);
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
		commentFrame.setPadding(15, 15, 15, 15);

		LinearLayout header = new LinearLayout(this);
		header.setOrientation(0);

		final TextView author = new TextView(this);
		comment.getParseUser("author").fetchIfNeededInBackground(new GetCallback(){

			@Override
			public void done(ParseObject arg0, ParseException arg1) {
				// TODO Auto-generated method stub
				ParseUser user = (ParseUser)arg0;
				author.setText(user.getString("fullName") + " ");
				author.setTypeface(Typeface.DEFAULT_BOLD);
			}

		});

		TextView timestamp = new TextView(this);
		long time = comment.getLong("timestamp");
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM d 'at' h:mm a ");
		timestamp.setText(formatter.format(new Date(time)));

		TextView commentText = new TextView(this);
		commentText.setText(comment.getString("text"));

		header.addView(author);
		header.addView(commentText);
		//		header.addView(timestamp);

		commentFrame.addView(header);
		commentFrame.addView(timestamp);

		return commentFrame;
	}

	/**
	 * Get list of comments for this message
	 * 
	 * @param message ParseObject representing the message to get comments for
	 * @return List of ParseObjects representing comments
	 */
	public void getComments(ParseObject message) {
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
					dialog.cancel();
				}
			}

		});
	}

	/**
	 * Update subscription status of this user, on this message
	 * 
	 * @param messageId The message ID of the event being viewed
	 */
	private void updateSubscriptionStatus(final String msgId) {
		ParseQuery subscription = new ParseQuery("Subscription");
		subscription.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
		subscription.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> subscriptions, ParseException e) {
				if (e == null) {
					for (ParseObject obj : subscriptions) {
						if (msgId.equals(obj.getString("subscriptionId"))) {
							subscribed = true;
						}
					}
				}
			}

		});
	}

	/**
	 * Method that gets called when Menu is created
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		if (subscribed) {
			inflater.inflate(R.menu.show_message_menu_subscribed, menu);
		} else {
			inflater.inflate(R.menu.show_message_menu_unsubscribed, menu);
		}
		return true;
	}

	/**
	 * Method that gets called when the menuitem is clicked
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.refresh){
			getComments(message);
			return true;
		} else if (id == R.id.messageSubscribe) {
			if (!PushService.getSubscriptions(this).contains("push_" + message.getObjectId())) {
				PushService.subscribe(this, "push_" + message.getObjectId(), ShowNotifications.class);
				ParseObject subscription = new ParseObject("Subscription");
				subscription.put("userId", ParseUser.getCurrentUser().getObjectId());
				subscription.put("subscriptionId", message.getObjectId());
				subscription.saveEventually();
			}
			Intent i = new Intent(this, ShowMessage.class);
			i.putExtra("messageID", message.getObjectId());
			Toast.makeText(this, "Subscribed to message", Toast.LENGTH_SHORT).show();
			finish();
			startActivity(i);
			return true;
		} else if (id == R.id.messageUnsubscribe) {
			PushService.unsubscribe(this, "push_" + message.getObjectId());
			ParseQuery subscription = new ParseQuery("Subscription");
			subscription.whereEqualTo("subscriptionId", message.getObjectId());
			subscription.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
			subscription.findInBackground(new FindCallback() {

				@Override
				public void done(List<ParseObject> subscriptions,
						ParseException e2) {
					if (e2 == null) {
						for (ParseObject obj : subscriptions) {
							obj.deleteEventually();
						}
					}
				}

			});
			Intent i = new Intent(this, ShowMessage.class);
			i.putExtra("messageID", message.getObjectId());
			Toast.makeText(this, "Unsubscribed from event", Toast.LENGTH_SHORT).show();
			finish();
			startActivity(i);
			return true;
		}
		return false;
	}
}
