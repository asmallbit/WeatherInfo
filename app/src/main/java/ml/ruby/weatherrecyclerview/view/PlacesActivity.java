package ml.ruby.weatherrecyclerview.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.lifecycle.ViewModelProvider;
import ml.ruby.weatherrecyclerview.BuildConfig;
import ml.ruby.weatherrecyclerview.R;
import ml.ruby.weatherrecyclerview.databinding.ActivityPlacesBinding;
import ml.ruby.weatherrecyclerview.viewmodel.PlacesViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;

public class PlacesActivity extends AppCompatActivity {

    private ActivityPlacesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlacesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        PlacesViewModel model = new ViewModelProvider(this).get(PlacesViewModel.class);

        // Binding the fragment
        changeToPlacesActivity();

        // 搜索地区
        binding.placeNameSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                model.queryPlaces(binding.placeNameSearch.getText().toString(), "5", BuildConfig.WEATHER_API_KEY);
                changeToQueryPlacesFragment();
                return true;
            }
            return false;
        });

        binding.cancelSearch.setOnClickListener(v -> binding.placeNameSearch.setText(""));
    }

    public void changeToPlacesActivity() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.places_list, new StaredPlaceFragment(), "star_fragment").commit();
    }

    public void changeToQueryPlacesFragment() {
        StaredPlaceFragment fragment = (StaredPlaceFragment) getSupportFragmentManager().findFragmentByTag("star_fragment");
        if (fragment != null && fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.places_list, new QueryPlacesFragment(), "query_fragment").commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.places_list, new QueryPlacesFragment(), "query_fragment").commit();
        }
    }
}