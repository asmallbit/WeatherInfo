package ml.ruby.weatherrecyclerview.repository;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ml.ruby.weatherrecyclerview.BuildConfig;
import ml.ruby.weatherrecyclerview.MyApplication;
import ml.ruby.weatherrecyclerview.model.place.PlaceBeanItem;
import ml.ruby.weatherrecyclerview.network.RetrofitClient;
import ml.ruby.weatherrecyclerview.utils.Constants;
import ml.ruby.weatherrecyclerview.utils.Logs;
import ml.ruby.weatherrecyclerview.utils.NumberOperation;
import ml.ruby.weatherrecyclerview.utils.PreferenceManage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author: jwhan
 * @createTime: 2022/05/11 8:11 PM
 * @description:
 */
public class PlacesRepository {
    private static final String TAG = "PlacesRepository";
    private static PlacesRepository repository;
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    // The first one is for the query results and the second one is for store the location place name(decode geo)

    private final int accuracy = 2;           // 精度
    private final MutableLiveData<List<PlaceBeanItem>> geoPlaces = new MutableLiveData<>();
    private final MutableLiveData<PlaceBeanItem> place = new MutableLiveData<>();
    private final MutableLiveData<Boolean> GPSEnabled = new MutableLiveData<>(true);
    private PreferenceManage preferenceManage;

    private PlacesRepository() {
    }

    public static PlacesRepository getInstance() {
        if (repository == null) {
            repository = new PlacesRepository();
            repository.preferenceManage = new PreferenceManage(MyApplication.getPreference());
        }
        return repository;
    }

    /**
     * 根据名字查询地区, 将查询到的结果保存到geoPlaces
     *
     * @param placeName 地区名
     * @param limit     符合条件的地区数量, 根据OpenWeather api的限制, 上限为5
     * @param appid     OpenWeather api key
     */
    public void queryPlacesGeo(String placeName,
                               String limit,
                               String appid) {
        Call<List<PlaceBeanItem>> placeInfo = RetrofitClient.placeGeocoding().getPlaceInfo(placeName, limit, appid);
        placeInfo.enqueue(new Callback<List<PlaceBeanItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<PlaceBeanItem>> call, @NonNull Response<List<PlaceBeanItem>> response) {
                if (response.body() != null) {
                    geoPlaces.postValue(response.body());
                } else {
                    Logs.logDebug(TAG, "onResponse: get some errors now");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PlaceBeanItem>> call, @Nullable Throwable t) {
                toastMessage.postValue("No internet");
            }
        });
    }

    public void queryPlacesReverseGeo(double lat,
                                      double lon,
                                      String appid) {
        Call<List<PlaceBeanItem>> placeInfo = RetrofitClient.placeGeocoding().getReversePlaceInfo(lat, lon, appid);
        placeInfo.enqueue(new Callback<List<PlaceBeanItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<PlaceBeanItem>> call, @NonNull Response<List<PlaceBeanItem>> response) {
                if (response.body() != null) {
                    PlaceBeanItem city = response.body().get(0);
                    place.postValue(city);
                    preferenceManage.putString(Constants.CITY_NAME, city.getAreaFullName());
                } else {
                    Logs.logDebug(TAG, "onResponse: get some errors now");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PlaceBeanItem>> call, @Nullable Throwable t) {
                toastMessage.postValue("No internet");
            }
        });
    }

    public void updateLocation() {
        askGPSEnable();
        if (ContextCompat.checkSelfPermission(MyApplication.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // if the user doesn't get us the permission of FINE_LOCATION, we will return the London's lat and lon
            double lat = NumberOperation.round(51.509865, accuracy);
            double lon = NumberOperation.round(-0.118092, accuracy);
            queryPlacesReverseGeo(lat, lon, BuildConfig.WEATHER_API_KEY);
            saveTheData2Sp((float) lat, (float) lon);
            return;
        }
        LocationManager locationManager = (LocationManager) MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
        // Use the network instead(Added in API level 1)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1000,
                location -> {
                    if (location != null) {
                        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        double lat = NumberOperation.round(lastKnownLocation.getLatitude(), 2);
                        double lon = NumberOperation.round(lastKnownLocation.getLongitude(), 2);
                        queryPlacesReverseGeo(lat, lon, BuildConfig.WEATHER_API_KEY);
                        saveTheData2Sp((float) lat, (float) lon);
                    }
                });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // we will use getCurrentLocation() method if the device is running android S or higher
            locationManager.getCurrentLocation(LocationManager.FUSED_PROVIDER, null,
                    MyApplication.getContext().getMainExecutor(),
                    location -> {
                        if (location != null) {
                            double lat = NumberOperation.round(location.getLatitude(), accuracy);
                            double lon = NumberOperation.round(location.getLongitude(), accuracy);
                            queryPlacesReverseGeo(lat, lon, BuildConfig.WEATHER_API_KEY);
                            saveTheData2Sp((float) lat, (float) lon);
                        }
                    });
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // use getLastKnownLocation()
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1000,
                        location -> {
                            if (location != null) {
                                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                double lat = NumberOperation.round(lastKnownLocation.getLatitude(), 2);
                                double lon = NumberOperation.round(lastKnownLocation.getLongitude(), 2);
                                queryPlacesReverseGeo(lat, lon, BuildConfig.WEATHER_API_KEY);
                                saveTheData2Sp((float) lat, (float) lon);
                            }
                        });
            } catch (IllegalArgumentException e) {
                Logs.logDebug(TAG, "updateLocation: " + "The device doesn't support GPS");
            }
        }
    }

    private void askGPSEnable() {
        LocationManager locationManager = (LocationManager) MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
        PackageManager packageManager = MyApplication.getContext().getPackageManager();
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            GPSEnabled.postValue(false);
        }
    }

    private void saveTheData2Sp(float lat, float lon) {
        preferenceManage.putFloat(Constants.LATITUDE, lat);
        preferenceManage.putFloat(Constants.LONGITUDE, lon);
    }

    public void updatePlaceManually(PlaceBeanItem item) {
        place.setValue(item);
    }

    public LiveData<List<PlaceBeanItem>> getGeoPlaces() {
        return geoPlaces;
    }

    public LiveData<PlaceBeanItem> getPlace() {
        return place;
    }

    public LiveData<Boolean> isGPSEnabled() {
        return GPSEnabled;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }
}
