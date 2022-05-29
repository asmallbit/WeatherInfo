package ml.ruby.weatherrecyclerview.model.onecall;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Daily {

	@SerializedName("moonset")
	private int moonset;

	@SerializedName("rain")
	private double rain;

	@SerializedName("sunrise")
	private int sunrise;

	@SerializedName("temp")
	private Temp temp;

	@SerializedName("moon_phase")
	private double moonPhase;

	@SerializedName("uvi")
	private double uvi;

	@SerializedName("moonrise")
	private int moonrise;

	@SerializedName("pressure")
	private int pressure;

	@SerializedName("clouds")
	private int clouds;

	@SerializedName("feels_like")
	private FeelsLike feelsLike;

	@SerializedName("wind_gust")
	private double windGust;

	@SerializedName("dt")
	private int dt;

	@SerializedName("pop")
	private double pop;

	@SerializedName("wind_deg")
	private int windDeg;

	@SerializedName("dew_point")
	private double dewPoint;

	@SerializedName("sunset")
	private int sunset;

	@SerializedName("weather")
	private List<Weather> weather;

	@SerializedName("humidity")
	private int humidity;

	@SerializedName("wind_speed")
	private double windSpeed;

	public void setMoonset(int moonset){
		this.moonset = moonset;
	}

	public int getMoonset(){
		return moonset;
	}

	public void setRain(double rain){
		this.rain = rain;
	}

	public double getRain(){
		return rain;
	}

	public void setSunrise(int sunrise){
		this.sunrise = sunrise;
	}

	public int getSunrise(){
		return sunrise;
	}

	public void setTemp(Temp temp){
		this.temp = temp;
	}

	public Temp getTemp(){
		return temp;
	}

	public void setMoonPhase(double moonPhase){
		this.moonPhase = moonPhase;
	}

	public double getMoonPhase(){
		return moonPhase;
	}

	public void setUvi(double uvi){
		this.uvi = uvi;
	}

	public double getUvi(){
		return uvi;
	}

	public void setMoonrise(int moonrise){
		this.moonrise = moonrise;
	}

	public int getMoonrise(){
		return moonrise;
	}

	public void setPressure(int pressure){
		this.pressure = pressure;
	}

	public int getPressure(){
		return pressure;
	}

	public void setClouds(int clouds){
		this.clouds = clouds;
	}

	public int getClouds(){
		return clouds;
	}

	public void setFeelsLike(FeelsLike feelsLike){
		this.feelsLike = feelsLike;
	}

	public FeelsLike getFeelsLike(){
		return feelsLike;
	}

	public void setWindGust(double windGust){
		this.windGust = windGust;
	}

	public double getWindGust(){
		return windGust;
	}

	public void setDt(int dt){
		this.dt = dt;
	}

	public int getDt(){
		return dt;
	}

	public void setPop(double pop){
		this.pop = pop;
	}

	public double getPop(){
		return pop;
	}

	public void setWindDeg(int windDeg){
		this.windDeg = windDeg;
	}

	public int getWindDeg(){
		return windDeg;
	}

	public void setDewPoint(double dewPoint){
		this.dewPoint = dewPoint;
	}

	public double getDewPoint(){
		return dewPoint;
	}

	public void setSunset(int sunset){
		this.sunset = sunset;
	}

	public int getSunset(){
		return sunset;
	}

	public void setWeather(List<Weather> weather){
		this.weather = weather;
	}

	public List<Weather> getWeather(){
		return weather;
	}

	public void setHumidity(int humidity){
		this.humidity = humidity;
	}

	public int getHumidity(){
		return humidity;
	}

	public void setWindSpeed(double windSpeed){
		this.windSpeed = windSpeed;
	}

	public double getWindSpeed(){
		return windSpeed;
	}
}