package utils.mahmoudmabrok.eventplanner.dataLayer.remote.model;

public class Weather {

    int id;
    String main;
    String description;
    String icon;


    public Weather() {
    }

    public Weather(int id, String main, String description, String icon) {

        this.id = id;

        this.main = main;

        this.description = description;

        this.icon = icon;

    }

    public int getId() {

        return this.id;

    }

    public void setId(int id) {

        this.id = id;

    }

    public String getMain() {

        return this.main;

    }

    public void setMain(String main) {

        this.main = main;

    }

    public String getDescription() {

        return this.description;

    }

    public void setDescription(String description) {

        this.description = description;

    }

    public String getIcon() {

        return this.icon;

    }

    public void setIcon(String icon) {

        this.icon = icon;

    }

}