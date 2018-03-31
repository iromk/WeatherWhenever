package pro.xite.dev.weatherwhenever.owm;

import android.os.Handler;

import pro.xite.dev.weatherwhenever.LeakSafeHandler;
import pro.xite.dev.weatherwhenever.ViewUpdatable;

/**
 * Created by Roman Syrchin on 3/26/18.
 */

public class OwmNearestForecastProvider extends OwmDataProvider {

    /**
     * Api url data identifier name.
     */
    private static final String OWM_ACTION_WEATHER = "forecast";
    private static final String OWM_LANG_RU = "&lang=ru";

    @Override
    protected String getApiAction() { return OWM_ACTION_WEATHER; }

    @Override
    protected String getExtraGetParams() {
        return super.getExtraGetParams() + OWM_LANG_RU;
    }

    /**
     * @deprecated
     * @param city
     * @param handler
     */
    public void request(String city, Handler handler) {
        requestOWM(city, OwmNearestForecast.class, handler);
    }

    public void request(String city, ViewUpdatable activity) {
        requestOWM(city, OwmNearestForecast.class,
                new LeakSafeHandler<OwmNearestForecast>(activity));
    }


}
