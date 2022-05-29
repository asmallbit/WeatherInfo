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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import ml.ruby.weatherrecyclerview.databinding.ActivityMainBinding;
import ml.ruby.weatherrecyclerview.repository.PlacesRepository;
import ml.ruby.weatherrecyclerview.repository.WeatherRepository;
import ml.ruby.weatherrecyclerview.utils.ExecutorSupplier;
import ml.ruby.weatherrecyclerview.utils.PreferenceManage;
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

        // ToastMessage
        PlacesRepository.getInstance().getToastMessage().observe(this, toastMessage ->
                Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show()
        );

        // 天气数据拉取失败提示
        WeatherRepository.getInstance().getIsSucceed().observe(this, isSucceed -> {
            if (!isSucceed) {
                Toast.makeText(MainActivity.this, "Connect to open weather. Maybe you don't have internet now.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ProgressBar
        WeatherRepository.getInstance().getIsLoaded().observe(this, this::isLoaded
        );

        // 根棍位置
        placesViewModel.updateLocation();

        placesViewModel.getPlaceToSwitch().observe(this, placeBeanItem -> {
                    // TODO: Chang the search action bar icon
                    binding.locationPlaceName.setText(placeBeanItem.getAreaFullName());
                }
        );

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
        FragmentTransaction addFragment = getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_weather, new WeatherFragment());
        // ... Maybe more widgets
        addFragment.commit();
    }

    private void isLoaded(boolean isLoaded) {
        if (isLoaded) {
            binding.progressbar.setVisibility(View.GONE);
        } else {
            binding.progressbar.setVisibility(View.VISIBLE);
        }
    }
}