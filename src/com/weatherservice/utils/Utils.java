package com.weatherservice.utils;

import java.io.BufferedInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import com.weather.aidl.WeatherData;
import com.weatherservice.JSON.JsonWeather;
import com.weatherservice.JSON.WeatherJSONParser;



import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * @class Utils
 * 
 * @brief Handles the actual downloading of Weather information from the Weather
 *        web service.
 */
public class Utils
{
	/**
	 * Logging tag used by the debugger.
	 */
	private final static String TAG = Utils.class.getCanonicalName();

	/**
	 * URL to the Weather web service.
	 */
	private final static String sWeather_Web_Service_URL = "http://api.openweathermap.org/data/2.5/weather?units=metric&q=";

	private final static String sWeather_Image_Icon_URL = "http://openweathermap.org/img/w/";

	/**
	 * Obtain the Weather information.
	 * 
	 * @return The information that responds to your current weather search.
	 */

	public static List<WeatherData> getResults(Context context,
			final String location)
	{
		// Create a List that will return the WeatherData obtained
		// from the Weather Service web service.
		final List<WeatherData> returnList = new ArrayList<WeatherData>();

		// A List of JsonWeather objects.
		List<JsonWeather> jsonWeathers = null;

		try
		{
			String encodedLocation = URLEncoder.encode(location, "UTF-8");

			// Append the location to create the full URL.
			final URL url = new URL(sWeather_Web_Service_URL + encodedLocation);

			// Opens a connection to the Weather Service.
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			// Sends the GET request and reads the Json results.
			try (InputStream in = new BufferedInputStream(
					urlConnection.getInputStream()))
			{
				// Create the parser.
				final WeatherJSONParser parser = new WeatherJSONParser();

				// Parse the Json results and create JsonWeather data
				// objects.
				jsonWeathers = parser.parseJsonStream(in);
				System.out.println("----- finished parsing ----");
			}
			finally
			{
				urlConnection.disconnect();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if (jsonWeathers != null && jsonWeathers.size() > 0)
		{
			// Convert the JsonWeather data objects to our WeatherData object,
			// which can be passed between processes.
			for (JsonWeather jsonWeather : jsonWeathers)
			{

				if (jsonWeather.getWeather().size() == 0)
				{
					Log.d(TAG, "Location " + location + " not found!");
					break;
				}

				// extra credit: downloading the icon for the app.
				// reused image download utils from assignment #1.
				String directoryPathname = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
						+ "/";

				String imageToDownload = sWeather_Image_Icon_URL
						+ jsonWeather.getWeather().get(0).getIcon() + ".png";

				Log.d(TAG, "---- Downloading Image: " + imageToDownload);

				Uri uri = Uri.parse(imageToDownload);

				Uri downloadedImageUri = com.weatherservice.utils.ImageUtils.downloadImage(
						context, uri, directoryPathname);

				String downloadImageUriString = null;
				if (downloadedImageUri != null)
					downloadImageUriString = downloadedImageUri.toString();
				else
					Log.d(TAG, "---- Image download failed: " + imageToDownload);

				// check to see if the city name is missing, if so, just 
				// display the country name
				String cityCountry;
				if (jsonWeather.getName().equals(""))
					cityCountry = jsonWeather.getSys().getCountry();
				else
					cityCountry = jsonWeather.getName() + ", " + 
							jsonWeather.getSys().getCountry();
					
				
				returnList.add(new WeatherData(cityCountry, jsonWeather
						.getWind().getSpeed(), jsonWeather.getWind().getDeg(),
						jsonWeather.getMain().getTemp(),jsonWeather.getMain().getTempMin(),jsonWeather.getMain().getTempMax(), jsonWeather.getMain()
								.getHumidity(), jsonWeather.getSys()
								.getSunrise(),
						jsonWeather.getSys().getSunset(), jsonWeather
								.getWeather().get(0).getIcon(), jsonWeather
								.getWeather().get(0).getMain(), jsonWeather
								.getWeather().get(0).getDescription(), 
						downloadImageUriString, location, System.currentTimeMillis()));

			}
			// Return the List of WeatherData.
			return returnList;
		}
		else
			return null;
	}
	
	
	public static String getDateTimeFromMs(long milliseconds)
	{
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date(milliseconds);
		return dateFormat.format(date);		
	}
	

	/**
	 * This method is used to hide a keyboard after a user has finished typing
	 * the url.
	 */
	public static void hideKeyboard(Activity activity, IBinder windowToken)
	{
		InputMethodManager mgr = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(windowToken, 0);
	}

	/**
	 * Show a toast message.
	 */
	public static void showToast(Context context, String message)
	{
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Ensure this class is only used as a utility.
	 */
	private Utils()
	{
		throw new AssertionError();
	}

	
	public static String convertLongToTime(long input)
	{
		Date date = new Date(input*1000L); // *1000 is to convert seconds to milliseconds
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm z"); // the format of your date
		sdf.setTimeZone(TimeZone.getDefault()); // give a timezone reference for formating (see comment at the bottom
		String formattedDate = sdf.format(date);
		
		return formattedDate;
	}
}
