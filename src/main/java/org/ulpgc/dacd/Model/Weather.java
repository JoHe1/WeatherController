package org.ulpgc.dacd.Model;

import java.time.Instant;

public class Weather {
    public double temp;
    public double precipitation;
    public double humidity;
    public double cloud;
    public double wind_velocity;
    public Instant ts;
    public Location location;

    public Weather(double temp, double precipitation, double humidity, double cloud, double wind_velocity, Instant ts, Location location) {
        this.temp = temp;
        this.precipitation = precipitation;
        this.humidity = humidity;
        this.cloud = cloud;
        this.wind_velocity = wind_velocity;
        this.ts = ts;
        this.location = location;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "temperature = " + temp +
                ", probability of precipitation = " + precipitation +
                ", humidity = " + humidity +
                ", cloud = " + cloud +
                ", wind_velocity = " + wind_velocity +
                ", instant = " + ts +
                ", island = " + location.island +
                '}';
    }
}
