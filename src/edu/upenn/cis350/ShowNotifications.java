package edu.upenn.cis350;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ShowNotifications extends ListActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, Settings.APPLICATION_ID, Settings.CLIENT_ID);

		final ProgressDialog dialog = ProgressDialog.show(this, "", 
				"Loading. Please wait...", true);
		dialog.setCancelable(true);
		ParseUser user = ParseUser.getCurrentUser();

		ParseQuery query = new ParseQuery("Notification");
		query.whereEqualTo("user", user);
		query.addAscendingOrder("isRead");

		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> notifications, ParseException e) {
				if (e == null) {
					ArrayList<ListItem> newNotifications = new ArrayList<ListItem>();
					ArrayList<ListItem> readNotifications = new ArrayList<ListItem>();
					for (ParseObject n : notifications) {
						if (!n.getBoolean("isRead")) {
							newNotifications.add(new ListItem(n, false));
						} else {
							readNotifications.add(new ListItem(n, false));
						}
					}
					List<ListItem> list = new ArrayList<ListItem>();
					list.add(new ListItem("New Notifications", true));
					list.addAll(newNotifications);
					list.add(new ListItem("Read Notifications", true));
					list.addAll(readNotifications);

					setListAdapter(new NotificationAdapter(getApplicationContext(), list));

					ListView lv = getListView();
					lv.setTextFilterEnabled(true);

					lv.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							Toast.makeText(getApplicationContext(), ((TextView) view.findViewById(R.id.notificationTitle)).getText(),
									Toast.LENGTH_SHORT);
						}
					});
				} else {
					Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
					return;
				}
				dialog.cancel();					
			}
		});

	}

	private class NotificationAdapter extends ArrayAdapter<ListItem> {

		private List<ListItem> notifications;

		public NotificationAdapter(Context context, List<ListItem> notifications) {
			super(context, 0, notifications);
			this.notifications = notifications;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final ListItem item = notifications.get(position);

			if (item != null) {
				if (item.isSection()) {
					/* This is a section header */
					String title = (String) item.getData();
					v = vi.inflate(R.layout.list_divider, null);

					v.setOnClickListener(null);
					v.setOnLongClickListener(null);
					v.setLongClickable(false);

					final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
					sectionView.setText(title);

				} else {
					/* This is a real list item */
					final ParseObject notification = (ParseObject) item.getData();
					v = vi.inflate(R.layout.notification_list_item, null);
					TextView temp = (TextView) v.findViewById(R.id.notificationTitle);
					temp.setText(notification.getString("title"));
					temp = (TextView) v.findViewById(R.id.notificationText);
					temp.setText(notification.getString("text"));
				}
			}
			return v;
		}
	}

}
