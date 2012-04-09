package edu.upenn.cis350;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowContact extends Activity {

	String contactId;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_contact);
		Parse.initialize(this, "FWyFNrvpkliSb7nBNugCNttN5HWpcbfaOWEutejH", "SZoWtHw28U44nJy8uKtV2oAQ8suuCZnFLklFSk46");

		Bundle extras = this.getIntent().getExtras();

		if (extras == null){
			Toast.makeText(this, "Could not load contact.", Toast.LENGTH_LONG);
			return;
		} else {
			contactId = extras.getString("contactID");
			ParseQuery query = new ParseQuery("_User");
			query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);

			query.getInBackground(contactId, new GetCallback() {

				@SuppressWarnings({ "unchecked", "rawtypes" })
				// Type unsafe in order to support ListView
				@Override
				public void done(ParseObject contact, ParseException e) {
					if (e == null) {
						TextView temp = (TextView) findViewById(R.id.contactHeader);
						String tempString = contact.getString("fullName");
						temp.setText(tempString);

						ListView listView = (ListView) findViewById(R.id.phoneList);
						ArrayList items = new ArrayList();
						items.add(new ListItem("Phone", true));
						
						//TODO reverse the if statement
						String info = "".equals(contact.getString("phone1")) ? contact.getString("phone1") : "None";
						items.add(new ListItem("Primary: " + info, false));
						info = "".equals(contact.getString("phone2")) ? contact.getString("phone2") : "None";
						items.add(new ListItem("Secondary: " + info, false));
						items.add(new ListItem("Email", true));
						info = "".equals(contact.getString("email1")) ? contact.getString("email1") : "None";
						items.add(new ListItem("Primary: " + info, false));
						info = "".equals(contact.getString("email2")) ? contact.getString("email2") : "None";
						items.add(new ListItem("Secondary: " + info, false));
						listView.setAdapter(new ContactAdapter(getApplicationContext(), items));

//						temp = (TextView) findViewById(R.id.contactPhone1);
//						tempString = contact.getString("phone1");
//						if ("".equals(tempString)) tempString = "None";
//						temp.setText(tempString);
//						Linkify.addLinks(temp, Linkify.PHONE_NUMBERS);
//						temp.setLinkTextColor(temp.getTextColors().getDefaultColor());

					} else {
						Toast.makeText(getApplicationContext(), "Could not load contact.", Toast.LENGTH_LONG);
					}

				}

			});
		}
	}
	
	private class ContactAdapter extends ArrayAdapter<ListItem> {
		
		private List<ListItem> contacts;
		
		public ContactAdapter(Context context, List<ListItem> contacts) {
			super(context, 0, contacts);
			this.contacts = contacts;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final ListItem item = contacts.get(position);

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
					final String contactData = (String) item.getData();
					v = vi.inflate(R.layout.contact_list_item, null);
					TextView temp = (TextView) v;
					temp.setText(contactData);
					Linkify.addLinks(temp, Linkify.ALL);
				}
			}
			return v;
		}
	}

}
