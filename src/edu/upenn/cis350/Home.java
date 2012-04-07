package edu.upenn.cis350;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;

/* This activity is what the app will start with after logging in.
 * For now, it just shows a few buttons to create a new event
 * and to show the agenda.
 */
public class Home extends ListActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);

	  String[] options = getResources().getStringArray(R.array.home_options_array);
	  setListAdapter(new ArrayAdapter<String>(this, R.layout.home_list_item, options));

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
	    	}
	      }
	    }
	  );
	  
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
    
    @Override
    public void onBackPressed() {
    	ParseUser.logOut();
    	Intent i = new Intent(this, Login.class);
    	startActivity(i);
    }    
}
