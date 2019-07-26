package utils.mahmoudmabrok.eventplanner.dataLayer.remote;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import utils.mahmoudmabrok.eventplanner.dataLayer.remote.model.WeatherResponce;

public interface WeatherService {

    @GET("weather?q=Cairo&appid=735df74303fd5bec95ae390f0ce0443d&units=metric")
    public Call<WeatherResponce> getWeather();
}
