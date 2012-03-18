package edu.upenn.cis350;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

public class Home extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Bundle extras = this.getIntent().getExtras();

    }
    
    public void onCreateEventClick(View view){
    	Intent i = new Intent(this, CreateNewEvent.class);
    	startActivity(i);
    }
    
    public void onShowEventClick(View view){
    	Intent i = new Intent(this, ShowEvent.class);
    	startActivity(i);
    }
    
    public void onShowAgenda(View view) {
    	Intent i = new Intent(this, Agenda.class);
    	startActivity(i);
    }
    
    public void onShowComments(View view) {
    	Intent i = new Intent(this, ShowComments.class);
    	startActivity(i);
    }
    
    public void nuke(View view) {
 		AndroidOpenDbHelper androidOpenDbHelperObj = new AndroidOpenDbHelper(this);

 		// Then we need to get a writable SQLite database, because we are going to insert some values
 		// SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
 		SQLiteDatabase db = androidOpenDbHelperObj.getWritableDatabase();
 		
// 		db.delete(AndroidOpenDbHelper.TABLE_NAME_EVENTS, null, null);
// 		db.delete(AndroidOpenDbHelper.TABLE_NAME_COMMENTS, null, null);	

 		String deleteEventsTable = "drop table events_table;";
 		String deleteCommentsTable = "drop table comments_table;";
 		String deleteUsersTable = "drop table users_table;";
 		try {
 	 		db.execSQL(deleteEventsTable);
 		} catch (Exception e1) {
 			try {
 				db.execSQL(deleteCommentsTable);
 			} catch (Exception e2) {
 				try {
 					db.execSQL(deleteUsersTable);
 				} catch (Exception e3) {
 					
 				}
 			}
 		}
 		db.close();
 	}
}
