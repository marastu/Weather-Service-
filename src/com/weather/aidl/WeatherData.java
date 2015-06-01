package com.weather.aidl;

import java.security.acl.LastOwnerException;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is a Plain Old Java Object (POJO) used for data
 * transport within the WeatherService app.  This POJO implements the
 * Parcelable interface to enable IPC between the WeatherActivity and
 * the WeatherServiceSync and WeatherServiceAsync. It represents the
 * response Json obtained from the Open Weather Map API, e.g., a call
 * to http://api.openweathermap.org/data/2.5/weather?q=Nashville,TN
 * might return the following Json data:
 * 
 * { "coord":{ "lon":-86.78, "lat":36.17 }, "sys":{ "message":0.0138,
 * "country":"United States of America", "sunrise":1431427373,
 * "sunset":1431477841 }, "weather":[ { "id":802, "main":"Clouds",
 * "description":"scattered clouds", "icon":"03d" } ],
 * "base":"stations", "main":{ "temp":289.847, "temp_min":289.847,
 * "temp_max":289.847, "pressure":1010.71, "sea_level":1035.76,
 * "grnd_level":1010.71, "humidity":76 }, "wind":{ "speed":2.42,
 * "deg":310.002 }, "clouds":{ "all":36 }, "dt":1431435983,
 * "id":4644585, "name":"Nashville", "cod":200 }
 *
 * Parcelable defines an interface for marshaling/de-marshaling
 * https://en.wikipedia.org/wiki/Marshalling_(computer_science)
 * to/from a format that Android uses to allow data transport between
 * processes on a device.  Discussion of the details of Parcelable is
 * outside the scope of this assignment, but you can read more at
 * https://developer.android.com/reference/android/os/Parcelable.html.
 */
public class WeatherData implements Parcelable {
    /*
     * These data members are the local variables that will store the
     * WeatherData's state
     */
    private String mName;
    private double mSpeed;
    private double mDeg;
    private double mTemp;
    private double mMinTemp;
    private double mMaxTemp;
    private long mHumidity;
    private long mSunrise;
    private long mSunset;
    private String mImageDownloadedLocation;
    private String mIconCode;
    private String mWeatherMainDesc;
    private String mWeatherDesc;
    private String mSearchedLocation;
    private long mLastUpdated;

    /**
     * Constructor
     * 
     * @param name
     * @param speed
     * @param deg
     * @param temp
     * @param humidity
     * @param sunrise
     * @param sunset
     */
    public WeatherData(String name,
                       double speed,
                       double deg,
                       double temp,
                       double mintemp,
                       double maxtemp,
                       long humidity,
                       long sunrise,
                       long sunset,
                       String iconCode,
                       String weatherMainDesc,
                       String weatherDesc,
                       String imageDownloadedLocation,
                       String searchedLocation,
                       long lastUpdated) {
        mName = name;
        mSpeed = speed;
        mDeg = deg;
        mTemp = temp;
        mMinTemp = mintemp;
        mMaxTemp = maxtemp;
        mHumidity = humidity;
        mSunrise = sunrise;
        mSunset = sunset;
        mIconCode = iconCode;
        mWeatherMainDesc = weatherMainDesc;
        mWeatherDesc = weatherDesc;
        mImageDownloadedLocation = imageDownloadedLocation;
        mSearchedLocation = searchedLocation;
        mLastUpdated = lastUpdated;
    }

    /**
     * Provides a printable representation of this object.
     */
    @Override
    public String toString() {
        return "WeatherData [name=" + mName 
            + ", speed=" + mSpeed
            + ", deg=" + mDeg 
            + ", temp=" + mTemp 
            + ", mintemp=" + mMinTemp 
            + ", maxtemp=" + mMaxTemp 
            + ", humidity=" + mHumidity 
            + ", sunrise=" + mSunrise 
            + ", sunset=" + mSunset 
            + ", weatherMainDesc=" + mWeatherMainDesc
            + ", weatherDesc=" + mWeatherDesc
            + ", imageDownloadedLocation=" + mImageDownloadedLocation
            + ", searchedLocation=" + mSearchedLocation
            + ", lastUpdated=" + mLastUpdated
            + ", iconCode=" + mIconCode + "]";
    }

    /*
     * BELOW THIS is related to Parcelable Interface.
     */

    /**
     * A bitmask indicating the set of special object types marshaled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write this instance out to byte contiguous memory.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeDouble(mSpeed);
        dest.writeDouble(mDeg);
        dest.writeDouble(mTemp);
        dest.writeDouble(mMinTemp);
        dest.writeDouble(mMaxTemp);
        dest.writeLong(mHumidity);
        dest.writeLong(mSunrise);
        dest.writeLong(mSunset);
        dest.writeString(mImageDownloadedLocation);
        dest.writeString(mWeatherMainDesc);
        dest.writeString(mWeatherDesc);
        dest.writeString(mSearchedLocation);
        dest.writeLong(mLastUpdated);
        dest.writeString(mIconCode);
    }

    /**
     * Private constructor provided for the CREATOR interface, which
     * is used to de-marshal an WeatherData from the Parcel of data.
     * <p>
     * The order of reading in variables HAS TO MATCH the order in
     * writeToParcel(Parcel, int)
     *
     * @param in
     */
    private WeatherData(Parcel in) {
        mName = in.readString();
        mSpeed = in.readDouble();
        mDeg = in.readDouble();
        mTemp = in.readDouble();
        mMinTemp = in.readDouble();
        mMaxTemp = in.readDouble();
        mHumidity = in.readLong();
        mSunrise = in.readLong();
        mSunset = in.readLong();
        mImageDownloadedLocation = in.readString();
        mWeatherMainDesc = in.readString();
        mWeatherDesc = in.readString();
        mSearchedLocation = in.readString();
        mLastUpdated = in.readLong();
        mIconCode = in.readString();
    }


	/**
     * public Parcelable.Creator for WeatherData, which is an
     * interface that must be implemented and provided as a public
     * CREATOR field that generates instances of your Parcelable class
     * from a Parcel.
     */
    public static final Parcelable.Creator<WeatherData> CREATOR =
        new Parcelable.Creator<WeatherData>() {
            public WeatherData createFromParcel(Parcel in) {
                return new WeatherData(in);
            }

            public WeatherData[] newArray(int size) {
                return new WeatherData[size];
            }
        };
        

	public String getmImageDownloadedLocation()
	{
		return mImageDownloadedLocation;
	}

	public String getmName()
	{
		return mName;
	}

	public double getmSpeed()
	{
		return mSpeed;
	}

	public double getmDeg()
	{
		return mDeg;
	}
	public double getmTemp()
	{
		return mTemp;
	}

	public double getmMinTemp()
	{
		return mMinTemp;
	}

	public double getmMaxTemp()
	{
		return mMaxTemp;
	}

	
	public long getmHumidity()
	{
		return mHumidity;
	}

	public long getmSunrise()
	{
		return mSunrise;
	}

	public long getmSunset()
	{
		return mSunset;
	}

	public String getmWeatherMainDesc()
	{
		return mWeatherMainDesc;
	}

	public String getmWeatherDesc()
	{
		return mWeatherDesc;
	}

	public long getmLastUpdated()
	{
		return mLastUpdated;
	}

	public String getmSearchedLocation()
	{
		return mSearchedLocation;
	}
	
	
}
