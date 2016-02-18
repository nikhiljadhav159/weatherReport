package com.weatherreport.MODEL;

/**
 * Created by Ajinkya.Pote on 17/02/2016.
 */
public class weatherList {

    private String dt,pressure,humidity;
    private weather weather;
    private Tempreture tempreture;

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public Tempreture getTempreture() {
        return tempreture;
    }

    public void setTempreture(Tempreture tempreture) {
        this.tempreture = tempreture;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public com.weatherreport.MODEL.weather getWeather() {
        return weather;
    }

    public void setWeather(com.weatherreport.MODEL.weather weather) {
        this.weather = weather;
    }
}
