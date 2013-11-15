package com.newxlabs.currencyapp;

import java.util.ArrayList;

import org.json.JSONObject;

import com.newxlabs.widgetapp.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class About_View extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about2);

		
		TextView title = (TextView) findViewById(R.id.titleTextView);
		TextView descp = (TextView) findViewById(R.id.descpTextView);
	}
}
