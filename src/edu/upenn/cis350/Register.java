package edu.upenn.cis350;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SignUpCallback;

/* This activity displays a form for the user to register a user name for the app.
 * In the future, this will contain more fields to create a more robust "contact"
 */
public class Register extends Activity {

	private String path;
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
			options.inSampleSize = 4;
			ImageView image = (ImageView) findViewById(R.id.registerImage);
			Bitmap bitmap = BitmapFactory.decodeFile(path, options);
			image.setImageBitmap(bitmap);
		}
	}

	/**
	 * Utility to see if app can handle intent
	 * @param context Active activity
	 * @param action The action for a new intent
	 * @return true if Intent can handle it, false otherwise
	 */
	private static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list =
				packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	/**
	 * Registers a new user in the application
	 * @param view
	 */
	public void newUser(View view) {
		/* Get all input data */
		String uname = ((EditText)findViewById(R.id.loginUsername)).getText().toString().toLowerCase();
		String pw = ((EditText)findViewById(R.id.loginPassword)).getText().toString();
		String pw2 = ((EditText)findViewById(R.id.loginPassword2)).getText().toString();
		String fname = ((EditText)findViewById(R.id.registerFname)).getText().toString();
		String lname = ((EditText)findViewById(R.id.registerLname)).getText().toString();
		String email1 = ((EditText)findViewById(R.id.registerEmail1)).getText().toString();
		String email2 = ((EditText)findViewById(R.id.registerEmail2)).getText().toString();
		String phone1 = ((EditText)findViewById(R.id.registerPhone1)).getText().toString();
		String phone2 = ((EditText)findViewById(R.id.registerPhone2)).getText().toString();

		/* Password Confirmation */
		if (!pw.equals(pw2)) {
			Toast.makeText(this, "Passwords do not match. Try again", Toast.LENGTH_SHORT).show();
			return;
		}

		/* Bundle data into ParseUser */
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
					PushService.subscribe(context, "", Login.class);
					PushService.subscribe(context, "user_" + user.getObjectId(), Login.class);
					successToast.show();
					finish();
				} else {
					// Sign up didn't succeed. Look at the ParseException
					failToast.show();
					return;
				}
			}
		});	
	}
}
