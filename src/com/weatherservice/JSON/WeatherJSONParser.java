package com.weatherservice.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

/**
 * Parses the Json weather data returned from the Weather Services API and
 * returns a List of JsonWeather objects that contain this data.
 */
public class WeatherJSONParser
{
	/**
	 * Used for logging purposes.
	 */
	private final String TAG = this.getClass().getCanonicalName();

	/**
	 * Parse the @a inputStream and convert it into a List of JsonWeather
	 * objects.
	 */
	public List<JsonWeather> parseJsonStream(InputStream inputStream)
			throws IOException
	{

		// Create a JsonReader for the inputStream.
		try (JsonReader reader = new JsonReader(new InputStreamReader(
				inputStream, "UTF-8")))
		{
			Log.d(TAG, "Parsing the results returned as an array");

			// Handle the array returned from the Weather Service.
			return parseWeatherMessage(reader);
		}
	}

	

	public List<JsonWeather> parseWeatherMessage(JsonReader reader)
			throws IOException
	{

		List<JsonWeather> weathers = new ArrayList<JsonWeather>();
		reader.beginObject();

		try
		{
			// If the weather wasn't expanded return null;
			if (reader.peek() == JsonToken.END_OBJECT)
				return null;
			
			JsonWeather jsonWeather = new JsonWeather();
			weathers.add(jsonWeather);
			
			while (reader.hasNext())
			{
				String name = reader.nextName();
				switch (name)
				{
				case JsonWeather.NAME_JSON:
					Log.d(TAG, "reading name field");
					String cityName = reader.nextString();
					jsonWeather.setName(cityName);
					Log.d(TAG, "reading cityname " + jsonWeather.getName());
					break;
				case JsonWeather.MAIN_JSON:
					Log.d(TAG, "reading main field");
					Main main = parseMain(reader);
					jsonWeather.setMain(main);
					break;
				case JsonWeather.SYS_JSON:
					Log.d(TAG, "reading sys field");
					Sys sys = parseSys(reader);
					jsonWeather.setSys(sys);
					break;					
				case JsonWeather.WIND_JSON:
					Log.d(TAG, "reading wind field");
					Wind wind = parseWind(reader);
					jsonWeather.setWind(wind);					
					break;					
				case JsonWeather.WEATHER_JSON:
					Log.d(TAG, "reading weather field");
					if (reader.peek() == JsonToken.BEGIN_ARRAY)
					{
						List<Weather> weatherList = parseWeatherArray(reader);
						jsonWeather.setWeather(weatherList);
					}
					break;
				default:
					reader.skipValue();
					Log.d(TAG, "Skipping " + name + " field");
					break;
				}				
			}
		}
		finally
		{
			reader.endObject();
		}
	
		return weathers;
	}

	/**
	 * Parse a Json stream and convert it into a List of JsonWeather objects.
	 */
	public List<Weather> parseWeatherArray(JsonReader reader)
			throws IOException
	{

		Log.d(TAG, "reading weather elements");

		reader.beginArray();

		try
		{
			List<Weather> weathers = new ArrayList<Weather>();

			while (reader.hasNext())
				weathers.add(parseWeather(reader));

			return weathers;
		}
		finally
		{
			reader.endArray();
		}
	}

	/**
	 * Parse a Json stream and return a JsonWeather object.
	 */
	public Weather parseWeather(JsonReader reader) throws IOException
	{

		reader.beginObject();

		Weather weather = new Weather();
		try
		{
			while (reader.hasNext())
			{
				String name = reader.nextName();
				switch (name)
				{
				case JsonWeather.WEATHER_MAIN_JSON:
					weather.setMain(reader.nextString());
					Log.d(TAG, "reading weather main " + weather.getMain());
					break;
				case JsonWeather.WEATHER_DESC_JSON:
					weather.setDescription(reader.nextString());
					Log.d(TAG, "reading desc " + weather.getDescription());
					break;
				case JsonWeather.WEATHER_ICON_JSON:
					weather.setIcon(reader.nextString());
					Log.d(TAG, "reading icon " + weather.getIcon());
					break;
				default:
					reader.skipValue();
					Log.d(TAG, "ignoring " + name);
					break;
				}
			}
		}
		finally
		{
			reader.endObject();
		}
		return weather;
	}

	
	
	/**
	 * Parse a Json stream and return a JsonWeather object.
	 */
	public Wind parseWind(JsonReader reader) throws IOException
	{

		reader.beginObject();

		Wind wind = new Wind();
		try
		{
			while (reader.hasNext())
			{
				String name = reader.nextName();
				switch (name)
				{
				case JsonWeather.WIND_DEG_JSON:
					wind.setDeg(reader.nextDouble());
					Log.d(TAG, "reading wind deg " + wind.getDeg());
					break;
				case JsonWeather.WIND_SPEED_JSON:
					wind.setSpeed(reader.nextDouble());
					Log.d(TAG, "reading wind speed " + wind.getSpeed());
					break;
				default:
					reader.skipValue();
					Log.d(TAG, "ignoring " + name);
					break;
				}
			}
		}
		finally
		{
			reader.endObject();
		}
		return wind;
	}
	
	
	/**
	 * Parse a Json stream and return a JsonWeather object.
	 */
	public Sys parseSys(JsonReader reader) throws IOException
	{

		reader.beginObject();

		Sys sys = new Sys();
		try
		{
			while (reader.hasNext())
			{
				String name = reader.nextName();
				switch (name)
				{
				case JsonWeather.SYS_SUNRISE_JSON:
					sys.setSunrise(reader.nextLong());
					Log.d(TAG, "reading sunrise " + sys.getSunrise());
					break;
				case JsonWeather.SYS_SUNSET_JSON:
					sys.setSunset(reader.nextLong());
					Log.d(TAG, "reading sunset " + sys.getSunset());
					break;
				case JsonWeather.SYS_COUNTRY_JSON:
					sys.setCountry(reader.nextString());
					Log.d(TAG, "reading country " + sys.getCountry());
					break;					
				default:
					reader.skipValue();
					Log.d(TAG, "ignoring " + name);
					break;
				}
			}
		}
		finally
		{
			reader.endObject();
		}
		return sys;
	}
	
	
	
	/**
	 * Parse a Json stream and return a JsonWeather object.
	 */
	public Main parseMain(JsonReader reader) throws IOException
	{

		reader.beginObject();

		Main main = new Main();
		try
		{
			while (reader.hasNext())
			{
				String name = reader.nextName();
				switch (name)
				{
				case JsonWeather.TEMP_JSON:
					main.setTemp(reader.nextDouble());
					Log.d(TAG, "reading temp " + main.getTemp());
					break;
				case JsonWeather.TEMPMIN_JSON:
					main.setTempMin(reader.nextDouble());
					Log.d(TAG, "reading tempmin " + main.getTempMin());
					break;
				case JsonWeather.TEMPMAX_JSON:
					main.setTempMax(reader.nextDouble());
					Log.d(TAG, "reading tempmax " + main.getTempMax());
					break;
				case JsonWeather.HUMIDITY_JSON:
					main.setHumidity(reader.nextInt());
					Log.d(TAG, "reading humidity " + main.getHumidity());
					break;					
				default:
					reader.skipValue();
					Log.d(TAG, "ignoring " + name);
					break;
				}
			}
		}
		finally
		{
			reader.endObject();
		}
		return main;
	}
}
