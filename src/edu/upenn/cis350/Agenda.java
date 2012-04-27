package edu.upenn.cis350;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQuery.CachePolicy;

/* This activity shows the events in a list form.
 * Events are separated by type - Emergency and Scheduled.
 * Clicking on an event goes to the ShowEvent view for that Event.
 */
public class Agenda extends Activity {

	private enum Filter {
		NEW, ALL, ONE_WEEK_OLD;
	}
	
	private Filter filter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agenda);
		Parse.initialize(this, Settings.APPLICATION_ID, Settings.CLIENT_ID);

		final ListView eventList = (ListView) findViewById(R.id.eventList);

		Bundle extras = getIntent().getExtras();
		ParseQuery query;
		
		if (extras != null) {
			filter = (Filter) extras.get("filter");
		}
		if (filter == null) {
			filter = Filter.NEW;
		}
		if (filter.equals(Filter.NEW)) { 
			query = getQuery(Filter.NEW);
		} else if (filter.equals(Filter.ALL)) {
			query = getQuery(Filter.ALL);
		} else if (filter.equals(Filter.ONE_WEEK_OLD)) {
			query = getQuery(Filter.ONE_WEEK_OLD);
		} else {
			// TODO: Other filters
			query = getQuery(Filter.NEW);
		}


		final ProgressDialog dialog = ProgressDialog.show(this, "", 
				"Loading. Please wait...", true);
		dialog.setCancelable(true);
		query.findInBackground(new FindCallback() {

			@Override
			/* Needs to be type-unsafe to dynamically create section dividers */
			public void done(final List<ParseObject> events, ParseException e) {
				if (e == null) {
					List<ListItem> items = new ArrayList<ListItem>();					

					// The following is not ideal, but I guess only O(3n) = O(n)
					items.add(new ListItem("Emergency Events", ListItem.Type.HEADER));
					for (ParseObject event : events) {
						if ("Emergency".equals(event.getString("type"))) {
							items.add(new ListItem(event, ListItem.Type.INFO));
						}
					}
					items.add(new ListItem("Scheduled Events", ListItem.Type.HEADER));
					for (ParseObject event : events) {
						if ("Scheduled".equals(event.getString("type"))) {
							items.add(new ListItem(event, ListItem.Type.INFO));
						}
					}
					items.add(new ListItem("Resolved Events", ListItem.Type.HEADER));
					for (ParseObject event : events) {
						if ("Resolved".equals(event.getString("type"))) {
							items.add(new ListItem(event, ListItem.Type.INFO));
						}
					}

					eventList.setAdapter(new EventListAdapter(getApplicationContext(), items));
				}
				dialog.cancel();
			}
		});
	}

	private ParseQuery getQuery(Filter filter) {
		ParseQuery query = new ParseQuery("Event");
		query.orderByAscending("startDate");
		Long now = System.currentTimeMillis();
				
		if (Filter.NEW.equals(filter)) {
			// Only show events with end date greater than now
			query.whereGreaterThanOrEqualTo("endDate", now);

		} else if (Filter.ONE_WEEK_OLD.equals(filter)) {
			// Only show events with start date greater than ONE WEEK AGO
			query.whereGreaterThanOrEqualTo("startDate", now-604800000);

		} else if (Filter.ALL.equals(filter)) {
			// no-op AKA all events
			query.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
			query.whereExists("startDate");
		}
		return query;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		if (filter.equals(Filter.NEW)) {
			inflater.inflate(R.menu.agenda_menu_new, menu);
		} else if (filter.equals(Filter.ONE_WEEK_OLD)) {
			inflater.inflate(R.menu.agenda_menu_one_week_old, menu);
		} else if (filter.equals(Filter.ALL)) {
			inflater.inflate(R.menu.agenda_menu_all, menu);
		}
		return true;
	}

	/**
	 * Method that gets called when the menuitem is clicked
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.refreshAgenda){
			Intent i = new Intent(this, Agenda.class);
			if (filter == null) {
				filter = Filter.NEW;
			}
			i.putExtra("filter", filter);
			finish();
			startActivity(i);
			return true;
		} else if (id == R.id.showOneWeekOldEvents) {
			Intent i = new Intent(this, Agenda.class);
			i.putExtra("filter", Filter.ONE_WEEK_OLD);
			finish();
			startActivity(i);
			return true;
		} else if (id == R.id.showUpcomingEvents) {
			Intent i = new Intent(this, Agenda.class);
			i.putExtra("filter", Filter.NEW);
			finish();
			startActivity(i);
			return true;
		} else if (id == R.id.showAllEvents) {
			Intent i = new Intent(this, Agenda.class);
			i.putExtra("filter", Filter.ALL);
			finish();
			startActivity(i);
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
				if (item.getType().equals(ListItem.Type.HEADER)) {
					/* This is a section header */
					String title = (String) item.getData();
					v = vi.inflate(R.layout.list_divider, null);

					v.setOnClickListener(null);
					v.setOnLongClickListener(null);
					v.setLongClickable(false);

					final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
					sectionView.setText(title);

				} else if (item.getType().equals(ListItem.Type.INFO)){
					/* This is a real list item */
					final ParseObject event = (ParseObject) item.getData();
					v = vi.inflate(R.layout.event_list_item, null);
					TextView temp = (TextView) v.findViewById(R.id.listEventTitle);
					if (temp != null) {
						temp.setText(event.getString("title"));
					}
					temp = (TextView) v.findViewById(R.id.listEventDate);
					if (temp != null) {
						SimpleDateFormat formatter = new SimpleDateFormat("h:mm a 'on' MMMM d yyyy");
						Date date1 = new Date(event.getLong("startDate"));
						Date date2 = new Date(event.getLong("endDate"));
						temp.setText("Start: " + formatter.format(date1) + 
								"\nEst. Finish: " + formatter.format(date2));
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
					temp = (TextView) v.findViewById(R.id.listEventDate);
					if (temp != null) {
						SimpleDateFormat formatter = new SimpleDateFormat("h:mm a  'on' MMMM d, yyyy");
						Date date1 = new Date(event.getLong("startDate"));
						Date date2 = new Date(event.getLong("endDate"));
						temp.setText("Start: " + formatter.format(date1) + "\n" + 
								"Est. Finish: " + formatter.format(date2));
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
