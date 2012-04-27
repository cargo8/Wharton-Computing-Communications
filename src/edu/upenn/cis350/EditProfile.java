package edu.upenn.cis350;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/* This activity displays a form for the user to register a user name for the app.
 * In the future, this will contain more fields to create a more robust "contact"
 */
public class EditProfile extends Activity {

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
		Parse.initialize(this, Settings.APPLICATION_ID, Settings.CLIENT_ID);
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

	private String checkBlank(String input) {
		if ("".equals(input))
			return "None";
		else
			return input;
	}

	/**
	 * Updates a user's profile in the application
	 * @param view
	 */
	public void updateUser(View view) {
		String uname = ((EditText)findViewById(R.id.loginUsername)).getText().toString().toLowerCase();
		uname = checkBlank(uname);
		String pw = ((EditText)findViewById(R.id.loginPassword)).getText().toString();
		String pw2 = ((EditText)findViewById(R.id.loginPassword2)).getText().toString();
		String fname = ((EditText)findViewById(R.id.registerFname)).getText().toString();
		String lname = ((EditText)findViewById(R.id.registerLname)).getText().toString();
		String email1 = ((EditText)findViewById(R.id.registerEmail1)).getText().toString().toLowerCase();
		email1 = checkBlank(email1);
		String email2 = ((EditText)findViewById(R.id.registerEmail2)).getText().toString().toLowerCase();
		email2 = checkBlank(email2);
		String phone1 = ((EditText)findViewById(R.id.registerPhone1)).getText().toString();
		phone1 = checkBlank(phone1);
		String phone2 = ((EditText)findViewById(R.id.registerPhone2)).getText().toString();
		phone2 = checkBlank(phone2);

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
		if(groups != null){
			StringBuffer gr = new StringBuffer();
			List<String> gl = new ArrayList<String>();
			for(int i = 0; i < groups.length; i++){
				if(groupsChecked[i]){
					gl.add(groups[i].toString());
					gr.append(groups[i].toString() + ",");
				}
			}
			if (gr.length() != 0) {
				gr.replace(gr.length()-1, gr.length(), "");
			}
			user.put("groups", gl);
			user.put("groups2", gr.toString());
		}
		if(systems != null){
			StringBuffer sys = new StringBuffer();
			List<String> sl = new ArrayList<String>();
			for(int i = 0; i < systems.length; i++){
				if(systemsChecked[i]){
					sl.add(systems[i].toString());
					sys.append(systems[i].toString() + ",");
				}
			}
			if (sys.length() != 0) {
				sys.replace(sys.length()-1, sys.length(), "");
			}
			user.put("systems", sl);
			user.put("systems2", sys.toString());
		}



		user.saveEventually();
		finish();
	}

	// creates dialogs
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case PICK_AFFILS_DIALOG_ID:


			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Pick Groups");
			builder.setMultiChoiceItems(groups, groupsChecked, new DialogInterface.OnMultiChoiceClickListener() {
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
			builder2.setMultiChoiceItems(systems, systemsChecked, new DialogInterface.OnMultiChoiceClickListener() {
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
				List<String> gl = (List<String>) ParseUser.getCurrentUser().get("groups");
				groups = new CharSequence[groupList.size()];
				groupsChecked = new boolean[groupList.size()];
				for(int i = 0; i < groupList.size(); i++){
					String temp = groupList.get(i).getString("name");
					groups[i] = temp;
					if(gl != null && gl.contains(temp))
						groupsChecked[i] = true;
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
				List<String> sl = (List<String>) ParseUser.getCurrentUser().get("systems");
				systems = new CharSequence[systemList.size()];
				systemsChecked = new boolean[systemList.size()];
				for(int i = 0; i < systemList.size(); i++){
					String temp = systemList.get(i).getString("name");
					systems[i] = temp;
					if(sl != null && sl.contains(temp))
						systemsChecked[i] = true;
				}
				showDialog(PICK_SYS_DIALOG_ID);

			}

		});
	}
}
