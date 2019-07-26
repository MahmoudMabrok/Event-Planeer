package utils.mahmoudmabrok.eventplanner.dataLayer.remote.model;

public class Coord {

    double lon;
    double lat;


    public Coord() {
    }

    public Coord(double lon, double lat) {

        this.lon = lon;

        this.lat = lat;

    }

    public double getLon() {

        return this.lon;

    }

    public void setLon(double lon) {

        this.lon = lon;

    }

    public double getLat() {

        return this.lat;

    }

    public void setLat(double lat) {

        this.lat = lat;

    }

}