package utils.mahmoudmabrok.eventplanner.dataLayer.remote;

import retrofit2.Call;
import retrofit2.Response;
import utils.mahmoudmabrok.eventplanner.dataLayer.remote.model.WeatherResponce;

public class Remote {

    WeatherService weatherService;

    public Remote() {
        weatherService = ApiClient.getRetrofit().create(WeatherService.class);
    }

    public Call<WeatherResponce> getWeather() {
        return weatherService.getWeather();
    }
}
