package pro.xite.dev.weatherwhenever;

import java.io.Serializable;
import java.util.ArrayList;

import pro.xite.dev.weatherwhenever.data.CityInfo;
import pro.xite.dev.weatherwhenever.data.ForecastInfo;
import pro.xite.dev.weatherwhenever.data.WeatherInfo;
import pro.xite.dev.weatherwhenever.data.owm.OwmCity;
import pro.xite.dev.weatherwhenever.data.owm.OwmNearestForecast;
import pro.xite.dev.weatherwhenever.data.owm.OwmWeather;

/**
 * Created by Roman Syrchin on 3/30/18.
 */
public class RecentCitiesList implements Serializable {

    private static final int MAX_RECENT_CITIES = 5;

    private ArrayList<CityInfo> cities = new ArrayList<>();
    private ArrayList<WeatherInfo> weathers = new ArrayList<>();
    private ArrayList<ForecastInfo> forecasts = new ArrayList<>();
    private int counter = 0;

    public void add(CityInfo city, WeatherInfo weather, ForecastInfo forecast) {
        if(counter == MAX_RECENT_CITIES) {
            cities.remove(0);
            weathers.remove(0);
            forecasts.remove(0);
            counter--;
        }
        counter++;
        cities.add(city);
        weathers.add(weather);
        forecasts.add(forecast);
    }

    public ArrayList<CityInfo> getCities() {
        return cities;
    }

    public CityInfo getLatestCity() {
        return cities.get(cities.size()-1);
    }

    public WeatherInfo getLatestWeather() {
        return weathers.get(weathers.size()-1);
    }

    public ForecastInfo getLatestForecast() {
        return forecasts.get(forecasts.size()-1);
    }

    private int indexForCityByName(String cityName) {
        if(counter > 0) {
            int n = 0;
            for(CityInfo city : cities) {
                if (city.getName().equals(cityName))
                    return n;
                else n++;
            }
        }
        return -1;
    }

    public CityInfo getCity(CharSequence cityName) {
        int n = indexForCityByName(cityName.toString());
        if(n > -1) {
            return cities.get(n);
        }
        return null;//new OwmCity();
    }

    public WeatherInfo getWeatherForCity(CharSequence cityName) {
        int n = indexForCityByName(cityName.toString());
        if(n > -1) {
            return weathers.get(n);
        }
        return null;//new OwmWeather();
    }

    public ForecastInfo getForecastForCity(CharSequence cityName) {
        int n = indexForCityByName(cityName.toString());
        if(n > -1) {
            return forecasts.get(n);
        }
        return null;//new OwmNearestForecast();
    }
}
