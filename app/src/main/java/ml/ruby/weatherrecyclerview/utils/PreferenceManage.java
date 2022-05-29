package ml.ruby.weatherrecyclerview.utils;

import android.content.SharedPreferences;

/**
 * @author: jwhan
 * @createTime: 2022/09/06 9:40 PM
 * @description:
 */
public class PreferenceManage {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public PreferenceManage(SharedPreferences preferences) {
        this.preferences = preferences;
        editor = preferences.edit();
    }

    public void putString(String value, String key) {
        editor.putString(value, key);
        editor.apply();
    }

    public String getString(String value) {
        return preferences.getString(value, "");
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.apply();
    }

    public float getFloat(String value) {
        return preferences.getFloat(value, 0);
    }
}
