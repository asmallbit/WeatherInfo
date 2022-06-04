package ml.ruby.weatherrecyclerview.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ml.ruby.weatherrecyclerview.MyApplication;
import ml.ruby.weatherrecyclerview.R;
import ml.ruby.weatherrecyclerview.databinding.WeatherRecyclerviewItemsBinding;
import ml.ruby.weatherrecyclerview.model.onecall.Daily;
import ml.ruby.weatherrecyclerview.utils.Constants;
import ml.ruby.weatherrecyclerview.utils.NumberOperation;
import ml.ruby.weatherrecyclerview.utils.Utility;

/**
 * @author: jwhan
 * @createTime: 2022/04/27 10:40 AM
 * @description:
 */
public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder> {
    List<Daily> weatherBeanList;
    private final String[] days = MyApplication.getContext().getResources().getStringArray(R.array.week_array);

    public WeatherRecyclerViewAdapter(List<Daily> weatherBeanList) {
        this.weatherBeanList = weatherBeanList;
    }

    @NonNull
    @Override
    public WeatherRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WeatherRecyclerviewItemsBinding binding = WeatherRecyclerviewItemsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WeatherRecyclerViewAdapter.ViewHolder holder, int position) {
        Daily daily = weatherBeanList.get(position);
        Date date = new Date(daily.getSunrise() * 1000L);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        holder.binding.date.setText(Utility.getTimeFromTimeStamp(daily.getDt(), "MM.dd")
                + " " + days[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        // Handle the temperature
        String temp = "";
        // Integer maybe better for reading
        long min = Math.round(daily.getTemp().getMin() - Constants.KELVINS);
        long max = Math.round(daily.getTemp().getMax() - Constants.KELVINS);
        temp += min + " / " + max + "℃";
        holder.binding.temperatureRange.setText(temp);
        // The weather forecast description
        holder.binding.forecast.setImageResource(Utility.getWeatherArtImage(daily.getWeather().get(0).getId()));
        holder.binding.expandableLayout.collapse();
        setListeners(holder, daily, position);
    }

    @Override
    public int getItemCount() {
        return weatherBeanList.size();
    }

    private void setListeners(@NonNull WeatherRecyclerViewAdapter.ViewHolder holder, Daily daily, int position) {
        holder.binding.weatherItem.setOnClickListener(v -> {
            holder.binding.expandableLayout.toggle();
            if (holder.binding.expandableLayout.isExpanded()) {
                holder.binding.precipitationKey.setText(MyApplication.getContext().getString(R.string.precipitation_key, daily.getRain()));
                holder.binding.popKey.setText(MyApplication.getContext().getString(R.string.pop_key, (int) (daily.getPop() * 100)));
                holder.binding.windKey.setText(MyApplication.getContext().
                        getString(R.string.wind_key, daily.getWindSpeed(), Utility.convertDegreeToCardinalDirection(daily.getWindDeg())));
                holder.binding.humidityKey.setText(MyApplication.getContext().getString(R.string.humidity_key, daily.getHumidity()));
                holder.binding.uvIndexKey.setText(String.valueOf(NumberOperation.round(daily.getUvi(), 1)));
                holder.binding.sunriseKey.setText(Utility.getTimeFromTimeStamp(daily.getSunrise(), "HH:mm"));
                holder.binding.sunsetKey.setText(Utility.getTimeFromTimeStamp(daily.getSunset(), "HH:mm"));
                holder.binding.expandableIcon.setImageResource(R.drawable.ic_collapse);
            } else {
                holder.binding.expandableIcon.setImageResource(R.drawable.ic_expand);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        WeatherRecyclerviewItemsBinding binding;

        public ViewHolder(@NonNull WeatherRecyclerviewItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
