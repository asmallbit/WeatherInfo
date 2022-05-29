package ml.ruby.weatherrecyclerview.utils;

import java.util.Locale;

/**
 * @author: jwhan
 * @createTime: 2022/05/02 5:57 PM
 * @description: Get the language on users' device
 */
public class GetLanguage {

    // Open weather api document: https://openweathermap.org/api/one-call-api#multi
    public static String getLanguage() {
        String language;
        String locale = Locale.getDefault().toString();
        if (locale.contains("zh_")) {
            if (locale.substring(0, 5).equalsIgnoreCase("zh_cn")) {
                language = "zh_cn";
            } else {
                language = "zh_tw";
            }
        } else if (locale.contains("pt_")) {
            if (locale.substring(0, 5).equalsIgnoreCase("pt_br")) {
                language = "pt_br";
            } else {
                language = "pt";
            }
        }
        language = locale.substring(0, 2);
        return language;
    }
}
