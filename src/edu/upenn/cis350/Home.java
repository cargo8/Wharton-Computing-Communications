package edu.upenn.cis350;

import android.app.Activity;
import android.os.Bundle;

public class Home extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Bundle extras = this.getIntent().getExtras();

    }
}
