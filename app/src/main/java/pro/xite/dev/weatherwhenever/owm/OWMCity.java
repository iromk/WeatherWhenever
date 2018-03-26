package pro.xite.dev.weatherwhenever.owm;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Roman Syrchin on 3/26/18.
 */

public class OWMCity implements Serializable {

    @SerializedName("id")
    Integer id;

    @SerializedName("name")
    String name;

    @SerializedName("coord")
    Coord coord;

    private class Coord {
        @SerializedName("lat")
        Float lat;
        @SerializedName("lon")
        Float lon;
    }

    @SerializedName("country")
    String country;

    @SerializedName("population")
    Long population;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coord getCoord() {
        return coord;
    }

    public String getCountry() {
        return country;
    }

    public Long getPopulation() {
        return population;
    }

    @Override
    public String toString() {
        return String.format("City %s [id%d] of country %s", getName(), getId(), getCountry());
    }
}

