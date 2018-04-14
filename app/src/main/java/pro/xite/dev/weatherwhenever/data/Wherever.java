package pro.xite.dev.weatherwhenever.data;

import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Roman Syrchin on 3/31/18.
 */
public interface Wherever extends Serializable {

    String getPlaceId();
    String getName();
    String getCountryCode();
    String getCountryName();
    TimeZone getTimezone();

    Weather getCurrentWeather();
    Weather getForecasetOn(Date date);

}
