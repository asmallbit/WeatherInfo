package ml.ruby.weatherrecyclerview.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ml.ruby.weatherrecyclerview.adapter.WeatherRecyclerViewAdapter;
import ml.ruby.weatherrecyclerview.databinding.FragmentWeatherBinding;
import ml.ruby.weatherrecyclerview.model.QueryParams;
import ml.ruby.weatherrecyclerview.model.onecall.Daily;
import ml.ruby.weatherrecyclerview.model.onecall.OneCallBean;
import ml.ruby.weatherrecyclerview.utils.ExecutorSupplier;
import ml.ruby.weatherrecyclerview.utils.GetLanguage;
import ml.ruby.weatherrecyclerview.viewmodel.PlacesViewModel;
import ml.ruby.weatherrecyclerview.viewmodel.WeatherViewModel;

/**
 * @author: jwhan
 * @createTime: 2022/04/27 11:02 AM
 * @description: Fragment of weather forecast
 */
public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;
    private PlacesViewModel placesViewModel;
    private WeatherViewModel weatherViewModel;
    private final List<Daily> dailyList = new ArrayList<>(8);
    private final WeatherRecyclerViewAdapter adapter = new WeatherRecyclerViewAdapter(dailyList);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placesViewModel = new ViewModelProvider(this).get(PlacesViewModel.class);
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        this.placesViewModel.getPlaceToSwitch().observe(this,
                locationBean -> {
                    String latitude = String.valueOf(Objects.requireNonNull(this.placesViewModel.getPlaceToSwitch().getValue()).getLat());
                    String longitude = String.valueOf(this.placesViewModel.getPlaceToSwitch().getValue().getLon());
                    this.weatherViewModel.queryWeatherInfo(
                            new QueryParams(latitude, longitude, GetLanguage.getLanguage()));
                }
        );

        // Set a observer to observe the weather info
        weatherViewModel.getWeatherLiveData().observe(WeatherFragment.this, oneCallBean -> {
            // We will notify the adapter when the user make a new request successfully
            ExecutorSupplier.getExecutor().execute(() -> {
                OneCallBean value = weatherViewModel.getWeatherLiveData().getValue();
                if (value != null) {
                    dailyList.clear();
                    dailyList.addAll(value.getDaily());
                }
                requireActivity().runOnUiThread(() -> adapter.notifyItemRangeChanged(0, 7));
            });
        });

        if (savedInstanceState != null) {
            dailyList.addAll(Objects.requireNonNull(weatherViewModel.getWeatherLiveData().getValue()).getDaily());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.weatherRecyclerview.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
