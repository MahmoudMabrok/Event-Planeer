package utils.mahmoudmabrok.eventplanner.feature.displayEvents;

public class Event {

    private String name;
    private String date;
    private String details;

    private String weatherIconCode;

    private double temperature;
    private double humidity;

    public Event(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    public String getWeatherIconCode() {
        return weatherIconCode;
    }

    public void setWeatherIconCode(String weatherIconCode) {
        this.weatherIconCode = weatherIconCode;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public String getWeather() {
        return "" + temperature + '\n' + humidity;
    }
}
