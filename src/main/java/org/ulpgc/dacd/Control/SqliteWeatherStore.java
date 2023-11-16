package org.ulpgc.dacd.Control;

import org.ulpgc.dacd.Model.Location;
import org.ulpgc.dacd.Model.Weather;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.List;

public class SqliteWeatherStore implements WeatherStore{
    public String file;

    public SqliteWeatherStore(String file) {
        this.file = file;
    }

    @Override
    public void save(List<Weather> weathers) throws SQLException {
        Connection connection = open();
        Statement statement = connection.createStatement();
        for (Weather weather:weathers) {
            String sqlCheckPrediction = "SELECT * FROM " + weather.location.island + " WHERE ts = '" + weather.ts + "';";
            if (statement.executeQuery(sqlCheckPrediction).next()) {
                String sqlUpdatePrediction = "UPDATE " + weather.location.island + " SET temp = " + weather.temp + ", precipitation = " + weather.precipitation + ", humidity = " + weather.humidity + ", cloud = " + weather.cloud + ", wind_velocity = " + weather.wind_velocity + " WHERE ts = '" + weather.ts + "';";
                statement.executeUpdate(sqlUpdatePrediction);
            } else {
                String sqlSentence = "INSERT INTO " + weather.location.island + " (ts,temp,precipitation,humidity,cloud,wind_velocity) VALUES ('" + weather.ts + "'," + weather.temp + "," + weather.precipitation + "," + weather.humidity + "," + weather.cloud + "," + weather.wind_velocity + ");";
                statement.executeUpdate(sqlSentence);
            }
        }
        connection.close();
    }

    @Override
    public Weather get(Location location, Instant instant) throws SQLException {
        Connection connection = open();
        Statement statement = connection.createStatement();
        String sqlSentence = "SELECT * FROM " + location.island + " WHERE ts = '" + instant.toString() + "';";
        Weather weather = null;
        if (statement.executeQuery(sqlSentence).next()) {
            weather = new Weather(statement.executeQuery(sqlSentence).getDouble("temp"), statement.executeQuery(sqlSentence).getDouble("precipitation"), statement.executeQuery(sqlSentence).getDouble("humidity"), statement.executeQuery(sqlSentence).getDouble("cloud"), statement.executeQuery(sqlSentence).getDouble("wind_velocity"), instant, location);
        }
        connection.close();
        return weather;
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
