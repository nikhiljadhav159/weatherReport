package com.weatherreport.MODEL;

import java.util.ArrayList;

/**
 * Created by Ajinkya.Pote on 17/02/2016.
 */
public class allWeather {

    private city city;
    private ArrayList<weatherList> list = new ArrayList<weatherList>();

    public com.weatherreport.MODEL.city getCity() {
        return city;
    }

    public void setCity(com.weatherreport.MODEL.city city) {
        this.city = city;
    }

    public ArrayList<weatherList> getList() {
        return list;
    }

    public void setList(ArrayList<weatherList> list) {
        this.list = list;
    }
}
