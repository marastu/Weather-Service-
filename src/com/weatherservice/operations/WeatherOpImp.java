package com.weatherservice.operations;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.weather.aidl.WeatherCall;
import com.weather.aidl.WeatherData;
import com.weather.aidl.WeatherRequest;
import com.weather.aidl.WeatherResults;
import com.weatherservice.MainActivity;
import com.weatherservice.R;
import com.weatherservice.services.WeatherServiceAsync;
import com.weatherservice.services.WeatherServiceSync;
import com.weatherservice.utils.GenericServiceConnection;
import com.weatherservice.utils.Utils;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class WeatherOpImp implements RetainedOp
{

	private static final int TEN_SECONDS = 10*1000;

	private static final String TAG = WeatherOpImp.class.getSimpleName();

	private WeakReference<MainActivity> mActivityRef;

	/**
	 * This GenericServiceConnection is used to receive results after binding to
	 * the WeatherServiceSync Service using bindService().
	 */
	private GenericServiceConnection<WeatherCall> mServiceConnectionSync;

	/**
	 * This GenericServiceConnection is used to receive results after binding to
	 * the WeatherServiceAsync Service using bindService().
	 */
	private GenericServiceConnection<WeatherRequest> mServiceConnectionAsync;

	private WeakReference<EditText> mEditText;
	
	private WeatherData mlastSearchedWeatherData;
	
	private Map<String, CachedWeatherData> weatherDataCacheMap = 
			new HashMap<String, CachedWeatherData>();
		
	/**
	 * Constructor
	 */
	public WeatherOpImp() {	}

	
	/**
	 * Cache the view fields (e.g., text, mEditText objects).
	 */
	private void initializeViewFields()
	{
		System.out.println("---- initializeViewFields -----");
		
		
		// Store the EditText that holds the urls entered by the user
		// (if any).
		mEditText = new WeakReference<>((EditText) mActivityRef.get()
				.findViewById(R.id.editText1));
		
		if (mlastSearchedWeatherData != null)
		{
			displayWeatherData(mlastSearchedWeatherData);
		}

	}

	/**
	 * (Re)initialize the non-view fields (e.g., GenericServiceConnection
	 * objects).
	 */
	private void initializeNonViewFields()
	{
		mServiceConnectionSync = new GenericServiceConnection<WeatherCall>(
				WeatherCall.class);

		mServiceConnectionAsync = new GenericServiceConnection<WeatherRequest>(
				WeatherRequest.class);
	}

	/**
	 * Initiate the service binding protocol.
	 */
	public void bindService()
	{
		Log.d(TAG, "calling bindService()");

		// Launch the Weather Bound Services if they aren't already
		// running via a call to bindService(), which binds this
		// activity to the WeatherService* if they aren't already
		// bound.
		if (mServiceConnectionSync.getInterface() == null)
			mActivityRef.get().bindService(
					WeatherServiceSync.makeIntent(mActivityRef.get()),
					mServiceConnectionSync, Context.BIND_AUTO_CREATE);

		if (mServiceConnectionAsync.getInterface() == null)
		{
			Log.d(TAG, "bind to mServiceConnectionAsync");
			mActivityRef.get().bindService(
					WeatherServiceAsync.makeIntent(mActivityRef.get()),
					mServiceConnectionAsync, Context.BIND_AUTO_CREATE);
		}
	}

	/**
	 * Initiate the service unbinding protocol.
	 */
	public void unbindService()
	{
		try
		{
			// Unbind the Async Service if it is connected.
			if (mServiceConnectionAsync.getInterface() != null)
				mActivityRef.get().unbindService(mServiceConnectionAsync);
		}
		catch (RuntimeException e)
		{
			// this might happen when the service is not already bound.
			e.printStackTrace();
		}

		try
		{
			// Unbind the Sync Service if it is connected.
			if (mServiceConnectionSync.getInterface() != null)
				mActivityRef.get().unbindService(mServiceConnectionSync);
		}
		catch (RuntimeException e)
		{
			// this might happen when the service is not already bound.
			e.printStackTrace();
		}
	}

	
	@Override
	public void onConfigurationChange(WeakReference<MainActivity> activity)
	{
		mActivityRef = activity;

		initializeViewFields();
		initializeNonViewFields();
	}

	/**
	 * Handle searched Weather Data from Sync / Async Service
	 * 
	 * @param results
	 */
	private void handleSearchedWeatherData(List<WeatherData> results)
	{
		if ((null == results) || results.size() == 0 || null == results.get(0))
		{
			displayInformation("No Result Found");
			return;
		}
		
		// retrieve the result and cache it.
		WeatherData weatherData = results.get(0);
		
		cacheWeatherData(weatherData);
		
		displayWeatherData(weatherData);
	}


	/**
	 * Display the information through the Activity
	 * 
	 * @param info
	 */
	private void displayInformation(String info)
	{
		if ((null == mActivityRef) || (null == mActivityRef.get()))
			return;
		
		MainActivity mainActivity = (MainActivity) mActivityRef.get();
		mainActivity.showToast(info);
	}


	/**
	 * Display the weather Data through the Activity
	 * 
	 * @param weatherData
	 */
	private void displayWeatherData(WeatherData weatherData)
	{
		if ((null == mActivityRef) || (null == mActivityRef.get()))
			return;
		
		MainActivity mainActivity = (MainActivity) mActivityRef.get();
		mainActivity.displayWeatherData(weatherData);
		
	}


	/**
	 * Cache weather data
	 * 
	 * @param weatherData
	 */
	private void cacheWeatherData(WeatherData weatherData)
	{
		String lastSearchedLocation = weatherData.getmSearchedLocation();
		long lastUpdatedTime = weatherData.getmLastUpdated();
		
		CachedWeatherData cachedWeatherData = 
				new CachedWeatherData(lastSearchedLocation, 
						lastUpdatedTime, weatherData);
		
		weatherDataCacheMap.put(lastSearchedLocation, cachedWeatherData);
		
		// setup a reference to the last searched data in the map for 
		// screen refresh
		mlastSearchedWeatherData = weatherData;
	}


	
	/**
	 * This method is to check to see if we need to search updated
	 * data or use the cached data from the previous search. 
	 * 
	 * @param location
	 * @return
	 */
	private boolean isCachedDataStillFreshForLocation(String location)
	{
		CachedWeatherData cachedWeatherData = weatherDataCacheMap.get(location);
		
		// no data currently cached for this location
		if (null == cachedWeatherData)
			return false;
		
		long currentTime = System.currentTimeMillis();
		long lastUpdatedTime = cachedWeatherData.getLastUpdatedTime();
		
		if ((currentTime - lastUpdatedTime) > TEN_SECONDS)
			return false;
			
		return true;
	}


	/*
	 * Initiate the asynchronous weather search when the user presses the
	 * "weather Async" button.
	 */
	public void getWeatherAsync(View v)
	{
		WeatherRequest weatherRequest = mServiceConnectionAsync.getInterface();

		if (weatherRequest != null)
		{
			
			// Get the weather location entered by the user.
			String location = mEditText.get().getText().toString();

			Utils.hideKeyboard(mActivityRef.get(), mEditText.get()
					.getWindowToken());
			
			if (location.length() == 0)
			{
				displayInformation("Please enter a location");
				return;
			}
			
			// check to see if the cached data is still fresh, if so
			// just pull the data from cache.
			if (isCachedDataStillFreshForLocation(location))
			{
				CachedWeatherData cachedData = weatherDataCacheMap.get(location);
				displayWeatherData(cachedData.getCachedWeatherData());
				return;
			}
			
			// otherwise, we fetch from remote server
			try
			{
				// Invoke a one-way AIDL call, which does not block
				// the client. The results are returned via the
				// sendResults() method of the mWeatherResults
				// callback object, which runs in a Thread from the
				// Thread pool managed by the Binder framework.
				weatherRequest.getCurrentWeather(location, mWeatherResults);

			}
			catch (RemoteException e)
			{
				Log.e(TAG, "RemoteException:" + e.getMessage());
			}
		}
		else
		{
			Log.d(TAG, "weatherRequest was null.");
		}
	}

	/**
	 * The implementation of the WeatherResults AIDL Interface, which will be
	 * passed to the Weather Web service using the
	 * WeatherRequest.getCurrentWeather() method.
	 * 
	 * This implementation of WeatherResults.Stub plays the role of Invoker in
	 * the Broker Pattern since it dispatches the up call to sendResults().
	 */
	private WeatherResults.Stub mWeatherResults = new WeatherResults.Stub()
	{
		@Override
		public void sendResults(List<WeatherData> results)
				throws RemoteException
		{
			System.out.println("===== Received Results from Async =====");
			
			handleSearchedWeatherData(results);
		}
	};

	
	/*
	 * Initiate the synchronous weather search when the user presses the
	 * "Weather Sync" button.
	 */
	public void gethWeatherSync(View v)
	{
		final WeatherCall weatherCall = mServiceConnectionSync.getInterface();

		if (weatherCall != null)
		{
			// Get the weather location entered by the user.
			final String location = mEditText.get().getText().toString();

			Utils.hideKeyboard(mActivityRef.get(), mEditText.get()
					.getWindowToken());
			
			if (location.length() == 0)
			{
				displayInformation("Please enter a location");
				return;
			}
			
			// check to see if the cached data is still fresh, if so
			// just pull the data from cache.
			if (isCachedDataStillFreshForLocation(location))
			{
				CachedWeatherData cachedData = weatherDataCacheMap.get(location);
				displayWeatherData(cachedData.getCachedWeatherData());
				return;
			}
			

			// Use AsyncTask for the two-way sync call to avoid block in the UI Thread.
			AsyncTask<String, Void, List<WeatherData>> weatherAsyncTask = 
					new AsyncTask<String, Void, List<WeatherData>>()
			{

				@Override
				protected List<WeatherData> doInBackground(String... params){
			
					List<WeatherData> weatherDataList;

					try
					{
						weatherDataList = weatherCall
								.getCurrentWeather(location);
					}
					catch (RemoteException e)
					{
						e.printStackTrace();
						weatherDataList = new ArrayList<WeatherData>();
					}

					return weatherDataList;
				}

				/**
				 * 
				 */
				@Override
				protected void onPostExecute(List<WeatherData> results)
				{
					System.out.println("===== RECEIVED RESULTS from Sync =====");
					
					handleSearchedWeatherData(results);
				}
			};

			weatherAsyncTask.execute();

		}

		else
		{
			Log.d(TAG, "weatherRequest was null.");
		}
	}
	
	
	
	
	
	/**
	 * This class is used to store the weather data, location and 
	 * the last updated time for caching purpose.
	 * 
	 * @author bwoo
	 *
	 */
	private class CachedWeatherData
	{
		private String location;
		private long lastUpdatedTime;
		private WeatherData cachedWeatherData;
		
		
		/**
		 * Constructor
		 * 
		 * @param location
		 * @param lastUpdatedTime
		 * @param weatherData
		 */
		public CachedWeatherData(String location, long lastUpdatedTime, WeatherData weatherData)
		{
			this.location = location;
			this.lastUpdatedTime = lastUpdatedTime;
			this.cachedWeatherData = weatherData;
		}
		
		
		public long getLastUpdatedTime()
		{
			return lastUpdatedTime;
		}
		public void setLastUpdatedTime(long lastUpdatedTime)
		{
			this.lastUpdatedTime = lastUpdatedTime;
		}
		public WeatherData getCachedWeatherData()
		{
			return cachedWeatherData;
		}
		public void setCachedWeatherData(WeatherData cachedWeatherData)
		{
			this.cachedWeatherData = cachedWeatherData;
		}
		public String getLocation()
		{
			return location;
		}
		public void setLocation(String location)
		{
			this.location = location;
		}	
	}

}
