package edu.upenn.cis350;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ShowComments extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showcomments);
        TextView v = (TextView)findViewById(R.id.message);
        Bundle extras = this.getIntent().getExtras();
        if(extras != null){
        	v.setText((String)extras.get("message"));
        }
    }

    public void onPostComment(View view) {
    	
    	
    }
    
    public void onBackToEventClick(View view){
    	Intent i = new Intent(this, ShowEvent.class);
    	startActivity(i);
    }
    
}
