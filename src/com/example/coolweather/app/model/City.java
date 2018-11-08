package com.example.coolweather.app.model;

public class City {

	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	public int getProviceid() {
		return proviceid;
	}
	public void setProviceid(int proviceid) {
		this.proviceid = proviceid;
	}
	private String cityname;
	private String citycode;
	private int proviceid;
}
