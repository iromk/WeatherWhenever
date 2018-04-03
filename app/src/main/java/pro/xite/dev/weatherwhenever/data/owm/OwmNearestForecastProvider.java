package pro.xite.dev.weatherwhenever.data.owm;

import pro.xite.dev.weatherwhenever.manage.LeakSafeHandler;
import pro.xite.dev.weatherwhenever.manage.DataProviderListener;

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

    public void request(String city, DataProviderListener activity) {
        requestOwmService(city, OwmNearestForecast.class,
                new LeakSafeHandler<OwmNearestForecast>(activity));
    }


}
