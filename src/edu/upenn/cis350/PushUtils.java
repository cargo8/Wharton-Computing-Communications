package edu.upenn.cis350;

import java.util.List;

import android.content.Context;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

public abstract class PushUtils {
	
	private PushUtils() {}
	
	/**
	 * Adds an entry to the LazySubscription data store that this userId should be
	 * subscribed to this event the next time the user opens the app. Also pushes
	 * a notification to tell them they have been listed as a Primary Contact
	 * 
	 * @param context Context of activity where subscribe is called
	 * @param event The event to be subscribed to
	 * @param userId The userId to subscribe and notify
	 */
	public static boolean lazySubscribeContact(Context context, ParseObject event, String userId) {
		ParseObject lazySub = new ParseObject("LazySubscription");
		lazySub.put("userID", userId);
		lazySub.put("eventID", event.getObjectId());
		lazySub.saveEventually();
		ParsePush userPush = new ParsePush();
		userPush.setChannel("user_" + userId);
		userPush.setMessage(ParseUser.getCurrentUser().getString("fullName") + " has set you as a Primary Contact for"
				+ " the event \"" + event.getString("title") +"\". Login to view the event details.");
		// expire after 5 days
		userPush.setExpirationTimeInterval(432000);
		userPush.sendInBackground();
		return true;
	}
	
	/**
	 * Called when user logs in to check if any lazy subscriptions exist
	 * If so, the user is then subscribed to the subscriptions, and deletes
	 * them from the database
	 * 
	 * @param context Context of activity where user is active
	 */
	public static void lazySubscribeToEvents(final Context context) {
		ParseQuery query = new ParseQuery("LazySubscription");
		query.whereEqualTo("userID", ParseUser.getCurrentUser().getObjectId());
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> subscriptions, ParseException e) {
				if (e == null) {
					for (ParseObject sub : subscriptions) {
						PushService.subscribe(context, "push_" + sub.getString("eventID"), Login.class);
						sub.deleteEventually();
					}
				}
			}
			
		});
	}

	/**
	 * Creates a push notification for Posting a Comment to a Message
	 * 
	 * @param messageId The messageID that this comment is posted on
	 * @param comment The comment parse object.
	 */
	public static void createCommentPush(ParseObject message, ParseObject comment) {
		ParsePush pushMessage = new ParsePush();
		ParseUser user = ParseUser.getCurrentUser();
		pushMessage.setChannel("push_" + message.getObjectId());
		pushMessage.setMessage(user.getString("fullName") + " commented: " +
				"\"" + comment.getString("text") + "\" on the message \"" +
				message.getString("text") + "\"");
		// expire after 5 days
		pushMessage.setExpirationTimeInterval(432000);
		pushMessage.sendInBackground();
	}

	/**
	 * Creates a push notification for Posting a Message to an Event
	 * 
	 * @param eventId The eventID of event that this message is posted on
	 * @param message The message parse object.
	 */
	public static void createMessagePush(ParseObject event, ParseObject message) {
		ParsePush pushMessage = new ParsePush();
		ParseUser user = ParseUser.getCurrentUser();
		pushMessage.setChannel("push_" + event.getObjectId());
		pushMessage.setMessage(user.getString("fullName") + " posted: \"" + message.getString("text") + "\" on " +
				"the event \"" + event.getString("title") + "\"");
		// expire after 5 days
		pushMessage.setExpirationTimeInterval(432000);
		pushMessage.sendInBackground();
	}	
	
	/**
	 * Creates a push notification for Editing an Event
	 * 
	 * @param context Context for activity calling this method
	 * @param eventId Event ID for event that has been changed
	 * @param event Event ParseObject that is changed
	 */
	public static void createEventChangedPush(Context context, String eventId, ParseObject event) {
		PushService.subscribe(context, "push_" + eventId, Login.class);
		ParsePush pushMessage = new ParsePush();
		ParseUser user = ParseUser.getCurrentUser();
		pushMessage.setChannel("push_" + eventId);
		pushMessage.setMessage(user.getString("fullName") + " updated the event \"" + event.getString("title") + "\"");
		// expire after 5 days
		pushMessage.setExpirationTimeInterval(432000);
		pushMessage.sendInBackground();
	}

	/**
	 * Creates a push notification for Marking an Event as Resolved
	 * 
	 * @param eventId Event ID for event that is marked Resolved
	 * @param event Event that is marked Resolved
	 */
	public static void createEventResolvedPush(String eventId, ParseObject event) {
		ParsePush pushMessage = new ParsePush();
		ParseUser user = ParseUser.getCurrentUser();
		pushMessage.setChannel("push_" + eventId);
		pushMessage.setMessage(user.getString("fullName") + " marked \"" + event.getString("title") + "\" as Resolved.");
		// expire after 5 days
		pushMessage.setExpirationTimeInterval(432000);
		pushMessage.sendInBackground();
	}
}
