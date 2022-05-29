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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import ml.ruby.weatherrecyclerview.R;
import ml.ruby.weatherrecyclerview.adapter.QueryPlacesResultAdapter;
import ml.ruby.weatherrecyclerview.databinding.FragmentQueryPlaceResultsBinding;
import ml.ruby.weatherrecyclerview.model.place.PlaceBeanItem;
import ml.ruby.weatherrecyclerview.viewmodel.PlacesViewModel;

/**
 * @author: jwhan
 * @createTime: 2022/05/11 8:27 PM
 * @description:
 */
public class QueryPlacesFragment extends Fragment {
    private FragmentQueryPlaceResultsBinding binding;
    private PlacesViewModel model;
    private List<PlaceBeanItem> placesInfoList;
    private QueryPlacesResultAdapter adapter;
    private RecyclerView placeRecyclerView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(PlacesViewModel.class);
        placesInfoList = new ArrayList<>();
        adapter = new QueryPlacesResultAdapter(placesInfoList, getActivity());
        model.getGeoPlace().observe(this, placeBeanItemList -> {
            placesInfoList.clear();
            placesInfoList.addAll(placeBeanItemList);
            adapter.notifyDataSetChanged();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentQueryPlaceResultsBinding.inflate(inflater, container, false);
        placeRecyclerView = binding.getRoot().findViewById(R.id.place_result_recyclerview);
        placeRecyclerView.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
