package com.newxlabs.currencyapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
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
		
		
		// Spinner click listener
		//fromSpinner.setOnItemSelectedListener((OnItemSelectedListener) this);
 
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("USD");
        categories.add("INR");
        categories.add("EUR");
        categories.add("GBP");
        categories.add("AUD");
        categories.add("CAD");
 
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        // attaching data adapter to spinner
        fromSpinner.setAdapter(dataAdapter);
        toSpinner.setAdapter(dataAdapter);
	}

}
