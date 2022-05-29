package ml.ruby.weatherrecyclerview.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import ml.ruby.weatherrecyclerview.R;
import ml.ruby.weatherrecyclerview.adapter.WeatherRecyclerViewAdapter;
import ml.ruby.weatherrecyclerview.databinding.FragmentWeatherBinding;
import ml.ruby.weatherrecyclerview.model.place.PlaceBeanItem;
import ml.ruby.weatherrecyclerview.model.onecall.Daily;
import ml.ruby.weatherrecyclerview.model.QueryParams;
import ml.ruby.weatherrecyclerview.model.onecall.OneCallBean;
import ml.ruby.weatherrecyclerview.repository.WeatherRepository;
import ml.ruby.weatherrecyclerview.utils.Constants;
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
    private static Handler handler = null;
    private PlacesViewModel placesViewModel;
    private WeatherViewModel weatherViewModel;
    List<Daily> dailyList = new ArrayList<>();
    WeatherRecyclerViewAdapter adapter = new WeatherRecyclerViewAdapter(dailyList);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placesViewModel = new ViewModelProvider(this).get(PlacesViewModel.class);
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        this.placesViewModel.getPlaceToSwitch().observe(this,
                locationBean -> {
                    String latitude = String.valueOf(this.placesViewModel.getPlaceToSwitch().getValue().getLat());
                    String longitude = String.valueOf(this.placesViewModel.getPlaceToSwitch().getValue().getLon());
                    this.weatherViewModel.queryWeatherInfo(
                            new QueryParams(latitude, longitude, GetLanguage.getLanguage()));
                }
        );

        // Set a observer to observe the weather info
        weatherViewModel.getWeatherLiveData().observe(WeatherFragment.this, oneCallBean -> {
            // We will notify the adapter when the user make a new request successfully
            new Thread(() -> {
                OneCallBean value = weatherViewModel.getWeatherLiveData().getValue();
                if (value != null) {
                    List<Daily> dailies = value.getDaily();
                    dailyList.clear();
                    dailyList.addAll(dailies);
                }
                requireActivity().runOnUiThread(adapter::notifyDataSetChanged);
            }).start();
        });

        // SwipeRefreshLayout 显示与隐藏
        WeatherRepository.getInstance().getIsLoaded().observe(this, isLoaded ->
                isLoaded(isLoaded)
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.weatherRecyclerview.setAdapter(adapter);

        binding.refreshData.setColorSchemeColors(getResources().getColor(R.color.blue, getActivity().getTheme()),
                getResources().getColor(R.color.red, getActivity().getTheme()),
                getResources().getColor(R.color.yellow, getActivity().getTheme()),
                getResources().getColor(R.color.green, getActivity().getTheme()));
        binding.refreshData.setOnRefreshListener(() -> {
            // TODO: Set the refresh method. We should query whether the place now in database. And we should use the location
            // update to show its info in screen
            PlaceBeanItem value = placesViewModel.getPlaceToSwitch().getValue();
            if (value != null) {
                weatherViewModel.queryWeatherInfo(new QueryParams(String.valueOf(value.getLat()),
                        String.valueOf(value.getLon()), GetLanguage.getLanguage()));
            }
        });
    }

    private void isLoaded(boolean isLoaded) {
        if (isLoaded) {
            binding.refreshData.setRefreshing(false);
        }
    }
}
