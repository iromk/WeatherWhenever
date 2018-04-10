package pro.xite.dev.weatherwhenever.data.ods;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import pro.xite.dev.weatherwhenever.data.Weather;
import pro.xite.dev.weatherwhenever.data.Wherever;

/**
 * Created by Roman Syrchin on 4/8/18.
 */
public class OdsResponse implements Serializable {

    @SerializedName("nhits")
    private int hitCount;

    @SerializedName("parameters.q")
    private String requestedCriteria;

    @SerializedName("parameters.rows")
    private String requestedRows;


    public List<OdsCity> getCities() {
        return cities;
    }

    @SerializedName("records")
    private List<OdsCity> cities;

    public class OdsCity implements Wherever {
        @SerializedName("recordid")
        String recordId;

        @SerializedName("fields")
        private Fields fields;

        private class Fields implements Serializable {

            @SerializedName("country")
            String country;
            @SerializedName("ascii_name")
            String asciiName;
            @SerializedName("alternate_names")
            String alternateNames;
            @SerializedName("country_code")
            String countryCode;

        }

        public String getRecordId() {
            return recordId;
        }

        public String getCountry() {
            return fields.country;
        }

        public String getAlternateNames() {
            return fields.alternateNames;
        }

        @Override
        public String getPlaceId() {
            return null;
        }

        @Override
        public String getName() {
            return fields.asciiName;
        }

        @Override
        public String getCountryCode() {
            return fields.countryCode;
        }

        @Override
        public Weather getCurrentWeather() {
            return null;
        }

        @Override
        public Weather getForecasetOn(Date date) {
            return null;
        }

        @Override
        public String toString() {
            return String.format("%s, %s", getName(), fields.country);
        }
    }

}
