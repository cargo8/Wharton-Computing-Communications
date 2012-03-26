package edu.upenn.cis350;

import java.util.List;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/* This activity displays a form for the user to register a user name for the app.
 * In the future, this will contain more fields to create a more robust "contact"
 */
public class Register extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "FWyFNrvpkliSb7nBNugCNttN5HWpcbfaOWEutejH", "SZoWtHw28U44nJy8uKtV2oAQ8suuCZnFLklFSk46");
        setContentView(R.layout.register);
        
    }
    
    // Adds the new user to the database if it doesn't already exist
    public void newUser(View view) {
    	String uname = ((EditText)findViewById(R.id.loginUsername)).getText().toString();
		String pw = ((EditText)findViewById(R.id.loginPassword)).getText().toString();
		
		ParseQuery query = new ParseQuery("Users");
		query.whereEqualTo("username", uname);
    	List<ParseObject> userList;
		try {
			userList = query.find();
		} catch (ParseException e) {
			Toast.makeText(this, "Error registering account", Toast.LENGTH_SHORT);
			return;
		}

		if (userList.size() == 0) {
			ParseObject user = new ParseObject("Users");
			user.put("username", uname);
			user.put("pw", pw);
			user.save();
    		Toast.makeText(this, "User " + uname + " created.", Toast.LENGTH_SHORT).show();
    		Intent i = new Intent(this, WhartonComputingCommunicationsActivity.class);
    		startActivity(i);

		} else {
    		Toast.makeText(this, "This username is taken. Try again.", Toast.LENGTH_SHORT).show();
		}
	
    }
}
