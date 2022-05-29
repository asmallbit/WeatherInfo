package ml.ruby.weatherrecyclerview.model.onecall;

import com.google.gson.annotations.SerializedName;

public class Rain{

	@SerializedName("1h")
	private double jsonMember1h;

	public void setJsonMember1h(double jsonMember1h){
		this.jsonMember1h = jsonMember1h;
	}

	public double getJsonMember1h(){
		return jsonMember1h;
	}
}