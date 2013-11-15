package com.newxlabs.currencyapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;


public class Profile_View extends Activity {

	private Spinner fromSpinner, toSpinner;

	Button updateBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.profile_activity);

		fromSpinner = (Spinner)findViewById(R.id.fromSpinner);
		fromSpinner.setPrompt("From");
		
		toSpinner = (Spinner)findViewById(R.id.toSpinner);
		toSpinner.setPrompt("From");

		updateBtn = (Button) findViewById(R.id.updateBtn);
		
	}

}
