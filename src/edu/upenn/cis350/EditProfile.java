package edu.upenn.cis350;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;

/* This activity displays a form for the user to register a user name for the app.
 * In the future, this will contain more fields to create a more robust "contact"
 */
public class EditProfile extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "FWyFNrvpkliSb7nBNugCNttN5HWpcbfaOWEutejH", "SZoWtHw28U44nJy8uKtV2oAQ8suuCZnFLklFSk46");
        setContentView(R.layout.edit_profile);
        
        ParseUser user = ParseUser.getCurrentUser();
    	String fname = user.getString("fname");
    	String lname = user.getString("lname");
    	String email1 = user.getString("email1");
    	String email2 = user.getString("email2");
    	String phone1 = user.getString("phone1");
    	String phone2 = user.getString("phone2");
    	
    	EditText temp = ((EditText)findViewById(R.id.loginUsername));
    	temp.setText(user.getUsername());
		temp = ((EditText)findViewById(R.id.loginPassword));
		temp = ((EditText)findViewById(R.id.loginPassword2));
		temp = ((EditText)findViewById(R.id.registerFname));
		temp.setText(fname);
		temp = ((EditText)findViewById(R.id.registerLname));
		temp.setText(lname);
		temp = ((EditText)findViewById(R.id.registerEmail1));
		temp.setText(email1);
		temp = ((EditText)findViewById(R.id.registerEmail2));
		temp.setText(email2);
		temp = ((EditText)findViewById(R.id.registerPhone1));
		temp.setText(phone1);
		temp = ((EditText)findViewById(R.id.registerPhone2));
		temp.setText(phone2);
    }
    
    /**
     * Updates a user's profile in the application
     * @param view
     */
    public void updateUser(View view) {
    	String uname = ((EditText)findViewById(R.id.loginUsername)).getText().toString().toLowerCase();
		String pw = ((EditText)findViewById(R.id.loginPassword)).getText().toString();
		String pw2 = ((EditText)findViewById(R.id.loginPassword2)).getText().toString();
		String fname = ((EditText)findViewById(R.id.registerFname)).getText().toString();
		String lname = ((EditText)findViewById(R.id.registerLname)).getText().toString();
		String email1 = ((EditText)findViewById(R.id.registerEmail1)).getText().toString();
		String email2 = ((EditText)findViewById(R.id.registerEmail2)).getText().toString();
		String phone1 = ((EditText)findViewById(R.id.registerPhone1)).getText().toString();
		String phone2 = ((EditText)findViewById(R.id.registerPhone2)).getText().toString();
		
		ParseUser user = ParseUser.getCurrentUser();
    	user.setUsername(uname);
    	if (!"".equals(pw) || !"".equals(pw2)) {
    		if (!pw.equals(pw2)) {
    			Toast.makeText(this, "Passwords do not match. Try again", Toast.LENGTH_SHORT).show();
    			return;
    		} else {
            	user.setPassword(pw);
    		}
    	}
    	user.put("fname", fname);
    	user.put("lname", lname);
    	user.put("fullName", fname + " " + lname);
    	user.put("email1", email1);
    	user.put("email2", email2);
    	user.put("phone1", phone1);
    	user.put("phone2", phone2);

		user.saveEventually();
		finish();
    }
}
