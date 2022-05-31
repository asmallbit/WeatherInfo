package ml.ruby.weatherrecyclerview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ml.ruby.weatherrecyclerview.adapter.HourlyWeatherAdapter;
import ml.ruby.weatherrecyclerview.databinding.ActivityMainBinding;
import ml.ruby.weatherrecyclerview.model.onecall.Current;
import ml.ruby.weatherrecyclerview.model.onecall.Hourly;
import ml.ruby.weatherrecyclerview.model.onecall.OneCallBean;
import ml.ruby.weatherrecyclerview.model.onecall.Weather;
import ml.ruby.weatherrecyclerview.repository.PlacesRepository;
import ml.ruby.weatherrecyclerview.repository.WeatherRepository;
import ml.ruby.weatherrecyclerview.utils.Constants;
import ml.ruby.weatherrecyclerview.utils.ExecutorSupplier;
import ml.ruby.weatherrecyclerview.utils.NumberOperation;
import ml.ruby.weatherrecyclerview.utils.PreferenceManage;
import ml.ruby.weatherrecyclerview.utils.Utility;
import ml.ruby.weatherrecyclerview.view.HourlyWeatherFragment;
import ml.ruby.weatherrecyclerview.view.PlacesActivity;
import ml.ruby.weatherrecyclerview.view.WeatherFragment;
import ml.ruby.weatherrecyclerview.viewmodel.PlacesViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final int ASK_LOCATION_PERMISSION = 1;
    private PreferenceManage preferenceManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PlacesViewModel placesViewModel = new ViewModelProvider(this).get(PlacesViewModel.class);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bindFragment();

        preferenceManage = new PreferenceManage(MyApplication.getPreference());
        if (!"".equals(preferenceManage.getString("cityName"))) {
            PlacesRepository.getInstance().queryPlacesReverseGeo(preferenceManage.getFloat("lat"),
                    preferenceManage.getFloat("lon"), BuildConfig.WEATHER_API_KEY);
            binding.locationPlaceName.setText(preferenceManage.getString("cityName"));
        }

        setObserver(placesViewModel);

        binding.searchView.setOnClickListener(v -> startActivity(new Intent(this, PlacesActivity.class)));

        binding.progressbar.setVisibility(View.VISIBLE);

        // Check the permission and ask for permission if user don't grant it
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ASK_LOCATION_PERMISSION);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭线程池
        ExecutorSupplier.getExecutor().shutdownNow();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ASK_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this,
                            "Sorry, you have to grant this permission to let us show the weather at your location",
                            Toast.LENGTH_SHORT).show();
                    PlacesRepository.getInstance().updateLocation();
                }
        }
    }

    private void bindFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_weather, new WeatherFragment())
                .add(R.id.fragment_hourly_weather, new HourlyWeatherFragment())
                .commit();
    }

    private void setObserver(PlacesViewModel placesViewModel) {
        // ToastMessage
        PlacesRepository.getInstance().getToastMessage().observe(this, toastMessage ->
                Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show()
        );

        // 天气数据拉取失败提示
        WeatherRepository.getInstance().getIsSucceed().observe(this, isSucceed -> {
            if (!isSucceed) {
                Toast.makeText(MainActivity.this, "Can't connect to open weather. Maybe you don't have internet now.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ProgressBar
        WeatherRepository.getInstance().getIsLoaded().observe(this, this::isLoaded
        );

        // 更新位置
        placesViewModel.updateLocation();

        placesViewModel.getPlaceToSwitch().observe(this, placeBeanItem -> {
                    binding.locationPlaceName.setText(placeBeanItem.getAreaFullName());
                    binding.progressbar.setVisibility(View.VISIBLE);
                }
        );

        // 检测GPS是否打开
        placesViewModel.isGPSEnabled().observe(this, isGPSEnabled -> {
            if (!isGPSEnabled) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Please open the gps")
                        .setMessage("We need the gps to get your location. Please enable the gps")
                        .setPositiveButton("Yes", (dialog, which) ->
                                MainActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                        .setNegativeButton("No", (dialog, which) ->
                                Toast.makeText(MainActivity.this,
                                        "Okay, we will show London's weather as we cant get your location", Toast.LENGTH_SHORT).show())
                        .setCancelable(false)
                        .show();
            }
        });

        // 监听天气是否刷新
        WeatherRepository.getInstance().getWeatherInfo().observe(this, this::setWeatherWidget);
    }

    private void isLoaded(boolean isLoaded) {
        if (isLoaded) {
            binding.progressbar.setVisibility(View.GONE);
        } else {
            binding.progressbar.setVisibility(View.VISIBLE);
        }
    }

    private void setWeatherWidget(OneCallBean bean) {
        Current current = bean.getCurrent();
        Weather currentWeather = bean.getCurrent().getWeather().get(0);
        binding.weatherWidget.setBackgroundResource(R.color.weather_widget_background_color);
        binding.weatherIcon.setImageResource(Utility.getWeatherArtImage(currentWeather.getId()));
        binding.weatherInfo.setText(currentWeather.getDescription());
        binding.temperature.setText((int) (current.getTemp() - Constants.KELVINS) + "℃");
        binding.feelsLike.setText("Feels like " +
                (int) (current.getFeelsLike() - Constants.KELVINS) + "℃");
        binding.windSpeed.setText("Wind: " + NumberOperation.round(current.getWindSpeed(), 1) + "m/s "
                + Utility.convertDegreeToCardinalDirection(current.getWindDeg()));
        binding.humidity.setText("Humidity: " + current.getHumidity() + "%");
        binding.uvIndex.setText("UV index: " + NumberOperation.round(current.getUvi(), 1));
        binding.pressure.setText("Pressure: " + current.getPressure() + "hPa");
        binding.visibility.setText("Visibility: " + current.getVisibility() / 1000 + "km");
        binding.dewPoint.setText("Dew point: " + (int) (current.getDewPoint() - Constants.KELVINS) + "℃");
    }
}