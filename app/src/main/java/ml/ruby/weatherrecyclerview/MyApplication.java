package ml.ruby.weatherrecyclerview;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import ml.ruby.weatherrecyclerview.utils.Constants;

/**
 * @author: jwhan
 * @createTime: 2022/09/06 8:57 PM
 * @description:
 */
public class MyApplication extends Application {
    private static Context context;
    private static SharedPreferences preference;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        preference = getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, MODE_PRIVATE);
    }


    public static Context getContext() {
        return context;
    }

    public static SharedPreferences getPreference() {
        return preference;
    }
}
