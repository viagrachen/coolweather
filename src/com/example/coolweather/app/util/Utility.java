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
//�����ʹ�����������ص�ʡ������
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
					//�����������ݴ洢��Province��
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
	
	//�����ʹ�����������ص��м�����
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
					//���������������ݴ洢��City��
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
	
	//�������ص��ؼ�����
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
					//�������������ݴ洢��county
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
