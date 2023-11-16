package org.ulpgc.dacd.Control;

import java.util.Date;

public class Task implements Runnable{
    WeatherController weatherController;

    public Task(WeatherController weatherController) {
        this.weatherController = weatherController;
    }

    @Override
    public void run() {
        weatherController.execute();
        System.out.println("La tarea se ejecutó a las " + new Date());
    }
}
