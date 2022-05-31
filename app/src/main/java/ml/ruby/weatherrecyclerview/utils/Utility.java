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

    public static String convertDegreeToCardinalDirection(int directionInDegrees) {
        String cardinalDirection = null;
        if ((directionInDegrees >= 348.75) && (directionInDegrees <= 360) ||
                (directionInDegrees >= 0) && (directionInDegrees <= 11.25)) {
            cardinalDirection = "N";
        } else if ((directionInDegrees >= 11.25) && (directionInDegrees <= 33.75)) {
            cardinalDirection = "NNE";
        } else if ((directionInDegrees >= 33.75) && (directionInDegrees <= 56.25)) {
            cardinalDirection = "NE";
        } else if ((directionInDegrees >= 56.25) && (directionInDegrees <= 78.75)) {
            cardinalDirection = "ENE";
        } else if ((directionInDegrees >= 78.75) && (directionInDegrees <= 101.25)) {
            cardinalDirection = "E";
        } else if ((directionInDegrees >= 101.25) && (directionInDegrees <= 123.75)) {
            cardinalDirection = "ESE";
        } else if ((directionInDegrees >= 123.75) && (directionInDegrees <= 146.25)) {
            cardinalDirection = "SE";
        } else if ((directionInDegrees >= 146.25) && (directionInDegrees <= 168.75)) {
            cardinalDirection = "SSE";
        } else if ((directionInDegrees >= 168.75) && (directionInDegrees <= 191.25)) {
            cardinalDirection = "S";
        } else if ((directionInDegrees >= 191.25) && (directionInDegrees <= 213.75)) {
            cardinalDirection = "SSW";
        } else if ((directionInDegrees >= 213.75) && (directionInDegrees <= 236.25)) {
            cardinalDirection = "SW";
        } else if ((directionInDegrees >= 236.25) && (directionInDegrees <= 258.75)) {
            cardinalDirection = "WSW";
        } else if ((directionInDegrees >= 258.75) && (directionInDegrees <= 281.25)) {
            cardinalDirection = "W";
        } else if ((directionInDegrees >= 281.25) && (directionInDegrees <= 303.75)) {
            cardinalDirection = "WNW";
        } else if ((directionInDegrees >= 303.75) && (directionInDegrees <= 326.25)) {
            cardinalDirection = "NW";
        } else if ((directionInDegrees >= 326.25) && (directionInDegrees <= 348.75)) {
            cardinalDirection = "NNW";
        } else {
            cardinalDirection = "?";
        }
        return cardinalDirection;
    }
}
