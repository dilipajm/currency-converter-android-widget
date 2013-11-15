package com.newxlabs.currencyapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Spinner;


public class Profile_View extends Activity {

	private Spinner fromSpinner, toSpinner;

	Button updateBtn;
	
	private Profile_View context;
	private int widgetId;
	AppWidgetManager widgetManager;
	RemoteViews views;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.profile_activity);

		setResult(RESULT_CANCELED);
		
		context = this;
		
		Bundle extras = getIntent().getExtras();
		if(extras!=null){
			widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		
		widgetManager = AppWidgetManager.getInstance(context);
		views = new RemoteViews(context.getPackageName(), R.layout.activity_main);
		
		
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
        
        fromSpinner.setSelection(0);
        toSpinner.setSelection(1);
        
        
	}
	
	public void updateUserProfile(View view){
		
		SharedPreferences.Editor prefs = getSharedPreferences("currency", 0).edit();
        prefs.putString("from", fromSpinner.getSelectedItem().toString());
        prefs.putString("to", toSpinner.getSelectedItem().toString());
		prefs.commit();
				 
		
		
		
		 Intent resultValue = new Intent();
		 resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		 setResult(RESULT_OK, resultValue);
		 finish();
		 
		 //widgetManager.updateAppWidget(widgetId, views);
			
		 
	}

}
