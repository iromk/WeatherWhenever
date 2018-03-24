package pro.xite.dev.weatherwhenever;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by Roman Syrchin on 3/24/18.
 */

public class ForecastProvider implements Serializable {

    private static ForecastProvider instance = null;

    private String[] forecasts;
    private String[] tipsOfTheDay;
    private int totalForecasts;
    private int fingerPointingInDaSkyForecast = 0;

    private ForecastProvider(Context context) {
        forecasts = context.getResources().getStringArray(R.array.forecasts_hardcoded_list);
        tipsOfTheDay = context.getResources().getStringArray(R.array.todo_actions_list);
        totalForecasts = forecasts.length;
        // implementation assumes that each forecast has certain related tip.
        if (totalForecasts != tipsOfTheDay.length) throw new AssertionError();
    }

    public static ForecastProvider create(Context context) {
        if(instance == null) {
            instance = new ForecastProvider(context);
        }
        return instance;
    }

    public static ForecastProvider getInstance() {
        if (instance == null) throw new AssertionError();
        return instance;
    }

    /**
     *  If you don't really know... keep looking confident.
     * @return Forecast object
     */
    public static Forecast makeReliableForecast(String city) {
        instance.fingerPointingInDaSkyForecast = (int) (Math.random() * instance.totalForecasts);
        return new Forecast(city,
                instance.forecasts[instance.fingerPointingInDaSkyForecast],
                instance.tipsOfTheDay[instance.fingerPointingInDaSkyForecast]);
    }

}
