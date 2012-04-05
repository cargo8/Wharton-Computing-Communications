package edu.upenn.cis350;

import android.app.Activity;
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
		PushService.subscribe(this, "", Login.class);
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
		    Intent i = new Intent(this, Home.class);
		    startActivity(i);
		} else {
			setContentView(R.layout.main);
		}
	}

	/**
	 * Logs in the User
	 */
	public void login(View view) {
		
		//TODO(jmow): Normalize login and registration!
		
		String uname = ((EditText)findViewById(R.id.loginUsername)).getText().toString();
		String pw = ((EditText)findViewById(R.id.loginPassword)).getText().toString();

		final Toast successToast = Toast.makeText(this, "Login Successful.", Toast.LENGTH_SHORT);
		final Intent i = new Intent(this, Home.class);
		
		final Toast failureToast = Toast.makeText(this, "Login failed.", Toast.LENGTH_SHORT);
		
		ParseUser.logInInBackground(uname, pw, new LogInCallback() {
		    public void done(ParseUser user, ParseException e) {
		        if (user != null) {
		            // Hooray! The user is logged in.
		        	successToast.show();
		    		i.putExtra("userKey", user.getObjectId());
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
		startActivityForResult(i, ACTIVITY_Register);
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(this, Login.class);
		startActivity(i);
	}
}