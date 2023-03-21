package ml.ruby.weatherrecyclerview.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ml.ruby.weatherrecyclerview.R;
import ml.ruby.weatherrecyclerview.room.PlacesDbDao;
import ml.ruby.weatherrecyclerview.model.db.PlaceRecodeItem;
import ml.ruby.weatherrecyclerview.model.place.PlaceBeanItem;
import ml.ruby.weatherrecyclerview.repository.PlacesRepository;
import ml.ruby.weatherrecyclerview.utils.AppDatabase;
import ml.ruby.weatherrecyclerview.utils.Constants;
import ml.ruby.weatherrecyclerview.utils.ExecutorSupplier;

/**
 * @author: jwhan
 * @createTime: 2022/05/10 8:57 PM
 * @description:
 */
public class QueryPlacesResultAdapter extends RecyclerView.Adapter<QueryPlacesResultAdapter.ViewHolder> {
    List<PlaceBeanItem> list;
    Context context;

    public QueryPlacesResultAdapter(List<PlaceBeanItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.query_places_results_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaceBeanItem bean = list.get(position);
        // Loading the image
        String flagUrl;
        // For PRC users
        if (Locale.getDefault().toString().equals("zh_CN") && bean.getCountry().equalsIgnoreCase("TW")) {
            flagUrl = "https://flagsapi.com/CN/flat/64.png";
        } else {
            flagUrl = "https://flagsapi.com/" + bean.getCountry().toUpperCase() + "/flat/64.png";
        }
        Glide.with(context).load(flagUrl).into(holder.countryFlag);
        bind(holder, bean);
        holder.starIcon.setChecked(false);
        ExecutorSupplier.getExecutor().execute(() -> {
            List<PlaceRecodeItem> list = AppDatabase.getInstance().getPlacesDbDao().getAll();
            for (PlaceRecodeItem item : list) {
                if (item.getAreaFullName().equals(bean.getAreaFullName()) &&
                        Math.abs(item.getLon() - bean.getLon()) <= Constants.EQUALS_ZERO &&
                        Math.abs(item.getLat() - bean.getLat()) <= Constants.EQUALS_ZERO) {
                    ((Activity) context).runOnUiThread(() -> holder.starIcon.setChecked(true));
                }
            }
        });
        holder.placeName.setText(bean.getAreaFullName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void bind(@NonNull ViewHolder holder, PlaceBeanItem bean) {
        View view = holder.view;
        CheckBox starIcon = holder.starIcon;
        view.setOnClickListener(v -> {
            PlacesRepository.getInstance().updatePlaceManually(bean);
            ((Activity) context).finish();
        });
        starIcon.setOnClickListener(v ->
                ExecutorSupplier.getExecutor().execute(
                        () -> {
                            PlacesDbDao placesDbDao = AppDatabase.getInstance().getPlacesDbDao();
                            List<PlaceRecodeItem> record = placesDbDao.isRecordExist(bean.getName(), bean.getState(), bean.getCountry(),
                                    bean.getLat(), bean.getLon());
                            if (starIcon.isChecked() && record.size() == 0) {
                                placesDbDao.insert(new PlaceRecodeItem(bean.getName(), bean.getLat(), bean.getLon(),
                                        bean.getCountry(), bean.getState(), true));
                            } else if (!starIcon.isChecked() && record.size() != 0) {
                                placesDbDao.deleteByPosition(bean.getLat(), bean.getLon(), true);
                            }
                        })
        );
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView countryFlag;
        TextView placeName;
        CheckBox starIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            countryFlag = itemView.findViewById(R.id.country_flag);
            placeName = itemView.findViewById(R.id.the_place_name);
            starIcon = itemView.findViewById(R.id.the_star_icon);
        }
    }
}
