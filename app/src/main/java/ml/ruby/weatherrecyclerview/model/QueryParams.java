package ml.ruby.weatherrecyclerview.model;

import ml.ruby.weatherrecyclerview.BuildConfig;

/**
 * @author: jwhan
 * @createTime: 2022/04/28 4:41 PM
 * @description: Params bean
 */
public class QueryParams {
    public String lat;
    public String lon;
    public String lang;
    private String appId = BuildConfig.WEATHER_API_KEY;

    public QueryParams(String lat, String lon, String lang) {
        this.lat = lat;
        this.lon = lon;
        this.lang = lang;
    }

    private QueryParams() {

    }

    public String getAppId() {
        return appId;
    }
}
