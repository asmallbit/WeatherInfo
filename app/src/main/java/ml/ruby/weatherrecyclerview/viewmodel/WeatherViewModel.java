package ml.ruby.weatherrecyclerview.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import ml.ruby.weatherrecyclerview.model.QueryParams;
import ml.ruby.weatherrecyclerview.model.onecall.OneCallBean;
import ml.ruby.weatherrecyclerview.repository.WeatherRepository;

/**
 * @author: jwhan
 * @createTime: 2022/04/27 5:56 PM
 * @description: ViewModel of WeatherFragment fragment
 */
public class WeatherViewModel extends ViewModel {
    public WeatherViewModel() {
    }

    public LiveData<OneCallBean> getWeatherLiveData() {
        return WeatherRepository.getInstance().getWeatherInfo();
    }

    public void queryWeatherInfo(@NonNull QueryParams params) {
        WeatherRepository.getInstance().queryOneCallWeatherInfo(params.lat, params.lon, params.lang, params.getAppId());
    }
}
