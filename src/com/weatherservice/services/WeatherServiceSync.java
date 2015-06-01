/**
 * 
 */
package com.weatherservice.services;


import java.util.List;

import com.weather.aidl.WeatherCall;
import com.weather.aidl.WeatherData;
import com.weatherservice.utils.Utils;



import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;


/**
 * @author bwoo
 *
 */
public class WeatherServiceSync extends LifecycleLoggingService
{

    /**
     * Factory method that makes an Intent used to start the
     * WeatherServiceSync when passed to bindService().
     * 
     * @param context
     *            The context of the calling component.
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context,
        		WeatherServiceSync.class);
    }

    
    /**
     * Called when a client (e.g., WeatherActivity) calls
     * bindService() with the proper Intent.  Returns the
     * implementation of WeatherCall, which is implicitly cast as an
     * IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mWeatherCallImpl;
    }

    /**
     * The concrete implementation of the AIDL Interface WeatherCall,
     * which extends the Stub class that implements WeatherCall,
     * thereby allowing Android to handle calls across process
     * boundaries.  This method runs in a separate Thread as part of
     * the Android Binder framework.
     * 
     * This implementation plays the role of Invoker in the Broker
     * Pattern.
     */
    WeatherCall.Stub mWeatherCallImpl = new WeatherCall.Stub() {
            /**
             * Implement the AIDL WeatherCall getCurrentWeather() method,
             * which forwards to Utils getResults() to obtain
             * the results from the Weather Web service and then
             * returns the results back to the Activity.
             */
			@Override
			public List<WeatherData> getCurrentWeather(String weather)
					throws RemoteException
			{
				Log.d(TAG, "----- WeatherServiceSYNC.getCurrentWeather() -----");
				
				List<WeatherData> dataList = Utils.getResults(getApplicationContext(), weather);
				
				if (dataList != null)
				{
					for (WeatherData data : dataList)
					{
						System.out.println("Weather Data = " + data.toString());
					}
				}
				
				return dataList;
			}
	};
    
}
