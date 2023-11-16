package org.ulpgc.dacd.Control;

public class WeatherController {
    public WeatherProvider weatherProvider;
    public WeatherStore weatherStore;

    public WeatherController(WeatherProvider weatherProvider, WeatherStore weatherStore) {
        this.weatherProvider = weatherProvider;
        this.weatherStore = weatherStore;
    }

    public void execute() {
    }
}
