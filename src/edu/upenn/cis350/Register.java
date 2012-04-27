package edu.upenn.cis350;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SignUpCallback;

/* This activity displays a form for the user to register a user name for the app.
 * In the future, this will contain more fields to create a more robust "contact"
 */
public class Register extends Activity {

	private String path;
	private Bitmap bitmap;
	private ParseFile photo;
	private static final int PICK_AFFILS_DIALOG_ID = 0;
	private static final int PICK_SYS_DIALOG_ID = 1;
	private CharSequence[] groups;
	private boolean[] groupsChecked;
	private CharSequence[] systems;
	private boolean[] systemsChecked;
    

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "FWyFNrvpkliSb7nBNugCNttN5HWpcbfaOWEutejH", "SZoWtHw28U44nJy8uKtV2oAQ8suuCZnFLklFSk46");
		path = Environment.getExternalStorageDirectory() + "/wcomm.jpg";
		setContentView(R.layout.register);
	}

	public void takeProfilePicture(View view) {
		File file = new File(path);
		Uri outputUri = Uri.fromFile(file);

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
		startActivityForResult(takePictureIntent, 0);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {		
		if (resultCode == 0) {
			/* User Cancelled */
		} else if (resultCode == -1) {
			/* Photo Taken */
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			ImageView image = (ImageView) findViewById(R.id.registerImage);
			bitmap = BitmapFactory.decodeFile(path, options);
			
			if (bitmap != null) {
				image.setImageBitmap(bitmap);

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				byte[] arr = stream.toByteArray();
				photo = new ParseFile("photo.jpg", arr);
				photo.saveInBackground();
			}
		}
	}

	private String checkBlank(String input) {
    	if ("".equals(input))
    		return "None";
    	else
    		return input;
    }
    
    /**
     * Registers a new user in the application
     * @param view
     */
    public void newUser(View view) {
    	String uname = ((EditText)findViewById(R.id.loginUsername)).getText().toString().toLowerCase();
    	uname = checkBlank(uname);
		String pw = ((EditText)findViewById(R.id.loginPassword)).getText().toString();
		String pw2 = ((EditText)findViewById(R.id.loginPassword2)).getText().toString();
		final String fname = ((EditText)findViewById(R.id.registerFname)).getText().toString();
		final String lname = ((EditText)findViewById(R.id.registerLname)).getText().toString();
		String email1 = ((EditText)findViewById(R.id.registerEmail1)).getText().toString().toLowerCase();
		email1 = checkBlank(email1);
		String email2 = ((EditText)findViewById(R.id.registerEmail2)).getText().toString().toLowerCase();
		email2 = checkBlank(email2);
		String phone1 = ((EditText)findViewById(R.id.registerPhone1)).getText().toString();
		phone1 = checkBlank(phone1);
		String phone2 = ((EditText)findViewById(R.id.registerPhone2)).getText().toString();

		/* Password Confirmation */
		phone2 = checkBlank(phone2);
    	
		if ("".equals(uname)) {
			Toast.makeText(this, "Please enter a username.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (!pw.equals(pw2)) {
			Toast.makeText(this, "Passwords do not match. Try again", Toast.LENGTH_SHORT).show();
			return;
		}

		final ParseUser user = new ParseUser();		
		/* This is the SUPER-USER VERIFICATION EMAIL 
		 * 
		 * user.getEmail() should never be used in the app,
		 * always use user.getString("email1/2") */
		String injectedMessage = "++" +
								"__Name__" + fname + "+" + lname +
								"__Email1__" + email1.replaceAll("@", "+") +
								"__Email2__" + email2.replaceAll("@", "+") +
								"__Phone1__" + phone1 +
								"__Phone2__" + phone2;
		String verificationEmail = String.format("jason.mow+%s@gmail.com",
				injectedMessage);
		user.setEmail(verificationEmail);
		
		/* User Data Fields*/
    	user.setUsername(uname);
    	user.setPassword(pw);
    	user.put("fname", fname);
    	user.put("lname", lname);
    	user.put("fullName", fname + " " + lname);
    	user.put("email1", email1);
    	user.put("email2", email2);
    	user.put("phone1", phone1);
    	user.put("phone2", phone2);
		final List<String> gl = new ArrayList<String>();
    	if(groups != null){
    		StringBuffer gr = new StringBuffer();
    		for(int i = 0; i < groups.length; i++){
    			if(groupsChecked[i]){
    				gl.add(groups[i].toString());
    				gr.append(groups[i].toString() + ",");
    			}
    		}
    		gr.replace(gr.length()-1, gr.length(), "");
    		user.put("groups", gl);
    		user.put("groups2", gr.toString());
    	}
		final List<String> sl = new ArrayList<String>();
    	if(systems != null){
    		StringBuffer sys = new StringBuffer();
    		for(int i = 0; i < systems.length; i++){
    			if(systemsChecked[i]){
    				sl.add(systems[i].toString());
    				sys.append(systems[i].toString() + ",");
    			}
    		}
			sys.replace(sys.length()-1, sys.length(), "");
    		user.put("systems", sl);
    		user.put("systems2", sys.toString());
    	}
    	

    	

    	final Toast successToast = Toast.makeText(this, "Registration Pending", Toast.LENGTH_SHORT);
		final Toast failToast = Toast.makeText(this, "Could not create user. Try again.", Toast.LENGTH_SHORT);
		final Intent i = new Intent(this, Home.class);

		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					Context context = getApplicationContext();
					Set<String> mySub = PushService.getSubscriptions(context);
					if (!mySub.contains("push_" + user.getObjectId())) {
						PushService.subscribe(context, "user_" + user.getObjectId(), ShowNotifications.class);
					}
					if (!mySub.contains("")) {
						PushService.subscribe(context, "", ShowNotifications.class);
					}				
					
					successToast.show();
					finish();
					startActivity(i);
    	        } else {
    	            // Sign up didn't succeed. Look at the ParseException
    	            // to figure out what went wrong
    	        	failToast.setText(e.getMessage());
					failToast.show();
					return;
				}
			}
		});	
	}

	// creates dialogs
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case PICK_AFFILS_DIALOG_ID:


			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Pick Groups");
			builder.setMultiChoiceItems(groups, null, new DialogInterface.OnMultiChoiceClickListener() {
				public void onClick(DialogInterface dialog, int item, boolean isChecked) {
					groupsChecked[item] = isChecked;
				}
			});
			builder.setPositiveButton("Finished", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			AlertDialog alert = builder.create();
			return alert;
		case PICK_SYS_DIALOG_ID:

			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setTitle("Pick Affected Systems");
			builder2.setMultiChoiceItems(systems, null, new DialogInterface.OnMultiChoiceClickListener() {
				public void onClick(DialogInterface dialog, int item, boolean isChecked) {
					systemsChecked[item] = isChecked;
				}
			});
			builder2.setPositiveButton("Finished", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			AlertDialog alert2 = builder2.create();
			return alert2;
		}
		return null;
	}

	// onClick function of pickAffils button
	public void showPickGroupsDialog(View view){
		ParseQuery query = new ParseQuery("Group");
		query.orderByAscending("name");
		query.findInBackground(new FindCallback(){

			@Override
			public void done(List<ParseObject> groupList, ParseException arg1) {
				// TODO Auto-generated method stub
				groups = new CharSequence[groupList.size()];
				groupsChecked = new boolean[groupList.size()];
				for(int i = 0; i < groupList.size(); i++){
					groups[i] = groupList.get(i).getString("name");
				}
				showDialog(PICK_AFFILS_DIALOG_ID);

			}

		});
	}
	// onClick function of pickSys button
	public void showPickSysDialog(View view){
		ParseQuery query = new ParseQuery("System");
		query.orderByAscending("name");
		query.findInBackground(new FindCallback(){

			@Override
			public void done(List<ParseObject> systemList, ParseException arg1) {
				// TODO Auto-generated method stub
				systems = new CharSequence[systemList.size()];
				systemsChecked = new boolean[systemList.size()];
				for(int i = 0; i < systemList.size(); i++){
					systems[i] = systemList.get(i).getString("name");
				}
				showDialog(PICK_SYS_DIALOG_ID);

			}

		});
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(this, Login.class);
		finish();
		startActivity(i);
	}
}
