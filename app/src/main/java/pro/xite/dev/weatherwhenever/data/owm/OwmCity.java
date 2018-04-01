package pro.xite.dev.weatherwhenever.data.owm;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import pro.xite.dev.weatherwhenever.data.CityInfo;
import pro.xite.dev.weatherwhenever.data.GeoLocation;
import pro.xite.dev.weatherwhenever.data.WeatherInfo;

/**
 * Created by Roman Syrchin on 3/26/18.
 */

public class OwmCity extends OwmData implements CityInfo {

    @SerializedName("id")
    Integer id;

    @SerializedName("name")
    String name;

    @SerializedName("coord")
    Coord coord;

    private class Coord implements GeoLocation {
        @SerializedName("lat")
        Float lat;
        @SerializedName("lon")
        Float lon;

        @Override
        public float getLatitude() {
            return lat;
        }

        @Override
        public float getLongitude() {
            return lon;
        }
    }

    private class Sys {
        /*  API: "Internal parameters"
        int type;
        int id;
        float message; */
        /** country code, 2 chars */
        String country;
        /** UTC time */
        long sunrise;
        long sunset;
    }

    @SerializedName("sys")
    Sys sys;

    @SerializedName("population")
    Long population;

    public Integer getId() {
        return id;
    }

    @Override
    public String getPlaceId() {
        return null;
    }

    public String getName() {
        return name;
    }

    public String getCountryCode() {
        return sys.country;
    }

    @Override
    public GeoLocation getGeoLocation() {
        return coord;
    }

    @Override
    public WeatherInfo getCurrentWeather() {
        return null;
    }

    @Override
    public WeatherInfo getForecasetOn(Date date) {
        return null;
    }

    public Long getPopulation() {
        return population;
    }

    public OwmCity getOWMCity() { return this; }


    @Override
    public String toString() {
        return String.format("City %s [id%d] of country %s", getName(), getId(), getCountryCode());
    }
}

