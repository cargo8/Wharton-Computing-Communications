package edu.upenn.cis350;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;
import com.parse.SaveCallback;


/* This activity shows the events in a list form.
 * Events are separated by type - Emergency and Scheduled.
 * Clicking on an event goes to the ShowEvent view for that Event.
 */
public class Agenda extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agenda);
		Parse.initialize(this, "FWyFNrvpkliSb7nBNugCNttN5HWpcbfaOWEutejH", "SZoWtHw28U44nJy8uKtV2oAQ8suuCZnFLklFSk46");

		final ListView eventList = (ListView) findViewById(R.id.eventList);

		ParseQuery query = new ParseQuery("Event");
		query.orderByAscending("startDate");

		// Only show events with end date greater than now
		Long now = System.currentTimeMillis();
		query.whereGreaterThanOrEqualTo("endDate", now);

		query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);

		final ProgressDialog dialog = ProgressDialog.show(this, "", 
				"Loading. Please wait...", true);
		query.findInBackground(new FindCallback() {

			@Override
			@SuppressWarnings({ "unchecked", "rawtypes" })
			/* Needs to be type-unsafe to dynamically create section dividers */
			public void done(final List<ParseObject> events, ParseException e) {
				if (e == null) {
					List items = new ArrayList();					

					// The following is not ideal, but I guess only O(3n) = O(n)
					items.add(new ListItem("Emergency Events", true));
					for (ParseObject event : events) {
						if ("Emergency".equals(event.getString("type"))) {
							items.add(new ListItem(event, false));
						}
					}
					items.add(new ListItem("Scheduled Events", true));
					for (ParseObject event : events) {
						if ("Scheduled".equals(event.getString("type"))) {
							items.add(new ListItem(event, false));
						}
					}
					items.add(new ListItem("Recently Resolved Events", true));
					for (ParseObject event : events) {
						if ("Resolved".equals(event.getString("type"))) {
							items.add(new ListItem(event, false));
						}
					}

					eventList.setAdapter(new EventListAdapter(getApplicationContext(), items));
				}
				dialog.cancel();
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.agenda_menu, menu);
		return true;
	}
	
	/**
	 * Method that gets called when the menuitem is clicked
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.refreshAgenda){
			Intent i = new Intent(this, Agenda.class);
			finish();
			startActivity(i);
			return true;
		}
		return false;
	}
	
	@Override
	public void onResume() {
		onCreate(new Bundle());
	}

	/**
	 * Adapter for formatting the Events into ListView items
	 * 
	 * @author JMow
	 * 
	 */
	private class EventListAdapter extends ArrayAdapter<ListItem> {

		private List<ListItem> events;

		public EventListAdapter(Context context, List<ListItem> events) {
			super(context, 0, events);
			this.events = events;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final ListItem item = events.get(position);

			if (item != null) {
				if (item.isSection()) {
					/* This is a section header */
					String title = (String) item.getData();
					v = vi.inflate(R.layout.event_list_divider, null);

					v.setOnClickListener(null);
					v.setOnLongClickListener(null);
					v.setLongClickable(false);

					final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
					sectionView.setText(title);

				} else {
					/* This is a real list item */
					final ParseObject event = (ParseObject) item.getData();
					v = vi.inflate(R.layout.event_list_item, null);
					TextView temp = (TextView) v.findViewById(R.id.listEventTitle);
					if (temp != null) {
						temp.setText(event.getString("title"));
					}
					temp = (TextView) v.findViewById(R.id.listEventDate);
					if (temp != null) {
						SimpleDateFormat formatter = new SimpleDateFormat();
						Date date1 = new Date(event.getLong("startDate"));
						Date date2 = new Date(event.getLong("endDate"));
						temp.setText("Start: " + formatter.format(date1) + 
								", Est. Finish: " + formatter.format(date2));
					}
					temp = (TextView) v.findViewById(R.id.listEventDescription);
					String desc = event.getString("description");
					if (temp != null) {
						if (desc.length() == 0) {
							temp.setText("No Description");
						} else if (desc.length() > 140) {
							temp.setText(desc.substring(0, 140) + "...");
						} else {
							temp.setText(desc);
						}
					}
					temp = (TextView) v.findViewById(R.id.listEventSeverity);
					if (temp != null) {
						temp.setBackgroundColor(event.getInt("severity"));
					}
					v.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent i = new Intent(getApplicationContext(), ShowEvent.class);
							i.putExtra("eventKey", event.getObjectId());
							startActivity(i);
						}
					});
				}
			}
			return v;
		}
	}
}
