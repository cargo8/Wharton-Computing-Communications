package edu.upenn.cis350;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
    }
    
    public void newUser(View view) {
    	AndroidOpenDbHelper dbHelper = new AndroidOpenDbHelper(this);
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	
    	String uname = ((EditText)findViewById(R.id.loginUsername)).getText().toString();
		String pw = ((EditText)findViewById(R.id.loginPassword)).getText().toString();
		
    	String[] columns = {dbHelper.COLUMN_NAME_USER_NAME};
    	Cursor cursor = db.query(dbHelper.TABLE_NAME_USERS, columns, 
    			dbHelper.COLUMN_NAME_USER_NAME + " = '" + uname + "'", 
    			null, null, null, null);
    	int count = cursor.getCount();
    	db.close();
    	
    	if (count == 0) {
    		db = dbHelper.getWritableDatabase();
    		ContentValues values = new ContentValues();
    		values.put(dbHelper.COLUMN_NAME_USER_NAME, uname);
    		values.put(dbHelper.COLUMN_NAME_USER_PW, pw);
    		values.put(dbHelper.COLUMN_NAME_USER_SIGNUP_TIMESTAMP, System.currentTimeMillis());
    		
    		long affectedRow = db.insert(dbHelper.TABLE_NAME_USERS, null, values);
    		db.close();
    		
    		Toast.makeText(this, "User " + uname + " created.", Toast.LENGTH_SHORT).show();
    		Intent i = new Intent(this, WhartonComputingCommunicationsActivity.class);
    		startActivity(i);
    	} else {
    		Toast.makeText(this, "This username is taken. Try again.", Toast.LENGTH_SHORT).show();
    	}
    }
}
