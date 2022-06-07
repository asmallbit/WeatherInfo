package ml.ruby.weatherrecyclerview.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author: jwhan
 * @createTime: 2022/04/27 4:25 PM
 * @description:
 */
public class RetrofitClient {
    private static class RetrofitClientContainer {
        private static Retrofit instance = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static WeatherApi weatherProvider() {
        return RetrofitClientContainer.instance.create(WeatherApi.class);
    }

    public static PlaceApi placeGeocoding() {
        return RetrofitClientContainer.instance.create(PlaceApi.class);
    }
}
