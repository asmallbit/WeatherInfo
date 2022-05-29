package ml.ruby.weatherrecyclerview.model.onecall;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OneCallBean {

	@SerializedName("current")
	private Current current;

	@SerializedName("timezone")
	private String timezone;

	@SerializedName("timezone_offset")
	private int timezoneOffset;

	@SerializedName("daily")
	private List<Daily> daily;

	@SerializedName("lon")
	private double lon;

	@SerializedName("hourly")
	private List<Hourly> hourly;

	@SerializedName("minutely")
	private List<Minutely> minutely;

	@SerializedName("lat")
	private double lat;

	public void setCurrent(Current current){
		this.current = current;
	}

	public Current getCurrent(){
		return current;
	}

	public void setTimezone(String timezone){
		this.timezone = timezone;
	}

	public String getTimezone(){
		return timezone;
	}

	public void setTimezoneOffset(int timezoneOffset){
		this.timezoneOffset = timezoneOffset;
	}

	public int getTimezoneOffset(){
		return timezoneOffset;
	}

	public void setDaily(List<Daily> daily){
		this.daily = daily;
	}

	public List<Daily> getDaily(){
		return daily;
	}

	public void setLon(double lon){
		this.lon = lon;
	}

	public double getLon(){
		return lon;
	}

	public void setHourly(List<Hourly> hourly){
		this.hourly = hourly;
	}

	public List<Hourly> getHourly(){
		return hourly;
	}

	public void setMinutely(List<Minutely> minutely){
		this.minutely = minutely;
	}

	public List<Minutely> getMinutely(){
		return minutely;
	}

	public void setLat(double lat){
		this.lat = lat;
	}

	public double getLat(){
		return lat;
	}
}