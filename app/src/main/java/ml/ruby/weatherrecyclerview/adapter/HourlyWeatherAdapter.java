package ml.ruby.weatherrecyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ml.ruby.weatherrecyclerview.R;
import ml.ruby.weatherrecyclerview.model.onecall.Hourly;
import ml.ruby.weatherrecyclerview.utils.Constants;
import ml.ruby.weatherrecyclerview.utils.NumberOperation;
import ml.ruby.weatherrecyclerview.utils.Utility;

/**
 * @author: jwhan
 * @createTime: 2022/05/30 9:34 PM
 * @description:
 */
public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder> {
    private List<Hourly> hourlies;

    public HourlyWeatherAdapter(List<Hourly> hourlyWeather) {
        this.hourlies = hourlyWeather;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_weather_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hourly hourly = hourlies.get(position);
        holder.hour.setText(Utility.getTimeFromTimeStamp(hourly.getDt()) + ":00");
        holder.temperature.setText((int) (hourly.getTemp() - Constants.KELVINS) + "℃");
        holder.weather_icon.setImageResource(Utility.getWeatherArtImage(hourly.getWeather().get(0).getId()));
    }

    @Override
    public int getItemCount() {
        return hourlies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView hour;
        TextView temperature;
        ImageView weather_icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hour = itemView.findViewById(R.id.hour);
            temperature = itemView.findViewById(R.id.temperature);
            weather_icon = itemView.findViewById(R.id.weather_icon);
        }
    }
}
