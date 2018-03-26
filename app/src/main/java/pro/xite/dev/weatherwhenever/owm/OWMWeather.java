package pro.xite.dev.weatherwhenever.owm;

/**
 * Created by Roman Syrchin on 3/26/18.
 */

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

/**
 * Represents data provided by the OWM service.
 */
public class OWMWeather extends OWMCity implements Serializable {

//    @SerializedName("id")
//    private Integer id;

    @SerializedName("cod")
    private Integer code;

//    @SerializedName("name")
//    private String name;

    @SerializedName("main")
    private Main main;

    public class Main {
        @SerializedName("temp")
        Double temp;
        @SerializedName("pressure")
        Float pressure;
        @SerializedName("humidity")
        Integer humidity;
        @SerializedName("temp_min")
        Double temp_min;
        @SerializedName("temp_max")
        Double temp_max;
    }

    public String getTemp() {
        return String.valueOf(main.temp);
    }

    public String getPressure() {
        return String.valueOf(main.pressure);
    }

    public String getCity() { return name; }

}
