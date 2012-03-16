package edu.upenn.cis350;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

// A helper class to manage database creation and version management.
public class AndroidOpenDbHelper extends SQLiteOpenHelper {
	// Database attributes
	public static final String DB_NAME = "events_db";
	public static final int DB_VERSION = 1;

	// Table attributes
	public static final String TABLE_NAME = "events_table";
	public static final String COLUMN_NAME_EVENT_TITLE = "event_title_column";
	public static final String COLUMN_NAME_EVENT_DESC = "event_desc_column";
	public static final String COLUMN_NAME_EVENT_ACTIONS = "event_actions_column";
	//TODO (closen) add more columns

	public AndroidOpenDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	// Called when the database is created for the first time.
	//This is where the creation of tables and the initial population of the tables should happen.
	@Override
	public void onCreate(SQLiteDatabase db) {
		// We need to check whether table that we are going to create already exists.
		// This method gets executed every time we creat an object of this class.
		//"create table if not exists TABLE_NAME ( BaseColumns._ID integer primary key autoincrement, FIRST_COLUMN_NAME text not null, SECOND_COLUMN_NAME integer not null);"
		String sqlQueryToCreateUndergraduateDetailsTable = "create table if not exists " + TABLE_NAME + " ( " + BaseColumns._ID + " integer primary key autoincrement, "
																+ COLUMN_NAME_EVENT_TITLE + " text not null, "
																+ COLUMN_NAME_EVENT_DESC + " text not null, "
																+ COLUMN_NAME_EVENT_ACTIONS + " real not null);";
		// Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
		db.execSQL(sqlQueryToCreateUndergraduateDetailsTable);
	}

	// onUpgrade method is used when we need to upgrade the database to a new version
	//As an example, the first release of the app contains DB_VERSION = 1
	//Then, the second release of the same app contains DB_VERSION = 2
	//where you may have added some new tables or altered the existing ones
	//Then we need check and do relevant actions to keep our passed data and move with the next structure
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion == 1 && newVersion == 2){
			// Upgrade the database
		}
	}
}
