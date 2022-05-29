package ml.ruby.weatherrecyclerview.network;

import java.util.List;

import ml.ruby.weatherrecyclerview.model.place.PlaceBeanItem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author: jwhan
 * @createTime: 2022/05/02 10:42 PM
 * @description: The interface of open weather geocoding api
 */
public interface PlaceApi {

    @GET("geo/1.0/direct")
    Call<List<PlaceBeanItem>> getPlaceInfo(@Query("q") String placeName,
                                           @Query("limit") String limit,
                                           @Query("appid") String appid);

    @GET("geo/1.0/reverse")
    Call<List<PlaceBeanItem>> getReversePlaceInfo(@Query("lat") double lat,
                                                  @Query("lon") double lon,
                                                  @Query("appid") String appid);
}
