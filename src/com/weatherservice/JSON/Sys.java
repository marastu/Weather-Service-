
package com.weatherservice.JSON;

import java.util.HashMap;
import java.util.Map;

public class Sys {

   
    private String country;
    private Long sunrise;
    private Long sunset;
    

    

    /**
     * 
     * @return
     *     The country
     */
    public String getCountry() {
        return country;
    }

    /**
     * 
     * @param country
     *     The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

  

    /**
     * 
     * @return
     *     The sunrise
     */
    public Long getSunrise() {
        return sunrise;
    }

    /**
     * 
     * @param sunrise
     *     The sunrise
     */
    public void setSunrise(Long sunrise) {
        this.sunrise = sunrise;
    }

    

    /**
     * 
     * @return
     *     The sunset
     */
    public Long getSunset() {
        return sunset;
    }

    /**
     * 
     * @param sunset
     *     The sunset
     */
    public void setSunset(Long sunset) {
        this.sunset = sunset;
    }

   

}
