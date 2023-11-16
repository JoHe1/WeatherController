package org.ulpgc.dacd.Control;

import org.ulpgc.dacd.Model.Location;
import org.ulpgc.dacd.Model.Weather;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class OpenWeatherMapProvider implements WeatherProvider{
    public String apikey;
    public OpenWeatherMapProvider(String apikey) {
        this.apikey = apikey;
    }

    @Override
    public List<Weather> get(Location location, Instant instant, int dataAmount) throws IOException {
        return null;
    }
}
