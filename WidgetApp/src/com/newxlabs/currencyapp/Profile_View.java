package com.newxlabs.currencyapp;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class Profile_View extends Activity {

	private Spinner fromSpinner, toSpinner, timeSpinner;

	Button updateBtn;

	private Profile_View context;
	private int widgetId;
	private int count;

	AppWidgetManager widgetManager;
	RemoteViews views;
	private ToggleButton toggleButton;

	SeekBar seekBar;
	TextView mainResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.profile_activity);

		setResult(RESULT_CANCELED);

		context = this;

		//toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
		seekBar = (SeekBar) findViewById(R.id.conf_seek);
		mainResult = (TextView) findViewById(R.id.main_result);

		count = 15;

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

		timeSpinner = (Spinner)findViewById(R.id.timeSpinner);
		timeSpinner.setPrompt("Minutes");

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


		// Spinner Drop down TIME elements
		List<String> timesList = new ArrayList<String>();
		timesList.add("seconds");
		timesList.add("minutes");
		timesList.add("hours");
		timesList.add("days");

		ArrayAdapter<String> timesDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timesList);
		timesDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timeSpinner.setAdapter(timesDataAdapter);
		timeSpinner.setSelection(1);


		final TextView mainResult = (TextView)findViewById(R.id.main_result);

		final SeekBar seekBar = (SeekBar)findViewById(R.id.conf_seek);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			public void onStopTrackingTouch(SeekBar seekBar)
			{}

			public void onStartTrackingTouch(SeekBar seekBar)
			{}

			public void onProgressChanged(SeekBar seekBar,
					int progress,
					boolean fromUser)
			{
				//From 1 sec to 60 sec = (from 0 sec to 59 sec) + 1 sec.
				count = progress+1;
				mainResult.setText(count+" "+timeSpinner.getSelectedItem().toString());
			}
		});
	}
/*
	public void toggleClicked(View view){

		String switchText = toggleButton.getText().toString();

		if(switchText.equalsIgnoreCase("on")){ //if on then create alarm manager
			//Toast.makeText(context, "Auto Refresh - ON", Toast.LENGTH_SHORT).show();

			seekBar.setEnabled(true);
			mainResult.setEnabled(true);
		}
		else{
			//Toast.makeText(context, "Auto Refresh - OFF", Toast.LENGTH_SHORT).show();
			seekBar.setEnabled(false);
			mainResult.setEnabled(false);
		}
	}*/
	public void updateUserProfile(View view){

		SharedPreferences.Editor prefs = getSharedPreferences(""+widgetId, AppWidgetManager.INVALID_APPWIDGET_ID).edit();
		prefs.putString("from", fromSpinner.getSelectedItem().toString());
		prefs.putString("to", toSpinner.getSelectedItem().toString());
		prefs.commit();

		String switchText = toggleButton.getText().toString();

		if(switchText.equalsIgnoreCase("on")){ //if on then create alarm manager
			Toast.makeText(context, "Auto Refresh Enabled.\nThe currency will update in every "+count+" minute(s)", Toast.LENGTH_LONG).show();
			/*
			 Intent resultValue = new Intent();
			 resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
			 setResult(RESULT_OK, resultValue);
			 finish();
			 */
			//widgetManager.updateAppWidget(widgetId, views);

			//Create and launch the AlarmManager.
			//N.B.:
			//Use a different action than the first update to have more reliable results.
			//Use explicit intents to have more reliable results.
			Uri.Builder build = new Uri.Builder();
			build.appendPath(""+widgetId);
			Uri uri = build.build();
			Intent intentUpdate = new Intent(context, MainActivity.class);
			intentUpdate.setAction(MainActivity.UPDATE_ONE);//Set an action anyway to filter it in onReceive()
			intentUpdate.setData(uri);//One Alarm per instance.
			//We will need the exact instance to identify the intent.
			MainActivity.addUri(widgetId, uri);
			intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
			PendingIntent pendingIntentAlarm = PendingIntent.getBroadcast(Profile_View.this,
					0,
					intentUpdate,
					PendingIntent.FLAG_UPDATE_CURRENT);
			//If you want one global AlarmManager for all instances, put this alarmManger as
			//static and create it only the first time.
			//Then pass in the Intent all the ids and do not put the Uri.
			AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis()+(count*60*1000),
					(count*60*1000),
					pendingIntentAlarm);
			Log.d("Ok Button", "Created Alarm. Action = " + MainActivity.UPDATE_ONE +
					" URI = " + build.build().toString() +
					" Minutes = " + count*60);
		}
		else{
			Toast.makeText(context, "Auto Refresh Disabled.\nTap to refresh it manually.", Toast.LENGTH_LONG).show();			
		}

		//Return the original widget ID, found in onCreate().
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		setResult(RESULT_OK, resultValue);
		finish();
	}

}
