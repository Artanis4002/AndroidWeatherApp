package com.example.assignment_6;

public class weatherModel {

    private String time;
    private int temp;
    private int humidity;
    private int wind;
    private String tempUnit;
    private String humidUnit;
    private String windUnit;
    private String icon;

    public weatherModel(String time, int temp, int humidity, int wind, String tempUnit, String humidUnit, String windUnit, String icon) {
        this.time = time;
        this.temp = temp;
        this.humidity = humidity;
        this.wind = wind;
        this.tempUnit = tempUnit;
        this.humidUnit = humidUnit;
        this.windUnit = windUnit;
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getWind() {
        return wind;
    }

    public void setWind(int wind) {
        this.wind = wind;
    }

    public String getTempUnit() {
        return tempUnit;
    }

    public void setTempUnit(String tempUnit) {
        this.tempUnit = tempUnit;
    }

    public String getHumidUnit() {
        return humidUnit;
    }

    public void setHumidUnit(String humidUnit) {
        this.humidUnit = humidUnit;
    }

    public String getWindUnit() {
        return windUnit;
    }

    public void setWindUnit(String windUnit) {
        this.windUnit = windUnit;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
