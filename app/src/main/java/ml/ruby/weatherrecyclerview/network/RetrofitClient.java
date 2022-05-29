package ml.ruby.weatherrecyclerview.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author: jwhan
 * @createTime: 2022/04/27 4:25 PM
 * @description:
 */
public class RetrofitClient {
    static Retrofit objRetrofit = null;

    public static WeatherApi weatherProvider() {
        createInstance();
        return objRetrofit.create(WeatherApi.class);
    }

    public static PlaceApi placeGeocoding() {
        createInstance();
        return objRetrofit.create(PlaceApi.class);
    }

    private static void createInstance() {
        if (objRetrofit == null) {
            objRetrofit = new Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }
}
