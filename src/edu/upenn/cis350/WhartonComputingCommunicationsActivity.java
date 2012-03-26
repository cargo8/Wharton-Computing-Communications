package edu.upenn.cis350;

import java.util.List;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/* This activity shows a login screen to the user.
 * If the user does not have an account, they can create an account
 * by clicking on the register button
 */
public class WhartonComputingCommunicationsActivity extends Activity {
	
	// fields for changing activities
	public static final int ACTIVITY_Home = 0;
	public static final int ACTIVITY_CreateNewEvent = 1;
	private static final int ACTIVITY_ShowEvent = 2;
	private static final int ACTIVITY_Agenda = 3;
	public static final int ACTIVITY_ShowComments = 4;
	public static final int ACTIVITY_Register = 5;
	public static final int ACTIVITY_PostMessage = 6;

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "FWyFNrvpkliSb7nBNugCNttN5HWpcbfaOWEutejH", "SZoWtHw28U44nJy8uKtV2oAQ8suuCZnFLklFSk46");
        setContentView(R.layout.main);
    }
    
    // Queries the DB to decide if the user exists or not
    public void login(View view) {
    	ParseQuery query = new ParseQuery("Users");

		String uname = ((EditText)findViewById(R.id.loginUsername)).getText().toString();
		String pw = ((EditText)findViewById(R.id.loginPassword)).getText().toString();
		
		if (uname.equals("") || pw.equals("")) {
			Toast.makeText(this, "Login failed.", Toast.LENGTH_SHORT).show();
			return;
		}
		
    	query.whereEqualTo("username", uname);
    	List<ParseObject> userList;
		try {
			userList = query.find();
		} catch (ParseException e) {
			Toast.makeText(this, "Error logging in", Toast.LENGTH_SHORT);
			return;
		}
    	
    	
    	if (userList.size() > 0) {
    		Toast.makeText(this, "Login Successful.", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(this, Home.class);
			i.putExtra("user", uname);
			startActivityForResult(i, ACTIVITY_Home);
		} else {
			Toast.makeText(this, "Login failed.", Toast.LENGTH_SHORT).show();
		}
    		
    }
    
    // onClick function of register button
    public void clickRegister(View view) {
    	Intent i = new Intent(this, Register.class);
    	startActivityForResult(i, ACTIVITY_Register);
    }
    
    @Override
    public void onBackPressed() {
       Intent i = new Intent(this, WhartonComputingCommunicationsActivity.class);
       startActivity(i);
    }
}