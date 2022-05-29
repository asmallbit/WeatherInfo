package ml.ruby.weatherrecyclerview.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import ml.ruby.weatherrecyclerview.MyApplication;
import ml.ruby.weatherrecyclerview.R;
import ml.ruby.weatherrecyclerview.adapter.StaredPlacesAdapter;
import ml.ruby.weatherrecyclerview.databinding.FragmentStaredPlacesBinding;
import ml.ruby.weatherrecyclerview.listeners.ItemsUpdateListener;
import ml.ruby.weatherrecyclerview.model.db.PlaceRecodeItem;
import ml.ruby.weatherrecyclerview.utils.AppDatabase;
import ml.ruby.weatherrecyclerview.utils.ExecutorSupplier;
import ml.ruby.weatherrecyclerview.utils.PlacesDiffCallback;
import ml.ruby.weatherrecyclerview.utils.PreferenceManage;

/**
 * @author: jwhan
 * @createTime: 2022/05/10 10:59 PM
 * @description:
 */
public class StaredPlaceFragment extends Fragment implements ItemsUpdateListener {
    private FragmentStaredPlacesBinding binding;
    private final List<PlaceRecodeItem> currPlaces = new ArrayList<>();
    private final List<PlaceRecodeItem> newPlaces = new ArrayList<>();
    private StaredPlacesAdapter adapter;
    private PreferenceManage preferenceManage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManage = new PreferenceManage(MyApplication.getPreference());
        adapter = new StaredPlacesAdapter(currPlaces, getActivity());
        adapter.setUpdateItems(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentStaredPlacesBinding.inflate(inflater, container, false);
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.places_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Load the data when user launch the activity every time
        updateItems();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void updateItems() {
        ExecutorSupplier.getExecutor().execute(() -> {
            if (preferenceManage == null) {
                preferenceManage = new PreferenceManage(MyApplication.getPreference());
            }
            newPlaces.clear();
            String placeName = preferenceManage.getString("cityName");
            float lat = preferenceManage.getFloat("lat");
            float lon = preferenceManage.getFloat("lon");
            if (!"".equals(placeName)) {
                String[] placeInfo = placeName.split(", ");
                if (placeInfo.length == 2) {
                    newPlaces.add(new PlaceRecodeItem(placeInfo[0], lat, lon, placeInfo[1], null, false));
                } else {
                    newPlaces.add(new PlaceRecodeItem(placeInfo[0], lat, lon, placeInfo[2], placeInfo[1], false));
                }
            }
            newPlaces.addAll(AppDatabase.getInstance().getPlacesDbDao().getAll());
            PlacesDiffCallback placesDiffCallback = new PlacesDiffCallback(currPlaces, newPlaces);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(placesDiffCallback);
            currPlaces.clear();
            currPlaces.addAll(newPlaces);
            requireActivity().runOnUiThread(() -> diffResult.dispatchUpdatesTo(adapter));
        });
    }
}
