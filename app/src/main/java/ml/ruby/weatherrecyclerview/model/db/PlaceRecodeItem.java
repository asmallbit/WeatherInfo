package ml.ruby.weatherrecyclerview.model.db;

import android.nfc.tech.NfcA;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author: jwhan
 * @createTime: 2022/05/16 10:25 PM
 * @description: This table is used to store the places which is starred(is_for_star=true) or shown weather info(is_for_star=false)
 */
@Entity(tableName = "places_table")
public class PlaceRecodeItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "place")
    private String place;
    @ColumnInfo(name = "latitude")
    private double lat;
    @ColumnInfo(name = "longitude")
    private double lon;
    @ColumnInfo(name = "country")
    private String country;
    @ColumnInfo(name = "state")
    private String state;
    @ColumnInfo(name = "is_for_star")
    private boolean isForStar;

    public PlaceRecodeItem(String place, double lat, double lon, String country, String state, boolean isForStar) {
        this.place = place;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
        this.state = state;
        this.isForStar = isForStar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isForStar() {
        return isForStar;
    }

    public void setForStar(boolean forStar) {
        isForStar = forStar;
    }

    public String getAreaFullName() {
        if (state != null) {
            return getPlace() + ", " + getState() + ", " + getCountry();
        } else {
            return getPlace() + ", " + getCountry();
        }
    }
}
