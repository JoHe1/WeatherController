package org.ulpgc.dacd.Control;

import org.ulpgc.dacd.Model.Location;
import org.ulpgc.dacd.Model.Weather;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.List;

public class SqliteWeatherStore implements  WeatherStore{
    public String file;

    public SqliteWeatherStore(String file) {
        this.file = file;
    }

    @Override
    public void save(List<Weather> weathers) throws SQLException {

    }

    @Override
    public Weather get(Location location, Instant instant) throws SQLException {
        return null;
    }

    @Override
    public Connection open() throws SQLException {
        String url = "jdbc:sqlite:" + this.file;
        Connection connection = null ;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println("Error creating the database: " + e.getMessage());
        }
        return connection;
    }

    public void createTable(Connection connection, String island) throws SQLException{
        Statement statement = connection.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + island + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ts TEXT," +
                "temp REAL," +
                "precipitation REAL," +
                "humidity REAL," +
                "cloud REAL," +
                "wind_velocity REAL)";
        statement.execute(createTableSQL);
    }

    @Override
    public void close() throws Exception {

    }
}
