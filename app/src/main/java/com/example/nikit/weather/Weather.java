package com.example.nikit.weather;

import java.util.Date;

/**
 * Created by nikit on 10.01.2017.
 */

public class Weather {
    private double mainTemp;
    private double minTemp;
    private double maxTemp;
    private double mainPressure;
    private double mainHumidity;

    private int weatherId;
    private String weatherMain;


    private String weatherDescription;
    private String weatherIconId;

    private int cloudsAll;
    private double windSpeed;
    private double windDeg;

    private Date date;

    public String getWeatherMain() {
        return weatherMain;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }


    public double getMainTemp() {
        return mainTemp;
    }

    public void setMainTemp(double mainTemp) {
        this.mainTemp = mainTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getMainPressure() {
        return mainPressure;
    }

    public void setMainPressure(double mainPressure) {
        this.mainPressure = mainPressure;
    }

    public double getMainHumidity() {
        return mainHumidity;
    }

    public void setMainHumidity(double mainHumidity) {
        this.mainHumidity = mainHumidity;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getWeatherIconId() {
        return weatherIconId;
    }

    public void setWeatherIconId(String weatherIconId) {
        this.weatherIconId = weatherIconId;
    }

    public int getCloudsAll() {
        return cloudsAll;
    }

    public void setCloudsAll(int cloudsAll) {
        this.cloudsAll = cloudsAll;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(double windDeg) {
        this.windDeg = windDeg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "mainTemp=" + mainTemp +
                ", minTemp=" + minTemp +
                ", maxTemp=" + maxTemp +
                ", mainPressure=" + mainPressure +
                ", mainHumidity=" + mainHumidity +
                ", weatherId=" + weatherId +
                ", weatherMain='" + weatherMain + '\'' +
                ", weatherDescription='" + weatherDescription + '\'' +
                ", weatherIconId='" + weatherIconId + '\'' +
                ", cloudsAll=" + cloudsAll +
                ", windSpeed=" + windSpeed +
                ", windDeg=" + windDeg +
                ", date=" + date +
                '}';
    }
}
