package org.ulpgc.dacd.Control;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.ulpgc.dacd.Model.Location;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static Map<String, Location> mapIslandLocation = new HashMap<>();
    public static void main(String[] args) throws SQLException {
        WeatherProvider weatherProvider = new OpenWeatherMapProvider(args[0]);
        WeatherStore weatherStore = new SqliteWeatherStore(args[1]);
        WeatherController weatherController = new WeatherController(weatherProvider, weatherStore);
        loadStaticLocations(args[2]);

        boolean databaseExist = new File(args[1]).exists();
        Menu(databaseExist, weatherController);
    }

    private static void Menu(boolean databaseExist, WeatherController weatherController) throws SQLException {
        // Mostrar el menú
        int option;
        System.out.println("----- Menú -----");
        System.out.println("1. Activate the APP to collect DATA");
        if (databaseExist == true){
            System.out.println("2. Show next 5 predictions");
        }
        System.out.println("0. Exit");
        do{
            System.out.print("Enter the option number: ");
            option = scanner.nextInt();
            switch(option){
                case 1:
                    System.out.println("Activating the APP...");
                    Connection connection = weatherController.weatherStore.open();
                    createIslandsTablesSqlite(weatherController, connection);
                    periodicTask(weatherController);
                    break;
                case 2:
                    System.out.println("Showing next 5 predictions...");
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }

        }while(option != 0);
    }

    private static void periodicTask(WeatherController weatherController) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Calendar now = Calendar.getInstance();
        Calendar nextRun = Calendar.getInstance();
        nextRun.set(Calendar.HOUR_OF_DAY, 0);
        nextRun.set(Calendar.MINUTE, 0);
        nextRun.set(Calendar.SECOND, 0);
        nextRun.set(Calendar.MILLISECOND, 0);
        if (now.after(nextRun)) {
            nextRun.add(Calendar.DAY_OF_YEAR, 1);
        }
        long initialDelay = nextRun.getTimeInMillis() - System.currentTimeMillis();
        scheduler.scheduleAtFixedRate(new Task(weatherController), initialDelay, 6 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
    }

    private static void loadStaticLocations(String file) {
        try (Reader reader = new FileReader(file);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            for (CSVRecord csvRecord : csvParser) {
                mapIslandLocation.put(csvRecord.get(0), new Location(csvRecord.get(1), csvRecord.get(2),csvRecord.get(0)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void createIslandsTablesSqlite(WeatherController weatherController, Connection connection) {
        try {
            for (String island:mapIslandLocation.keySet()) {
                weatherController.weatherStore.createTable(connection, island);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}