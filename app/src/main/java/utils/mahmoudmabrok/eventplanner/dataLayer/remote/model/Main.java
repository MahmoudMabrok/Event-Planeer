package utils.mahmoudmabrok.eventplanner.dataLayer.remote.model;

public class Main {

    double temp;
    int pressure;
    int humidity;
    double temp_min;
    double temp_max;


    public Main() {
    }

    public Main(double temp, int pressure, int humidity, double temp_min, double temp_max) {

        this.temp = temp;

        this.pressure = pressure;

        this.humidity = humidity;

        this.temp_min = temp_min;

        this.temp_max = temp_max;

    }

    public double getTemp() {

        return this.temp;

    }

    public void setTemp(double temp) {

        this.temp = temp;

    }

    public int getPressure() {

        return this.pressure;

    }

    public void setPressure(int pressure) {

        this.pressure = pressure;

    }

    public int getHumidity() {

        return this.humidity;

    }

    public void setHumidity(int humidity) {

        this.humidity = humidity;

    }

    public double getTemp_min() {

        return this.temp_min;

    }

    public void setTemp_min(double temp_min) {

        this.temp_min = temp_min;

    }

    public double getTemp_max() {

        return this.temp_max;

    }

    public void setTemp_max(double temp_max) {

        this.temp_max = temp_max;

    }

}