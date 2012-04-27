package edu.upenn.cis350;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
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

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.upenn.cis350.ListItem.Type;

public class ContactList extends ListActivity {

	ListItem.Type filterType = null;
	String filter = null;
	
	/**
	 *  Called when the activity is first created.
	 *  Makes query to fetch and populate contact list
	 *  
	 **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Parse.initialize(this, Settings.APPLICATION_ID, Settings.CLIENT_ID);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			filterType = (Type) extras.get("filter");
			filter = extras.getString("groupName");
		}
		
        final ProgressDialog dialog = ProgressDialog.show(this, "", 
				"Loading. Please wait...", true);
		dialog.setCancelable(true);
		
        ParseQuery query = new ParseQuery("_User");
        if (filterType != null && filter != null) {
        	if (ListItem.Type.GROUP.equals(filterType)) {
            	query.whereContains("groups2", filter);
        	} else if (ListItem.Type.SYSTEM.equals(filterType)) {
            	query.whereContains("systems2", filter);
        	}
        }
        query.orderByAscending("lname");
            
    	query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
    	query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> contactList, ParseException e) {
				if (e == null) {
					List<String> names = new ArrayList<String>();
					final HashMap<String, String> identifiers = new HashMap<String, String>();
					for (ParseObject c : contactList) {
						String formattedName = c.getString("lname") + ", " + c.getString("fname");
						names.add(formattedName);
						identifiers.put(formattedName, c.getObjectId());
					}
			        setListAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, names));

			        ListView lv = getListView();
			        lv.setTextFilterEnabled(true);
			        
			        lv.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							Intent i = new Intent(getApplicationContext(), ShowContact.class);
							i.putExtra("contactID", identifiers.get(((TextView) view).getText()));
							startActivity(i);
						}
			          });
				} else {
					return;
				}
				dialog.cancel();
			}
    		
    	});
    }
    
	/**
	 * Creates menu on menu button press
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.show_contact_menu, menu);
		return true;
	}

	/**
	 * Method that gets called when the menuitem is clicked
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.refresh) {
			Intent i = new Intent(this, ContactList.class);
			i.putExtra("filter", filterType);
			i.putExtra("groupName", filter);
			finish();
			startActivity(i);
			return true;
		}
		return false;
	}
}
