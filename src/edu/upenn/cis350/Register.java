package edu.upenn.cis350;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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
    
    /**
     * Registers a new user in the application
     * @param view
     */
    public void newUser(View view) {
    	String uname = ((EditText)findViewById(R.id.loginUsername)).getText().toString();
		String pw = ((EditText)findViewById(R.id.loginPassword)).getText().toString();
    	
		ParseUser user = new ParseUser();
    	user.setUsername(uname);
    	user.setPassword(pw);

    	

    	final Toast successToast = Toast.makeText(this, "User " + uname + " created.", Toast.LENGTH_SHORT);
		final Intent i = new Intent(this, WhartonComputingCommunicationsActivity.class);
		
		final Toast failToast = Toast.makeText(this, "Could not create user. Try again.", Toast.LENGTH_SHORT);
		
    	user.signUpInBackground(new SignUpCallback() {
    	    public void done(ParseException e) {
    	        if (e == null) {
					successToast.show();
		    		startActivity(i);
    	    	} else {
    	            // Sign up didn't succeed. Look at the ParseException
    	            // to figure out what went wrong
					failToast.show();
					return;
    	        }
    	    }
    	});	
    }
}
