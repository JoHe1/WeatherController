package org.ulpgc.dacd.Control;

import org.ulpgc.dacd.Model.Weather;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.ulpgc.dacd.Control.Main.mapIslandLocation;

public class WeatherController {
    public WeatherProvider weatherProvider;
    public WeatherStore weatherStore;

    public WeatherController(WeatherProvider weatherProvider, WeatherStore weatherStore) {
        this.weatherProvider = weatherProvider;
        this.weatherStore = weatherStore;
    }

    public void execute() {
        mapIslandLocation.forEach((island,location) -> {
            try {
                List<Weather> weathers = weatherProvider.get(location);
                weatherStore.save(weathers);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
