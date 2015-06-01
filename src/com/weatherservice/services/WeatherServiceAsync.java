/**
 * 
 */
package com.weatherservice.services;



import java.util.List;

import com.weather.aidl.WeatherData;
import com.weather.aidl.WeatherRequest;
import com.weather.aidl.WeatherResults;
import com.weatherservice.utils.Utils;



import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * 
 * @author marastu
 *
 */
public class WeatherServiceAsync extends LifecycleLoggingService
{
	
	
    /**
     * Factory method that makes an Intent used to start the
     * WeatherServiceAsync when passed to bindService().
     * 
     * @param context
     *            The context of the calling component.
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context,
        		WeatherServiceAsync.class);
    }
    
    
    /**
     * Called when a client (e.g., MyActivity) calls
     * bindService() with the proper Intent.  Returns the
     * implementation of WeatherRequest, which is implicitly cast as
     * an IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mWeatherRequestImpl;
    }
    
    
    /**
     * The concrete implementation of the AIDL Interface
     * WeatherRequest, which extends the Stub class that implements
     * WeatherRequest, thereby allowing Android to handle calls across
     * process boundaries.  This method runs in a separate Thread as
     * part of the Android Binder framework.
     * 
     * This implementation plays the role of Invoker in the Broker
     * Pattern.
     */
    WeatherRequest.Stub mWeatherRequestImpl = new WeatherRequest.Stub() 
    {
            /**
             * Implement the AIDL WeatherRequest getCurrentWeather()
             * method, which forwards to Utils getResults() to
             * obtain the results from the Weather Web service and
             * then sends the results back to the Activity via a
             * callback.
             */
			@Override
			public void getCurrentWeather(String weatherLocation, WeatherResults results)
					throws RemoteException
			{
				Log.d(TAG, "----- WeatherServiceAsync.getCurrentWeather() -----");
				
				List<WeatherData> dataList = Utils.getResults(getApplicationContext(), weatherLocation);
				
				if (dataList != null)
				{
					for (WeatherData data : dataList)
					{
						System.out.println("Weather Data = " + data.toString());
					}
				}
				
				results.sendResults(dataList);
				
			}
	};
}
