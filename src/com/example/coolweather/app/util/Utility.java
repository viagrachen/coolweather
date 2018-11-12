package com.example.coolweather.app.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.example.coolweather.app.model.City;
import com.example.coolweather.app.model.CoolWeatherDB;
import com.example.coolweather.app.model.County;
import com.example.coolweather.app.model.Province;

public class Utility {
//解析和处理服务器返回的省级数据
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolweatherdb,String response){
		if(!TextUtils.isEmpty(response)){
			JSONArray allProvinces;
			try {
				allProvinces = new JSONArray(response);
				for(int i=0;i<allProvinces.length();i++){
					JSONObject provinceobject=allProvinces.getJSONObject(i);
					Province province=new Province();
					province.setProvincecode(provinceobject.getString("id"));/////
					province.setProvincename(provinceobject.getString("name"));
					//将解析的数据存储到Province表
					coolweatherdb.saveProvnice(province);
			}
				return true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	//解析和处理服务器返回的市级数据
	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolweatherdb,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
			JSONArray allcities;
			try {
				allcities = new JSONArray(response);
				for(int i=0;i<allcities.length();i++){
					JSONObject cityobject=allcities.getJSONObject(i);
					City city=new City();
					city.setCitycode(cityobject.getString("id"));
					city.setCityname(cityobject.getString("name"));
					city.setProvinceid(provinceId);
					//将解析出来的数据存储到City表
					coolweatherdb.saveCity(city);
				} 
				return true;
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				return false;
	}
	
	//解析返回的县级数据
	public synchronized static boolean handleCountriesResponse(CoolWeatherDB coolweatherdb,String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			JSONArray allcounties;
			try {
				allcounties = new JSONArray(response);
				for(int i=0;i<allcounties.length();i++){
					JSONObject countyobject=allcounties.getJSONObject(i);
					County county=new County();
					county.setCountycode(countyobject.getString("id"));
					county.setCountyname(countyobject.getString("name"));
					county.setCityid(cityId);
					//将解析出的数据存储到county
					coolweatherdb.saveCounty(county);
					}
				return true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}			
		return true;
	}
}
