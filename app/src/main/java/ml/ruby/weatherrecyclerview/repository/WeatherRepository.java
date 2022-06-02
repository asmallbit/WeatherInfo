package ml.ruby.weatherrecyclerview.repository;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ml.ruby.weatherrecyclerview.model.onecall.OneCallBean;
import ml.ruby.weatherrecyclerview.network.RetrofitClient;
import ml.ruby.weatherrecyclerview.utils.Logs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author: jwhan
 * @createTime: 2022/04/27 4:17 PM
 * @description: Repository of the application
 */

public class WeatherRepository {
    private static final String TAG = "WeatherRepository";
    private static WeatherRepository repository = null;
    private final MutableLiveData<OneCallBean> weatherLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoaded = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSucceed = new MutableLiveData<>();

    private WeatherRepository() {
    }

    public static WeatherRepository getInstance() {
        if (repository == null) {
            repository = new WeatherRepository();
        }
        return repository;
    }

    // Query the weather info
    public void queryOneCallWeatherInfo(@NotNull String lat,
                                        @NotNull String lon,
                                        @NotNull String lang,
                                        @NotNull String appid) {
        Call<OneCallBean> weatherCall = RetrofitClient.weatherProvider().getOneCallWeatherInfo(lat, lon, lang, appid);
        // Call the api
        weatherCall.enqueue(new Callback<OneCallBean>() {
            @Override
            public void onResponse(@NonNull Call<OneCallBean> call, @NonNull Response<OneCallBean> response) {
                // Okay, we get the response successfully
                weatherLiveData.postValue(response.body());
                Logs.logDebug(TAG, "onResponse: " + weatherCall.request().url());
                isLoaded.postValue(true);
                isSucceed.postValue(true);
            }

            @Override
            public void onFailure(@NonNull Call<OneCallBean> call, @Nullable Throwable t) {
                isLoaded.postValue(true);
                isSucceed.postValue(false);
            }
        });
    }

    public LiveData<OneCallBean> getWeatherInfo() {
        return weatherLiveData;
    }

    public LiveData<Boolean> getIsLoaded() {
        return isLoaded;
    }

    public LiveData<Boolean> getIsSucceed() {
        return isSucceed;
    }
}
