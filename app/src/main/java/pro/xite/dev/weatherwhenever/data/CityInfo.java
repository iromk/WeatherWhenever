package pro.xite.dev.weatherwhenever.data;

import java.util.Date;

/**
 * Created by Roman Syrchin on 3/31/18.
 */
public interface CityInfo {

    String getPlaceId();
    String getName();
    String getCountryCode();
    GeoLocation getGeoLocation();

    WeatherInfo getCurrentWeather();
    WeatherInfo getForecasetOn(Date date);
}
