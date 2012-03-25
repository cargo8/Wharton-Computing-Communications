package edu.upenn.cis350;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

/* This activity is what the app will start with after logging in.
 * For now, it just shows a few buttons to create a new event
 * and to show the agenda.
 * TODO: Make it look more visually appealing
 */
public class Home extends Activity {

	private String uname;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
        	uname = extras.getString("user");
        }

    }
    
    // onClick function for createEvent button
    public void onCreateEventClick(View view){
    	Intent i = new Intent(this, CreateNewEvent.class);
		i.putExtra("user", uname);
    	startActivity(i);
    }
    
    // onClick function for showEvent button (deprecated)
    public void onShowEventClick(View view){
    	Intent i = new Intent(this, ShowEvent.class);
		i.putExtra("user", uname);
    	startActivity(i);
    }
    
    // onClick function for showAgenda button
    public void onShowAgenda(View view) {
    	Intent i = new Intent(this, Agenda.class);
		i.putExtra("user", uname);
    	startActivity(i);
    }
    
    // onClick function for showComments button (deprecated)
    public void onShowComments(View view) {
    	Intent i = new Intent(this, ShowComments.class);
		i.putExtra("user", uname);
    	startActivity(i);
    }
    
    @Override
    public void onBackPressed() {
       Intent i = new Intent(this, WhartonComputingCommunicationsActivity.class);
       startActivity(i);
    }    
    
    // Drops the tables in the database
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
 		String deleteMessagesTable = "drop table messages_table;";
 		try {
 			db.execSQL(deleteCommentsTable);
 			db.execSQL(deleteEventsTable);
 			db.execSQL(deleteUsersTable);
 			db.execSQL(deleteMessagesTable);
 		} catch (Exception e1) {
 			try {
 	 			db.execSQL(deleteEventsTable);
 				db.execSQL(deleteUsersTable);
 	 			db.execSQL(deleteMessagesTable);
 			} catch (Exception e2) {
 				try {
 					db.execSQL(deleteUsersTable);
 		 			db.execSQL(deleteMessagesTable);
 				} catch (Exception e3) {
 					try {
 	 		 			db.execSQL(deleteMessagesTable);
 					} catch (Exception e4) {
 						
 					}
 				}
 			}
 		}
 		db.close();
 	}
}
