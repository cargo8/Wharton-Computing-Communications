package edu.upenn.cis350;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;

/* This activity is what the app will start with after logging in.
 * For now, it just shows a few buttons to create a new event
 * and to show the agenda.
 */
public class Home extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Parse.initialize(this, Settings.APPLICATION_ID, Settings.CLIENT_ID);
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {		    
			Intent i = new Intent(this, Login.class);
			finish();
			startActivity(i);
			return;
		} else if (!currentUser.getBoolean("emailVerified")) {
			Intent i = new Intent(this, Login.class);
			Toast.makeText(this, "Your registration has not been verified.", Toast.LENGTH_LONG).show();
			finish();
			startActivity(i);
			return;
		}

		String[] options = getResources().getStringArray(R.array.home_options_array);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, options));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String label = ((TextView) view).getText().toString();
				if ("Create New Event".equals(label)) {
					onCreateEventClick();
				} else if ("Show Agenda".equals(label)) {
					onShowAgenda();
				} else if ("Show Contacts".equals(label)) {
					onShowContacts();
				} else if ("Edit Profile".equals(label)) {
					onEditProfile();
				} else if ("View Notifications".equals(label)) {
					onViewNotifications();
				} else if ("Manage Groups and Systems".equals(label)) {
					onViewManageGroups();
				}
			}
		});

		PushUtils.lazySubscribeToEvents(this);
	}

	// onClick function for createEvent button
	public void onCreateEventClick(){
		Intent i = new Intent(this, CreateNewEvent.class);
		startActivity(i);
	}

	// onClick function for showAgenda button
	public void onShowAgenda() {
		Intent i = new Intent(this, Agenda.class);
		startActivity(i);
	}

	public void onShowContacts() {
		Intent i = new Intent(this, ContactList.class);
		startActivity(i);
	}

	public void onEditProfile() {
		Intent i = new Intent(this, EditProfile.class);
		startActivity(i);
	}

	public void onViewNotifications() {
		Intent i = new Intent(this, ShowNotifications.class);
		startActivity(i);
	}
	
	public void onViewManageGroups() {
		Intent i = new Intent(this, ManageGroups.class);
		startActivity(i);
	}

	/**
	 * Method that gets called when Menu is created
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home_menu, menu);
		return true;
	}

	/**
	 * Method that gets called when the menuitem is clicked
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.logout){
			new AlertDialog.Builder(this)
			.setTitle("Logout")
			.setMessage("Are you sure you want to logout?")
			.setNegativeButton(android.R.string.no, null)
			.setPositiveButton(android.R.string.yes, new OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					ParseUser.logOut();
					Intent i = new Intent(getApplicationContext(), Login.class);
					finish();
					startActivity(i);
				}
			}).create().show();
			return true;
		}
		return false;
	}
}
