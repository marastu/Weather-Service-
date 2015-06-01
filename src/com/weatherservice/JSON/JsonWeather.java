
package com.weatherservice.JSON;

import java.util.ArrayList
;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonWeather {

	protected static final String WEATHER_JSON = "weather"; 
	protected static final String MAIN_JSON = "main";
	protected static final String WIND_JSON = "wind";
	protected static final String SYS_JSON = "sys";
	
	protected static final String TEMP_JSON = "temp";
	protected static final String TEMPMIN_JSON = "temp_min";
	protected static final String TEMPMAX_JSON = "temp_max";
	protected static final String HUMIDITY_JSON = "humidity";
	
	protected static final String WEATHER_MAIN_JSON = "main";
	protected static final String WEATHER_DESC_JSON = "description";
	protected static final String WEATHER_ICON_JSON = "icon";
	
	protected static final String WIND_SPEED_JSON = "speed";
	protected static final String WIND_DEG_JSON = "deg";
	
	protected static final String SYS_SUNRISE_JSON = "sunrise";
	protected static final String SYS_SUNSET_JSON = "sunset";
	protected static final String SYS_COUNTRY_JSON = "country";
	
	protected static final String NAME_JSON = "name";
	
	
  
    private Sys sys;
    private List<Weather> weather = new ArrayList<Weather>();
   
    private Main main;
    private Wind wind;
    private Integer id;
    private String name;
 

   
    /**
     * 
     * @return
     *     The sys
     */
    public Sys getSys() {
        return sys;
    }

    /**
     * 
     * @param sys
     *     The sys
     */
    public void setSys(Sys sys) {
        this.sys = sys;
    }

    
    /**
     * 
     * @return
     *     The weather
     */
    public List<Weather> getWeather() {
        return weather;
    }

    /**
     * 
     * @param weather
     *     The weather
     */
    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

   
   
    /**
     * 
     * @return
     *     The main
     */
    public Main getMain() {
        return main;
    }

    /**
     * 
     * @param main
     *     The main
     */
    public void setMain(Main main) {
        this.main = main;
    }

    
    /**
     * 
     * @return
     *     The wind
     */
    public Wind getWind() {
        return wind;
    }

    /**
     * 
     * @param wind
     *     The wind
     */
    public void setWind(Wind wind) {
        this.wind = wind;
    }

    

   
    

   

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

   
    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

   


   
}
