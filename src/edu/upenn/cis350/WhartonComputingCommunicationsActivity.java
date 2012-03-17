package edu.upenn.cis350;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class WhartonComputingCommunicationsActivity extends Activity {
	
	// fields for changing activities
	public static final int ACTIVITY_CreateNewEvent = 1;
	private static final int ACTIVITY_ShowEvent = 2;
	private static final int ACTIVITY_AGENDA = 3;
	public static final int ACTIVITY_SHOWCOMMENTS = 4;


	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
    }
    
    public void onCreateEventClick(View view){
    	Intent i = new Intent(this, CreateNewEvent.class);
    	startActivityForResult(i, ACTIVITY_CreateNewEvent);
    }
    
    public void onShowEventClick(View view){
    	Intent i = new Intent(this, ShowEvent.class);
    	startActivityForResult(i, ACTIVITY_ShowEvent);
    }
    
    public void onShowAgenda(View view) {
    	Intent i = new Intent(this, Agenda.class);
    	startActivityForResult(i, ACTIVITY_AGENDA);
    }
    
    public void onShowComments(View view) {
    	Intent i = new Intent(this, ShowComments.class);
    	startActivityForResult(i, ACTIVITY_SHOWCOMMENTS);
    }
    
    public void nuke(View view) {
		AndroidOpenDbHelper androidOpenDbHelperObj = new AndroidOpenDbHelper(this);

		// Then we need to get a writable SQLite database, because we are going to insert some values
		// SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
		SQLiteDatabase sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();
		
		sqliteDatabase.delete(AndroidOpenDbHelper.TABLE_NAME_EVENTS, null, null);
		sqliteDatabase.delete(AndroidOpenDbHelper.TABLE_NAME_COMMENTS, null, null);	
		
		sqliteDatabase.close();
	}
}