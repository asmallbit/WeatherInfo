package ml.ruby.weatherrecyclerview.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import ml.ruby.weatherrecyclerview.R;

/**
 * @author: jwhan
 * @createTime: 2022/05/09 9:11 PM
 * @description:
 */
public class Utility {
    /**
     * This method is used to get the drawable resource from the weather id code
     *
     * @param weatherId The id of the weather, check this link https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
     * @return the drawable resource for code xxx
     */
    public static int getWeatherArtImage(int weatherId) {
        if (weatherId >= 200 && weatherId < 300) {
            return R.drawable.art_thunderstorm;
        } else if (weatherId >= 300 && weatherId < 400) {
            return R.drawable.art_shower_rains;
        } else if (weatherId >= 500 && weatherId < 600) {
            return R.drawable.art_rain;
        } else if (weatherId >= 600 && weatherId < 700) {
            return R.drawable.art_snow;
        } else if (weatherId >= 700 && weatherId < 800) {
            return R.drawable.art_mist;
        } else if (weatherId == 800) {
            return R.drawable.art_clear_sky;
        } else if (weatherId == 801) {
            return R.drawable.art_few_clouds;
        } else if (weatherId == 802) {
            return R.drawable.art_scattered_clouds;
        } else {
            return R.drawable.art_broken_clouds;
        }
    }

    // 从时间戳里获取对应的小时
    public static String getTimeFromTimeStamp(long timeStamp) {
        Date date = new Date(timeStamp * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        return simpleDateFormat.format(date);
    }
}
