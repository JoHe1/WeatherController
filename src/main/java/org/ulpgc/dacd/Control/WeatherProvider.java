package org.ulpgc.dacd.Control;

import org.ulpgc.dacd.Model.Location;
import org.ulpgc.dacd.Model.Weather;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public interface WeatherProvider {
    List<Weather> get(Location location) throws IOException;
}
