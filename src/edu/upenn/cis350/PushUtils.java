package edu.upenn.cis350;

import android.content.Context;

import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;

public abstract class PushUtils {
	
	private PushUtils() {}
	
	/**
	 * Adds an entry to the LazySubscription data store that this userId should be
	 * subscribed to this event the next time the user opens the app. Also pushes
	 * a notification to tell them they have been listed as a Primary Contact
	 * 
	 * @param event The event to be subscribed to
	 * @param userId The userId to subscribe and notify
	 */
	public static boolean lazySubscribe(Context context, ParseObject event, String userId) {
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
}
