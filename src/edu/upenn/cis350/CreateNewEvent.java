package edu.upenn.cis350;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private CharSequence[] affils;
    private boolean[] affilsChecked;
    private CharSequence[] systems;
    private boolean[] systemsChecked;

    //dialog constants
    static final int START_DATE_DIALOG_ID = 0;
    static final int END_DATE_DIALOG_ID = 1;
	static final int PICK_AFFILS_DIALOG_ID = 2;
	private static final int PICK_SYS_DIALOG_ID = 3;

    
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
        
        //populate spinner
        populateSpinners();
        
        //set up severity buttons
        //TODO closen: add listeners
        final RadioButton radioRed = (RadioButton) findViewById(R.id.radioRed);
        radioRed.setBackgroundColor(Color.RED);
        final RadioButton radioYellow = (RadioButton) findViewById(R.id.radioYellow);
        radioYellow.setBackgroundColor(Color.YELLOW);
        final RadioButton radioGreen = (RadioButton) findViewById(R.id.radioGreen);
        radioGreen.setBackgroundColor(Color.GREEN);

    }
    
    private void populateSpinners() {
        Spinner spinner = (Spinner) findViewById(R.id.personSpinner1);
        ArrayAdapter <CharSequence> adapter =
        	  new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for(int i = 1; i <= 10; i++){
        	adapter.add("Person " + i);
        }
        spinner.setAdapter(adapter);
        
        Spinner spinner2 = (Spinner) findViewById(R.id.personSpinner2);
        ArrayAdapter <CharSequence> adapter2 =
        	  new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for(int i = 1; i <= 10; i++){
        	adapter2.add("Person " + i);
        }
        spinner2.setAdapter(adapter2);
	}

	// onClick function of submit button
    public void onCreateEventSubmit(View view){
    	//TODO closen: transition to main activity
    	//Intent i = new Intent(this, WhartonComputingCommunicationsActivity.class);
    	Intent i = new Intent(this, ShowEvent.class);
    	EditText temp = (EditText)findViewById(R.id.eventTitle);
    	i.putExtra("eventTitle", temp.getText().toString());
    	temp = (EditText)findViewById(R.id.eventDesc);
    	i.putExtra("eventDesc", temp.getText().toString());
    	temp = (EditText)findViewById(R.id.eventActions);
    	i.putExtra("eventActions", temp.getText().toString());
    	TextView temp2 = (TextView)findViewById(R.id.startDateDisplay);
    	i.putExtra("startDate", temp2.getText().toString());
    	temp2 = (TextView)findViewById(R.id.endDateDisplay);
    	i.putExtra("endDate", temp2.getText().toString());
    	i.putExtra("affils", affils);
    	i.putExtra("affilsChecked", affilsChecked);
    	i.putExtra("systems", systems);
    	i.putExtra("systemsChecked", systemsChecked);
    	Spinner spin1 = (Spinner)findViewById(R.id.personSpinner1);
    	i.putExtra("person1", spin1.getSelectedItem().toString());
    	spin1 = (Spinner)findViewById(R.id.personSpinner2);
    	i.putExtra("person2", spin1.getSelectedItem().toString());
    	;
    	if(((RadioButton)findViewById(R.id.radioRed)).isChecked())
    		i.putExtra("sevColor", Color.RED);
    	else if(((RadioButton)findViewById(R.id.radioYellow)).isChecked())
    		i.putExtra("sevColor", Color.YELLOW);
    	else if(((RadioButton)findViewById(R.id.radioGreen)).isChecked())
    		i.putExtra("sevColor", Color.GREEN);
    	else
    		i.putExtra("sevColor", Color.BLACK);
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
    
    // onClick function of pickAffils button
    public void showPickAffilsDialog(View view){
       	showDialog(PICK_AFFILS_DIALOG_ID);
    }
    
    public void showPickSysDialog(View view){
    	showDialog(PICK_SYS_DIALOG_ID);
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
    
    // creates dialogs
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
        case PICK_AFFILS_DIALOG_ID:
        	final CharSequence[] items = {"Group 1", "Group 2", "Group 3"};
        	affils = items;
        	affilsChecked = new boolean[items.length];

        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setTitle("Pick Affiliations");
        	builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
        	    public void onClick(DialogInterface dialog, int item, boolean isChecked) {
        	        Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
        	        affilsChecked[item] = isChecked;
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
        	final CharSequence[] items2 = {"System 1", "System 2", "System 3"};
        	systems = items2;
        	systemsChecked = new boolean[items2.length];

        	AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        	builder2.setTitle("Pick Affected Systems");
        	builder2.setMultiChoiceItems(items2, null, new DialogInterface.OnMultiChoiceClickListener() {
        	    public void onClick(DialogInterface dialog, int item, boolean isChecked) {
        	        Toast.makeText(getApplicationContext(), items2[item], Toast.LENGTH_SHORT).show();
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
    
}
