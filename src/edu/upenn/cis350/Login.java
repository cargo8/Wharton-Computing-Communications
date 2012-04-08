package edu.upenn.cis350;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.PushService;

/* This activity shows a login screen to the user.
 * If the user does not have an account, they can create an account
 * by clicking on the register button
 */
public class Login extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "FWyFNrvpkliSb7nBNugCNttN5HWpcbfaOWEutejH", "SZoWtHw28U44nJy8uKtV2oAQ8suuCZnFLklFSk46");
		//TODO: Remove when user DB is cleared - has been moved to Registration
		PushService.subscribe(this, "", Login.class);
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			
			//TODO: Remove when user DB is cleared - has been moved to Registration
			PushService.subscribe(this, "user_" + currentUser.getObjectId(), Login.class);
		    
			Intent i = new Intent(this, Home.class);
			Toast.makeText(this, "Login Successful.", Toast.LENGTH_SHORT).show();
		    finish();
		    startActivity(i);
		} else {
			setContentView(R.layout.login);
		}
	}

	/**
	 * Logs in the User
	 */
	public void login(View view) {
				
		String uname = ((EditText)findViewById(R.id.loginUsername)).getText().toString().toLowerCase();
		String pw = ((EditText)findViewById(R.id.loginPassword)).getText().toString();

		final Toast successToast = Toast.makeText(this, "Login Successful.", Toast.LENGTH_SHORT);
		final Intent i = new Intent(this, Home.class);
		
		final Toast failureToast = Toast.makeText(this, "Login failed.", Toast.LENGTH_SHORT);
		
		ParseUser.logInInBackground(uname, pw, new LogInCallback() {
		    public void done(ParseUser user, ParseException e) {
		        if (user != null) {
		            // Hooray! The user is logged in.
		        	successToast.show();
		    		finish();
		    		startActivity(i);
		        } else {
		            // Signup failed. Look at the ParseException to see what happened.
		        	failureToast.show();
		        }
		    }
		});
		
	}

	// onClick function of register button
	public void clickRegister(View view) {
		Intent i = new Intent(this, Register.class);
		startActivity(i);
	}

	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setTitle("Exit Application")
	        .setMessage("Are you sure you want to exit?")
	        .setNegativeButton(android.R.string.no, null)
	        .setPositiveButton(android.R.string.yes, new OnClickListener() {

	            public void onClick(DialogInterface arg0, int arg1) {
	                Login.super.finish();
	            }
	        }).create().show();
	}
}