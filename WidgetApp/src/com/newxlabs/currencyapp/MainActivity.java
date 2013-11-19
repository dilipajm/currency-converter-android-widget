package com.newxlabs.currencyapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.webkit.WebView.FindListener;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends AppWidgetProvider {

	private static final String COPY_CLICKED    = "automaticWidgetCopyButtonClick";
	private static final String SHARE_CLICKED    = "automaticWidgetShareButtonClick";
	public Context myContext;
	public Intent myIntent;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		super.onUpdate(context, appWidgetManager, appWidgetIds);

		//SharedPreferences.Editor prefs = context.getSharedPreferences("data", Context.MODE_WORLD_WRITEABLE).edit();

		ComponentName thisWidget = new ComponentName(context, MainActivity.class);

		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		
		
		for(int widgetId : allWidgetIds){

			//int number = (new Random().nextInt(thoughtsArray.length));


			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.activity_main);
				
			//remoteViews.setTextViewText(R.id.update, String.valueOf(number));
			//remoteViews.setTextViewText(R.id.update, "Loading..");

			Intent intent = new Intent(context, MainActivity.class);
			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

			PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, widgetId, intent, 0);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent2);
			remoteViews.setOnClickPendingIntent(R.id.lastUpdated, pendingIntent2);
			//remoteViews.setOnClickPendingIntent(R.id.copyBtn, pendingIntent2);
			remoteViews.setOnClickPendingIntent(R.id.copyBtn, getPendingSelfIntent(context, COPY_CLICKED));

			//getLatestData(remoteViews,"USD", "INR");

			//copy button

			//**************
			/*Intent clickIntent = new Intent(context, MainActivity.class);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
			PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, widgetId, clickIntent, 0);
			remoteViews.setOnClickPendingIntent(R.id.copyBtn, pendingIntent2);
			 *///***************




			appWidgetManager.updateAppWidget(widgetId, remoteViews);

			//prefs.putString("thought", thoughtsArray[number]);
			//prefs.commit();
			
			myContext = context;
			myIntent = intent;

			//Toast.makeText(context, "Its just take few seconds to update.", Toast.LENGTH_SHORT).show();
		}
		
		/*
		//30 mins update
		int widId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		SharedPreferences prefs = context.getSharedPreferences(""+widId, AppWidgetManager.INVALID_APPWIDGET_ID);
		String from = prefs.getString("from","USD");
		String to = prefs.getString("to","INR");
		getLatestData(remoteViews,from, to,widId);
		*/
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);

		int widId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -99);
		//Toast.makeText(context, "onReceive- "+widId, Toast.LENGTH_SHORT).show();

		RemoteViews remoteViews;
		//ComponentName watchWidget;

		remoteViews = new RemoteViews(context.getPackageName(), R.layout.activity_main);
		myContext = context;
		myIntent = intent;

		//Toast.makeText(context, "Its just take few seconds to update.", Toast.LENGTH_SHORT).show();

		SharedPreferences prefs = context.getSharedPreferences(""+widId, -99);
		String from = prefs.getString("from","USD");
		String to = prefs.getString("to","INR");
		getLatestData(remoteViews,from, to,widId);

		/*
		if (COPY_CLICKED.equals(intent.getAction())) {

			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

			RemoteViews remoteViews;
			ComponentName watchWidget;

			remoteViews = new RemoteViews(context.getPackageName(), R.layout.activity_main);
			watchWidget = new ComponentName(context, MainActivity.class);

			//remoteViews.setTextViewText(R.id.copyBtn, "TESTING");

			//appWidgetManager.updateAppWidget(watchWidget, remoteViews);


			myContext = context;
			myIntent = intent;

			Toast.makeText(context, "Its just take few seconds to update.", Toast.LENGTH_SHORT).show();

			SharedPreferences prefs = context.getSharedPreferences("currency", 0);
            String from = prefs.getString("from","USD");
            String to = prefs.getString("to","INR");
			getLatestData(remoteViews,from, to);
		}
		 */
	}

	protected PendingIntent getPendingSelfIntent(Context context, String action) {
		Intent intent = new Intent(context, getClass());
		intent.setAction(action);
		return PendingIntent.getBroadcast(context, 0, intent, 0);
	}


	public void getLatestData(RemoteViews remoteViews,String from, String to, int widId){

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(myContext);
		remoteViews.setTextViewText(R.id.update, "Fetching data...");
		appWidgetManager.updateAppWidget(widId, remoteViews);
		//appWidgetManager.updateAppWidget(appWidgetManager.getAppWidgetIds(thisWidget), remoteViews);

		String url = "http://rate-exchange.appspot.com/currency?from="+from+"&to="+to;

		new ServerAsycTask(remoteViews, widId).execute(url);
		//new ServerAsycTask().execute(url);
	}

	private class ServerAsycTask extends AsyncTask<String, Void, String> {

		private RemoteViews views;

		int widId;

		public ServerAsycTask(RemoteViews views, int widId){
			this.views = views;
			this.widId = widId;
		}

		protected String doInBackground(String... urls){
			String response = "";
			for (String url : urls) {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse execute = client.execute(httpGet);
					InputStream content = execute.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {

			//Log.d("ServerCommunication", "ServerCommunication onPostExecute ServerAsycTask: "+result);


			//Toast.makeText(myContext, "widId: "+this.widId+" for url: "+this.url, Toast.LENGTH_SHORT).show();

			try {
				JSONObject obj = new JSONObject(result);
				String str = "1 "+obj.getString("from")+" = "+obj.getString("rate")+" "+obj.getString("to");
				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(myContext);

				//ComponentName watchWidget;

				//watchWidget = new ComponentName(myContext, MainActivity.class);

				String date = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
				views.setTextViewText(R.id.update, str);
				views.setTextViewText(R.id.lastUpdated, "Last Updated: "+date);
				
				appWidgetManager.updateAppWidget(this.widId, views);
				//appWidgetManager.updateAppWidget(watchWidget, views);

			} catch (Exception e) {
				e.printStackTrace();
			}

			//Toast.makeText("d", "App definitions received.", Toast.LENGTH_LONG).show();
			/*SQLiteHelper dbHandler = new SQLiteHelper(myContext, null, null, 1);
			try {
				dbHandler.appDefinition_insertDB(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}*/
		}
	}
}
