package edu.upenn.cis350;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PostMessage extends Activity {
	
	private String uname;
	private EventPOJO event;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.postmessage);
		Bundle extras = this.getIntent().getExtras();
		if(extras != null){
			uname = (String)extras.get("user");
			event = (EventPOJO)extras.get("eventPOJO");
		}
	}
	 
	public void onPostClick(View view){
		TextView tv = (TextView)findViewById(R.id.messageBox);
		MessagePOJO msg = new MessagePOJO();
		msg.setAuthor(uname);
		msg.setText(tv.getText().toString());
		msg.setTimestamp(System.currentTimeMillis() + "");
		Intent i = new Intent(this, ShowEvent.class);
		i.putExtra("eventPOJO", event);
		i.putExtra("message", msg);
		i.putExtra("user", uname);
		startActivity(i);
	}

}
