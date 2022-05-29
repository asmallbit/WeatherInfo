package ml.ruby.weatherrecyclerview.model;

/**
 * @author: jwhan
 * @createTime: 2022/05/02 11:50 AM
 * @description: Latitude, Longitude bean
 */
public class LocationBean {
    private double latitude;
    private double longitude;

    public LocationBean(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
