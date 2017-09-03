package com.ctrip.tingting.bean;

public class CityKeyEntity {

	private String City;
	private String City_Key;
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getCity_Key() {
		return City_Key;
	}
	public void setCity_Key(String city_Key) {
		City_Key = city_Key;
	}
	@Override
	public String toString() {
		return "CityKeyEntity [City=" + City + ", City_Key=" + City_Key + "]";
	}
}
