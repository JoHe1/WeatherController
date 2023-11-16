package org.ulpgc.dacd.Control;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.ulpgc.dacd.Model.Location;
import org.ulpgc.dacd.Model.Weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OpenWeatherMapProvider implements WeatherProvider{
    public String apikey;
    public OpenWeatherMapProvider(String apikey) {
        this.apikey = apikey;
    }

    @Override
    public List<Weather> get(Location location) throws IOException {
        List<Weather> weathers = new ArrayList<>();
        String url = "https://api.openweathermap.org/data/2.5/forecast?"
                + "lat=" + location.latitude
                + "&lon=" + location.longitude
                + "&appid=" + apikey
                + "&units=metric&cnt=40";
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            for (JsonElement jsonElement : JsonParser.parseString(response.toString()).getAsJsonObject().getAsJsonArray("list")) {
                String dt_txt = jsonElement.getAsJsonObject().get("dt_txt").getAsString();
                String hour = dt_txt.substring(11,13);
                if (hour.equals("12")){
                    double temp = jsonElement.getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsDouble();
                    double pop = jsonElement.getAsJsonObject().get("pop").getAsDouble();
                    double humidity = jsonElement.getAsJsonObject().get("main").getAsJsonObject().get("humidity").getAsDouble();
                    double clouds = jsonElement.getAsJsonObject().get("clouds").getAsJsonObject().get("all").getAsDouble();
                    double wind = jsonElement.getAsJsonObject().get("wind").getAsJsonObject().get("speed").getAsDouble();
                    Instant instantPrediction = Instant.ofEpochSecond(jsonElement.getAsJsonObject().get("dt").getAsLong());
                    weathers.add(new Weather(temp, pop, humidity, clouds, wind, instantPrediction, location));
                }
            }
        }
        return weathers;
    }
}
