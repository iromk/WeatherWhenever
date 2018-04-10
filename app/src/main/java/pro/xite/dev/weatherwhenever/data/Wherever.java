package pro.xite.dev.weatherwhenever.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Roman Syrchin on 3/31/18.
 */
public interface Wherever extends Serializable {

    String getPlaceId();
    String getName();
    String getCountryCode();

    Weather getCurrentWeather();
    Weather getForecasetOn(Date date);
}
