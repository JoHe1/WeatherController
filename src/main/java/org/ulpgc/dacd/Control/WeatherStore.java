package org.ulpgc.dacd.Control;

import org.ulpgc.dacd.Model.Location;
import org.ulpgc.dacd.Model.Weather;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

public interface WeatherStore extends AutoCloseable {
    void save(List<Weather> weathers) throws SQLException;
    Weather get(Location location, Instant instant) throws SQLException;
    Connection open() throws SQLException;

    void close() throws Exception;

    void createTable(Connection connection, String location) throws SQLException;
}
