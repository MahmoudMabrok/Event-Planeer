package utils.mahmoudmabrok.eventplanner.dataLayer.remote.model;

public class Wind {

    double speed;
    int deg;


    public Wind() {
    }

    public Wind(double speed, int deg) {

        this.speed = speed;

        this.deg = deg;

    }

    public double getSpeed() {

        return this.speed;

    }

    public void setSpeed(double speed) {

        this.speed = speed;

    }

    public int getDeg() {

        return this.deg;

    }

    public void setDeg(int deg) {

        this.deg = deg;

    }

}