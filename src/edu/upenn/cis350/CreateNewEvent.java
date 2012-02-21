package edu.upenn.cis350;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class CreateNewEvent extends Activity {

	
	// fields for dateDisplay popup (START DATE FIELDS)
	private TextView mDateDisplay;
    private Button mPickDate;
    private int mYear;
    private int mMonth;
    private int mDay;
	// fields for dateDisplay popup (END DATE FIELDS)
    private TextView mDateDisplay2;
    private Button mPickDate2;
    private int mYear2;
    private int mMonth2;
    private int mDay2;

    static final int START_DATE_DIALOG_ID = 0;
    static final int END_DATE_DIALOG_ID = 1;

    
    // the callback received when the user "sets" the date in the dialog (START DATE)
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };
     // the callback received when the user "sets" the date in the dialog (END DATE)
     private DatePickerDialog.OnDateSetListener mDateSetListener2 =
           new DatePickerDialog.OnDateSetListener() {

               public void onDateSet(DatePicker view, int year, 
                                     int monthOfYear, int dayOfMonth) {
                   mYear2 = year;
                   mMonth2 = monthOfYear;
                   mDay2 = dayOfMonth;
                   updateDisplay();
               }
           };
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventform);
        
        // capture our View elements
        mDateDisplay = (TextView) findViewById(R.id.startDateDisplay);
        mPickDate = (Button) findViewById(R.id.pickStartDate);

        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        
        mDateDisplay2 = (TextView) findViewById(R.id.endDateDisplay);
        mPickDate2 = (Button) findViewById(R.id.pickEndDate);

        mYear2 = mYear;
        mMonth2 = mMonth;
        mDay2 = mDay;
        // display the current date (this method is below)
        updateDisplay();
    }
    
    // onClick function of submit button
    public void onCreateEventSubmit(View view){
    	//TODO closen: transition to main activity
    	Intent i = new Intent(this, WhartonComputingCommunicationsActivity.class);
    	startActivity(i);
    }
    
    // onClick function of pickStartDateButton
    public void showStartDateDialog(View view){
    	showDialog(START_DATE_DIALOG_ID);
    }
    
    // onClick function of pickEndDateButton
    public void showEndDateDialog(View view){
    	showDialog(END_DATE_DIALOG_ID);
    }
    
    // updates the date in the TextView
    private void updateDisplay() {
        mDateDisplay.setText(
            new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mMonth + 1).append("-")
                    .append(mDay).append("-")
                    .append(mYear).append(" "));
        mDateDisplay2.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mMonth2 + 1).append("-")
                        .append(mDay2).append("-")
                        .append(mYear2).append(" "));
    }
    
    // creates dialog for datePicker
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case START_DATE_DIALOG_ID:
            return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        case END_DATE_DIALOG_ID:
        		return new DatePickerDialog(this,
        					mDateSetListener2,
        					mYear2, mMonth2, mDay2);
        }
        return null;
    }
    
}
