package com.weatherservice;

import com.weather.aidl.WeatherData;
import com.weatherservice.operations.WeatherOpImp;
import com.weatherservice.utils.RetainedFragmentManager;
import com.weatherservice.utils.Utils;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.weatherservice.R;
/**
 * 
 * @author 
 *
 */
public class MainActivity extends Activity {

	private static final String TAG_TASK_FRAGMENT = "task_fragment";
	private com.weatherservice.utils.RetainedFragmentManager mTaskFragment;
	
	private WeatherOpImp mWeatherOpsImp;

	private TextView mTextViewCity;
	private TextView mTextViewDesc;
	private TextView mTextViewTemp;
	private TextView mTextViewMinTemp;
	private TextView mTextViewMaxTemp;
	private TextView mTextViewWind;
	private TextView mTextViewHumidity;
	private TextView mTextViewSunrise;
	private TextView mTextViewSunset;
	private ImageView mImageViewWeatherIcon;
	private TextView mTextViewLastUpdatedTime;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// caching the UI components
		mTextViewCity = (TextView) findViewById(R.id.textViewCity);
		mTextViewDesc = (TextView) findViewById(R.id.textViewDesc);
		mTextViewTemp = (TextView) findViewById(R.id.textViewTemp);
		mTextViewMinTemp = (TextView) findViewById(R.id.textViewMinTemp);
		mTextViewMaxTemp = (TextView) findViewById(R.id.textViewMaxTemp);
		mTextViewWind = (TextView) findViewById(R.id.textViewWind);
		mTextViewHumidity = (TextView) findViewById(R.id.textViewHumidity);
		mTextViewSunrise = (TextView) findViewById(R.id.textViewSunrise);
		mTextViewSunset = (TextView) findViewById(R.id.textViewSunset);
		mImageViewWeatherIcon = (ImageView) findViewById(R.id.imageViewWeatherIcon);
		mTextViewLastUpdatedTime = (TextView) findViewById(R.id.textViewLastUpdated);
		
		handleConfigurationChanges();
	}

	/**
	 * Hook method called after onCreate() or after onRestart() (when the
	 * activity is being restarted from stopped state).
	 */
	@Override
	protected void onStart()
	{
		// Always call super class for necessary
		// initialization/implementation.
		super.onStart();

		// Initiate the service binding protocol.
		mWeatherOpsImp.bindService();
	}

	
    /**
     * Hook method called by Android when this Activity becomes
     * invisible.
     */
    @Override
    protected void onStop() {
        // Unbind from the Service.
    	mWeatherOpsImp.unbindService();

        // Always call super class for necessary operations when
        // stopping.
        super.onStop();
    }
    
	
	/**
	 * Handle hardware reconfigurations, such as rotating the display.
	 */
	protected void handleConfigurationChanges()
	{
		FragmentManager fm = getFragmentManager();
		mTaskFragment = (RetainedFragmentManager) fm
				.findFragmentByTag(TAG_TASK_FRAGMENT);
		
		// If the Fragment is non-null, then it is currently being
		// retained across a configuration change.
		if (mTaskFragment == null)
		{
			mTaskFragment = new RetainedFragmentManager();
			
			// add the tasks to the RetainedFragmentManager
			
			
			mWeatherOpsImp = new WeatherOpImp();
			mTaskFragment.put("WeatherOpsImp", mWeatherOpsImp);
			
			fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT)
					.commit();
		}
		
		mWeatherOpsImp = (WeatherOpImp) mTaskFragment.get("WeatherOpsImp");	
		
		// In the case where Android destroys background activities, 
		// the mWeatherOpsImp seems to get destroyed as well.
		// we need to recreate the task and add that to the fragment.
		// this does not happen very often but it could happen when
		// the "Don't keep activities" in Developer Settings is selected. 
		if (null == mWeatherOpsImp)
		{
			mWeatherOpsImp = new WeatherOpImp();
			mTaskFragment.put("WeatherOpsImp", mWeatherOpsImp);
		}

	}

	

	/*
	 * Initiate the Asynchronous weather lookup when the user presses the
	 * "Weather Async" button.
	 */
	public void getWeatherAsync(View v)
	{
		mWeatherOpsImp.getWeatherAsync(v);
	}

	
	/*
	 * Initiate the synchronous weather lookup when the user presses the
	 * "Weather Sync" button.
	 */
	public void getWeatherSync(View v)
	{
		mWeatherOpsImp.gethWeatherSync(v);
	}
	
	
	/**
	 * Show Toast on the screen to show any informational message on UI Thread
	 * 
	 * @param message
	 */
	public void showToast(final String message)
	{
		runOnUiThread(new Runnable()
		{
			
			@Override
			public void run()
			{
				Toast toastMsg = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
				toastMsg.show();
			}
		});
	}
	
	/**
	 *  Display Weather Data on UI Thread
	 * @param weatherData
	 */
	public void displayWeatherData(final WeatherData weatherData)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				mTextViewCity.setText(weatherData.getmName());
				mTextViewDesc.setText(weatherData.getmWeatherDesc());
				
				String humidity = "Humidity: " + Long.toString(weatherData.getmHumidity()) + "%";
				mTextViewHumidity.setText(humidity);
				
				String sunrise = "Sunrise: " + Utils.convertLongToTime(weatherData.getmSunrise());
				mTextViewSunrise.setText(sunrise);
				
				String sunset = "Sunset: " + Utils.convertLongToTime(weatherData.getmSunset());
				mTextViewSunset.setText(sunset);
				
				String temp = (int) weatherData.getmTemp() + " C";
				mTextViewTemp.setText(temp);
				String Mintemp ="Min Temp: " + (int) weatherData.getmMinTemp() + " C";
				mTextViewMinTemp.setText(Mintemp);
				String Maxtemp = "Max Temp: " +(int) weatherData.getmMaxTemp() + " C";
				mTextViewMaxTemp.setText(Maxtemp);
				
				String windString = "Wind: " + (int) weatherData.getmSpeed() + " km/h";
				mTextViewWind.setText(windString);
				
				String imageLocation = weatherData.getmImageDownloadedLocation();
				Uri imageUri = Uri.parse(imageLocation);
				mImageViewWeatherIcon.setImageURI(null);
				mImageViewWeatherIcon.setImageURI(imageUri);
				mImageViewWeatherIcon.invalidate();
				
				long lastUpdatedTime = weatherData.getmLastUpdated();
				String lastUpdatedTimeString = "Last Updated at " + Utils.getDateTimeFromMs(lastUpdatedTime);
				mTextViewLastUpdatedTime.setText(lastUpdatedTimeString);
				
			}
		});
	}
	

}
