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
import android.os.AsyncTask;
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

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
			//getLatestData(remoteViews,"USD", "INR");

			//copy button
			remoteViews.setOnClickPendingIntent(R.id.copyBtn, getPendingSelfIntent(context, COPY_CLICKED));


			appWidgetManager.updateAppWidget(widgetId, remoteViews);

			//prefs.putString("thought", thoughtsArray[number]);
			//prefs.commit();
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);

		if (COPY_CLICKED.equals(intent.getAction())) {

			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

			RemoteViews remoteViews;
			ComponentName watchWidget;

			remoteViews = new RemoteViews(context.getPackageName(), R.layout.activity_main);
			watchWidget = new ComponentName(context, MainActivity.class);

			//remoteViews.setTextViewText(R.id.copyBtn, "TESTING");

			//appWidgetManager.updateAppWidget(watchWidget, remoteViews);

			/*SharedPreferences prefs = context.getSharedPreferences("data", Context.MODE_WORLD_WRITEABLE);
            String text = prefs.getString("thought", "Welcome to wise thoughts!")+" \nBy Wise Thought - Widget App";

        	ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        	ClipData clip = ClipData.newPlainText("thought",text);
        	clipboard.setPrimaryClip(clip);
			 */
			myContext = context;
			myIntent = intent;

			//Toast.makeText(context, "Its just take few seconds to update.", Toast.LENGTH_SHORT).show();
			getLatestData(remoteViews,"USD", "INR");
		}
		/*else if (SHARE_CLICKED.equals(intent.getAction())) {
        	SharedPreferences prefs = context.getSharedPreferences("data", Context.MODE_WORLD_WRITEABLE);
    	    String text = prefs.getString("thought", "Welcome to wise thoughts!")+" \nBy Wise Thought - Widget App";


    	    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.activity_main);
            watchWidget = new ComponentName(context, MainActivity.class);

            //remoteViews.setTextViewText(R.id.copyBtn, "TESTING");

            //appWidgetManager.updateAppWidget(watchWidget, remoteViews);

            //**************


    	    Intent configIntent = new Intent(context, About_View.class);

    	    PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);

    	    remoteViews.setOnClickPendingIntent(R.id.shareBtn, configPendingIntent);

    	    ComponentName thisWidget = new ComponentName(context, MainActivity.class);

    	    appWidgetManager.updateAppWidget(appWidgetManager.getAppWidgetIds(thisWidget), remoteViews);

    	    /*
		 * Intent intent = new Intent(Intent.ACTION_SEND);
intent.setType("text/plain");
intent.putExtra(android.content.Intent.EXTRA_TEXT, "News for you!");
startActivity(intent);
		 * */
		/* Intent sendIntent = new Intent();
        	sendIntent.setAction(Intent.ACTION_SEND);
        	sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        	sendIntent.setType("text/plain");
        	context.startActivity(Intent.createChooser(sendIntent, context.getResources().getText(R.string.hello_world)));
		 */
		/*Toast.makeText(context, "This feature coming soon!", Toast.LENGTH_SHORT).show();
         }*/
	}

	protected PendingIntent getPendingSelfIntent(Context context, String action) {
		Intent intent = new Intent(context, getClass());
		intent.setAction(action);
		return PendingIntent.getBroadcast(context, 0, intent, 0);
	}


	public void getLatestData(RemoteViews remoteViews,String from, String to){

		 AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(myContext);
		ComponentName thisWidget = new ComponentName(myContext, MainActivity.class);

	    appWidgetManager.updateAppWidget(appWidgetManager.getAppWidgetIds(thisWidget), remoteViews);
	    
	    remoteViews.setTextViewText(R.id.update, "Fetching data...");
		appWidgetManager.updateAppWidget(appWidgetManager.getAppWidgetIds(thisWidget), remoteViews);
		
		String url = "http://rate-exchange.appspot.com/currency?from="+from+"&to="+to;
		//String url = "http://rate-exchange.appspot.com/currency?from=USD&to=INR";
		//Log.d("ServerCommunication", "ServerCommunication onPostExecute ServerAsycTask: "+urls);
		//Toast.makeText(myContext, "1- "+url, Toast.LENGTH_LONG).show();

		new ServerAsycTask(remoteViews,url).execute(url);
		//new ServerAsycTask().execute(url);
	}

	private class ServerAsycTask extends AsyncTask<String, Void, String> {

		private RemoteViews views;
		String url;

		public ServerAsycTask(RemoteViews views, String url){
			this.views = views;
			this.url = url;
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


			//Toast.makeText(myContext, "result1: "+result+" for url: "+this.url, Toast.LENGTH_SHORT).show();

			try {
				JSONObject obj = new JSONObject(result);
				String str = "1 "+obj.getString("from")+" = "+obj.getString("rate")+" "+obj.getString("to");
				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(myContext);

				ComponentName watchWidget;

				watchWidget = new ComponentName(myContext, MainActivity.class);

				views.setTextViewText(R.id.update, str);

				appWidgetManager.updateAppWidget(watchWidget, views);

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
