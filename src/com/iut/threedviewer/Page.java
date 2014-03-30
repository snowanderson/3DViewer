package com.iut.threedviewer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Page extends Activity {

	Button button = new Button(null);
	ImageView image = new ImageView(null);
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_wing);
 
		addListenerOnButton(button = (Button) findViewById(R.id.button1));
 
	}
 
	public void addListenerOnButton(Button button) {
 
		image = (ImageView) findViewById(R.id.imageView1);
 
//		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				image.setImageResource(R.drawable.bunny);
			}
 
		});
 
	}
 

	
    
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.x_wing);
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
