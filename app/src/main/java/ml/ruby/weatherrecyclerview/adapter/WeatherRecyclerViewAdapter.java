package ml.ruby.weatherrecyclerview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ml.ruby.weatherrecyclerview.R;
import ml.ruby.weatherrecyclerview.model.onecall.Daily;
import ml.ruby.weatherrecyclerview.utils.Constants;

/**
 * @author: jwhan
 * @createTime: 2022/04/27 10:40 AM
 * @description:
 */
public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "WeatherRecyclerViewAdap";
    List<Daily> weatherBeanList;
    private String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    public WeatherRecyclerViewAdapter(List<Daily> weatherBeanList) {
        this.weatherBeanList = weatherBeanList;
    }

    @NonNull
    @Override
    public WeatherRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_recyclerview_items,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRecyclerViewAdapter.ViewHolder holder, int position) {
        Daily daily = weatherBeanList.get(position);
        Date date = new Date(daily.getSunrise() * 1000L);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        holder.dayOfWeek.setText(days[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        // Handle the temperature
        String temp = "";
//        double min = NumberOperation.round(daily.getTemp().getMin() - Constants.KELVINS, 0);
//        double max = NumberOperation.round(daily.getTemp().getMax() - Constants.KELVINS, 0);
        // Integer maybe better for reading
        long min = Math.round(daily.getTemp().getMin() - Constants.KELVINS);
        long max = Math.round(daily.getTemp().getMax() - Constants.KELVINS);
        temp += min + "℃ - " + max + "℃";
        holder.temperature.setText(temp);
        // The weather forecast description
        holder.forecast.setText(daily.getWeather().get(0).getDescription());
    }

    @Override
    public int getItemCount() {
        return weatherBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayOfWeek;
        TextView temperature;
        TextView forecast;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfWeek = itemView.findViewById(R.id.dayOfWeek);
            temperature = itemView.findViewById(R.id.temperature_range);
            forecast = itemView.findViewById(R.id.forecast);
        }
    }
}
