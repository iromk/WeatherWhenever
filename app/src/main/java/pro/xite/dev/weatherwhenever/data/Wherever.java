package pro.xite.dev.weatherwhenever.data;

import java.util.Date;

/**
 * Created by Roman Syrchin on 3/31/18.
 */
public interface Wherever {

    String getPlaceId();
    String getName();
    String getCountryCode();

    Weather getCurrentWeather();
    Weather getForecasetOn(Date date);
}
