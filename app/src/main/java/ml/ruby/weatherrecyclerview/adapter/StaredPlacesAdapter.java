package ml.ruby.weatherrecyclerview.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ml.ruby.weatherrecyclerview.BuildConfig;
import ml.ruby.weatherrecyclerview.R;
import ml.ruby.weatherrecyclerview.listeners.ItemsUpdateListener;
import ml.ruby.weatherrecyclerview.model.db.PlaceRecodeItem;
import ml.ruby.weatherrecyclerview.model.place.PlaceBeanItem;
import ml.ruby.weatherrecyclerview.model.weather.WeatherBean;
import ml.ruby.weatherrecyclerview.network.RetrofitClient;
import ml.ruby.weatherrecyclerview.repository.PlacesRepository;
import ml.ruby.weatherrecyclerview.utils.AppDatabase;
import ml.ruby.weatherrecyclerview.utils.Constants;
import ml.ruby.weatherrecyclerview.utils.ExecutorSupplier;
import ml.ruby.weatherrecyclerview.utils.Utility;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author: jwhan
 * @createTime: 2022/05/10 8:31 PM
 * @description:
 */
public class StaredPlacesAdapter extends RecyclerView.Adapter<StaredPlacesAdapter.ViewHolder> {
    private List<PlaceRecodeItem> list;
    private final Context context;

    private ItemsUpdateListener callback = null;

    public StaredPlacesAdapter(List<PlaceRecodeItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setUpdateItems(ItemsUpdateListener items) {
        callback = items;
    }

    @NonNull
    @Override
    public StaredPlacesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stared_places_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaredPlacesAdapter.ViewHolder holder, int position) {
        PlaceRecodeItem place = list.get(position);
        holder.placeName.setText(place.getAreaFullName());
        setListeners(place, holder);
        // do a weather query about this item
        ExecutorSupplier.getExecutor().execute(() -> syncWeatherIcon(place, holder, BuildConfig.WEATHER_API_KEY));
        if (position == 0) {
            holder.starState.setBackgroundResource(R.drawable.ic_location);
        }
    }

    // 查询天气信息并显示对应天气德图标
    private void syncWeatherIcon(@NonNull PlaceRecodeItem place, ViewHolder holder, String appid) {
        Call<WeatherBean> weatherInfo = RetrofitClient.weatherProvider().getWeatherInfo(place.getLat(), place.getLon(), appid);
        try {
            Response<WeatherBean> response = weatherInfo.execute();
            if (response.body() != null) {
                WeatherBean body = response.body();
                double temperature = body.getMain().getTemp() - Constants.KELVINS;
                ((Activity) context).runOnUiThread(() -> {
                    holder.temperature.setText(((int) temperature) + "℃");
                    holder.forecastIcon.setImageResource(Utility.getWeatherArtImage(body.getWeather().get(0).getId()));
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setListeners(@NonNull PlaceRecodeItem place, ViewHolder holder) {
        holder.view.setOnClickListener(v -> {
            // TODO: @param localNames should not be null for multi languages.
            // 点击更新显示地点
            PlacesRepository.getInstance().updatePlaceManually(new PlaceBeanItem(place.getCountry(), place.getPlace(),
                    place.getLon(), place.getState(), place.getLat(), null));
            ((Activity) context).finish();
        });
        // 点击star从数据库中删除
        holder.starState.setOnClickListener(v -> {
            ExecutorSupplier.getExecutor().execute(() ->
                    AppDatabase.getInstance().getPlacesDbDao()
                            .deleteByPosition(place.getLat(), place.getLon(), true)
            );
            if (callback != null) {
                callback.updateItems();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView starState;
        TextView placeName;
        TextView temperature;
        ImageView forecastIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            starState = itemView.findViewById(R.id.star_icon);
            placeName = itemView.findViewById(R.id.place_name);
            temperature = itemView.findViewById(R.id.temperature_now);
            forecastIcon = itemView.findViewById(R.id.forecast_icon_now);
        }
    }
}
