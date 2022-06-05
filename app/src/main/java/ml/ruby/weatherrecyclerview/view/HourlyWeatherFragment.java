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
import ml.ruby.weatherrecyclerview.adapter.HourlyWeatherAdapter;
import ml.ruby.weatherrecyclerview.databinding.FragmentHourlyWeatherBinding;
import ml.ruby.weatherrecyclerview.model.onecall.Hourly;
import ml.ruby.weatherrecyclerview.utils.ExecutorSupplier;
import ml.ruby.weatherrecyclerview.viewmodel.WeatherViewModel;

public class HourlyWeatherFragment extends Fragment {
    private final List<Hourly> hourlies = new ArrayList<>(24);
    private final HourlyWeatherAdapter adapter = new HourlyWeatherAdapter(hourlies);
    private FragmentHourlyWeatherBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeatherViewModel weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        weatherViewModel.getWeatherLiveData().observe(this, oneCallBean -> {
            ExecutorSupplier.getExecutor().execute(() -> {
                hourlies.clear();
                for (int i = 0; i < 24; i++) {
                    hourlies.add(oneCallBean.getHourly().get(i));
                }
                requireActivity().runOnUiThread(adapter::notifyDataSetChanged);
            });
        });

        if (savedInstanceState != null) {
            hourlies.addAll(Objects.requireNonNull(weatherViewModel.getWeatherLiveData().getValue()).getHourly());
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentHourlyWeatherBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.everyHourWeather.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}