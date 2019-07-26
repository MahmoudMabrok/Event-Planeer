package utils.mahmoudmabrok.eventplanner.dataLayer.remote.model;

import java.util.List;

public class WeatherResponce {

    Coord coord;
    List<Weather> weather;
    String base;
    Main main;
    int visibility;
    Wind wind;
    Clouds clouds;
    int dt;
    Sys sys;
    int timezone;
    int id;
    String name;
    int cod;


    public WeatherResponce() {
    }

    public WeatherResponce(Coord coord, List<Weather> weather, String base, Main main, int visibility, Wind wind, Clouds clouds, int dt, Sys sys, int timezone, int id, String name, int cod) {

        this.coord = coord;

        this.weather = weather;

        this.base = base;

        this.main = main;

        this.visibility = visibility;

        this.wind = wind;

        this.clouds = clouds;

        this.dt = dt;

        this.sys = sys;

        this.timezone = timezone;

        this.id = id;

        this.name = name;

        this.cod = cod;

    }

    public Coord getCoord() {


        return this.coord;

    }

    public void setCoord(Coord coord) {

        this.coord = coord;

    }

    public List<Weather> getWeather() {

        return this.weather;

    }

    public void setWeather(List<Weather> weather) {

        this.weather = weather;

    }

    public String getBase() {

        return this.base;

    }

    public void setBase(String base) {

        this.base = base;

    }

    public Main getMain() {


        return this.main;

    }

    public void setMain(Main main) {

        this.main = main;

    }

    public int getVisibility() {

        return this.visibility;

    }

    public void setVisibility(int visibility) {

        this.visibility = visibility;

    }

    public Wind getWind() {


        return this.wind;

    }

    public void setWind(Wind wind) {

        this.wind = wind;

    }

    public Clouds getClouds() {


        return this.clouds;

    }

    public void setClouds(Clouds clouds) {

        this.clouds = clouds;

    }

    public int getDt() {

        return this.dt;

    }

    public void setDt(int dt) {

        this.dt = dt;

    }

    public Sys getSys() {


        return this.sys;

    }

    public void setSys(Sys sys) {

        this.sys = sys;

    }

    public int getTimezone() {

        return this.timezone;

    }

    public void setTimezone(int timezone) {

        this.timezone = timezone;

    }

    public int getId() {

        return this.id;

    }

    public void setId(int id) {

        this.id = id;

    }

    public String getName() {

        return this.name;

    }

    public void setName(String name) {

        this.name = name;

    }

    public int getCod() {

        return this.cod;

    }

    public void setCod(int cod) {

        this.cod = cod;

    }

}