package edu.upenn.cis350;

import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.PushService;
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
    	String uname = ((EditText)findViewById(R.id.loginUsername)).getText().toString().toLowerCase();
		String pw = ((EditText)findViewById(R.id.loginPassword)).getText().toString();
		String pw2 = ((EditText)findViewById(R.id.loginPassword2)).getText().toString();
		String fname = ((EditText)findViewById(R.id.registerFname)).getText().toString();
		String lname = ((EditText)findViewById(R.id.registerLname)).getText().toString();
		String email1 = ((EditText)findViewById(R.id.registerEmail1)).getText().toString();
		String email2 = ((EditText)findViewById(R.id.registerEmail2)).getText().toString();
		String phone1 = ((EditText)findViewById(R.id.registerPhone1)).getText().toString();
		String phone2 = ((EditText)findViewById(R.id.registerPhone2)).getText().toString();
    	
		if (!pw.equals(pw2)) {
			Toast.makeText(this, "Passwords do not match. Try again", Toast.LENGTH_SHORT).show();
			return;
		}
		
		final ParseUser user = new ParseUser();
    	user.setUsername(uname);
    	user.setPassword(pw);
    	user.put("fname", fname);
    	user.put("lname", lname);
    	user.put("fullName", fname + " " + lname);
    	user.put("email1", email1);
    	user.put("email2", email2);
    	user.put("phone1", phone1);
    	user.put("phone2", phone2);

    	

    	final Toast successToast = Toast.makeText(this, "User " + uname + " created.", Toast.LENGTH_SHORT);
		
		final Toast failToast = Toast.makeText(this, "Could not create user. Try again.", Toast.LENGTH_SHORT);
		
    	user.signUpInBackground(new SignUpCallback() {
    	    public void done(ParseException e) {
    	        if (e == null) {
    	        	Context context = getApplicationContext();
    	        	Set<String> mySub = PushService.getSubscriptions(context);
					if (!mySub.contains("push_" + user.getObjectId())) {
	    				PushService.subscribe(context, "user_" + user.getObjectId(), Login.class);
					}
					if (!mySub.contains("")) {
	    	    		PushService.subscribe(context, "", Login.class);
					}
					successToast.show();
					finish();
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
