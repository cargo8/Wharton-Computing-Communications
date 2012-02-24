package edu.upenn.cis350;

import android.app.Activity;
import android.os.Bundle;
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
    
}
