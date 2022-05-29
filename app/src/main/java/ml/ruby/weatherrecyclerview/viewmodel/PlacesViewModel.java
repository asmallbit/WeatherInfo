package ml.ruby.weatherrecyclerview.viewmodel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import ml.ruby.weatherrecyclerview.model.place.PlaceBeanItem;
import ml.ruby.weatherrecyclerview.repository.PlacesRepository;

/**
 * @author: jwhan
 * @createTime: 2022/05/11 8:29 PM
 * @description:
 */
public class PlacesViewModel extends ViewModel {
    public PlacesViewModel() {
    }

    public void queryPlaces(String placeName,
                            String limit,
                            String appId) {
        PlacesRepository.getInstance().queryPlacesGeo(placeName, limit, appId);
    }

    public LiveData<List<PlaceBeanItem>> getGeoPlace() {
        return PlacesRepository.getInstance().getGeoPlaces();
    }

    public void updateLocation() {
        PlacesRepository.getInstance().updateLocation();
    }

    public LiveData<PlaceBeanItem> getPlaceToSwitch() {
        return PlacesRepository.getInstance().getPlace();
    }

    public LiveData<Boolean> isGPSEnabled() {
        return PlacesRepository.getInstance().isGPSEnabled();
    }
}
