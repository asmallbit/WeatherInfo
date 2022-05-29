package ml.ruby.weatherrecyclerview.network;

import ml.ruby.weatherrecyclerview.model.onecall.OneCallBean;
import ml.ruby.weatherrecyclerview.model.weather.WeatherBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author: jwhan
 * @createTime: 2022/04/27 10:10 AM
 * @description: The interface to call the open weather api
 */
public interface WeatherApi {

    @GET("data/2.5/onecall")
    Call<OneCallBean> getOneCallWeatherInfo(@Query("lat") String lat,
                                            @Query("lon") String lon,
                                            @Query("lang") String lang,
                                            @Query("appid") String appid);

    @GET("data/2.5/weather")
    Call<WeatherBean> getWeatherInfo(@Query("lat") double lat,
                                     @Query("lon") double lon,
                                     @Query("appid") String appid);

}
