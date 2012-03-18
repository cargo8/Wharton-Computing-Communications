package edu.upenn.cis350;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class WhartonComputingCommunicationsActivity extends Activity {
	
	// fields for changing activities
	public static final int ACTIVITY_Home = 0;
	public static final int ACTIVITY_CreateNewEvent = 1;
	private static final int ACTIVITY_ShowEvent = 2;
	private static final int ACTIVITY_Agenda = 3;
	public static final int ACTIVITY_ShowComments = 4;
	public static final int ACTIVITY_Register = 5;


	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);   
    }
    
    public void login(View view) {
		AndroidOpenDbHelper dbHelper = new AndroidOpenDbHelper(this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		dbHelper.createUsersTable(db);

		String uname = ((EditText)findViewById(R.id.loginUsername)).getText().toString();
		String pw = ((EditText)findViewById(R.id.loginPassword)).getText().toString();
		
		if (uname.equals("") || pw.equals("")) {
			Toast.makeText(this, "Login failed.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String[] columns = {dbHelper.COLUMN_NAME_USER_NAME, dbHelper.COLUMN_NAME_USER_PW};
		
		Cursor cursor = db.query(dbHelper.TABLE_NAME_USERS, columns, 
				dbHelper.COLUMN_NAME_USER_NAME + " = '" + uname + "' AND " +
				dbHelper.COLUMN_NAME_USER_PW + " = '" + pw + "'", null, null, null, null);
		startManagingCursor(cursor);
		int count = cursor.getCount();
		db.close();
		
		if(count > 0) {
			db.close();
    		Toast.makeText(this, "Login Successful.", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(this, Home.class);
			startActivityForResult(i, ACTIVITY_Home);
		} else {
			db.close();
			Toast.makeText(this, "Login failed.", Toast.LENGTH_SHORT).show();
		}
    }
    
    public void clickRegister(View view) {
    	Intent i = new Intent(this, Register.class);
    	startActivityForResult(i, ACTIVITY_ShowComments);
    }
        
    public void nuke(View view) {
		AndroidOpenDbHelper androidOpenDbHelperObj = new AndroidOpenDbHelper(this);

		// Then we need to get a writable SQLite database, because we are going to insert some values
		// SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
		SQLiteDatabase db = androidOpenDbHelperObj.getWritableDatabase();
		
//		db.delete(AndroidOpenDbHelper.TABLE_NAME_EVENTS, null, null);
//		db.delete(AndroidOpenDbHelper.TABLE_NAME_COMMENTS, null, null);	

		String deleteEventsTable = "drop table events_table;";
		String deleteCommentsTable = "drop table comments_table;";
		String deleteUsersTable = "drop table users_table;";
		
		db.execSQL(deleteEventsTable + deleteCommentsTable + deleteUsersTable);
		db.close();
	}
}