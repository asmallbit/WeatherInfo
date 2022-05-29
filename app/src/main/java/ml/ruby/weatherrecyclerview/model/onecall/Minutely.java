package ml.ruby.weatherrecyclerview.model.onecall;

import com.google.gson.annotations.SerializedName;

public class Minutely {

	@SerializedName("dt")
	private int dt;

	@SerializedName("precipitation")
	private double precipitation;

	public void setDt(int dt){
		this.dt = dt;
	}

	public int getDt(){
		return dt;
	}

	public void setPrecipitation(double precipitation){
		this.precipitation = precipitation;
	}

	public double getPrecipitation(){
		return precipitation;
	}
}