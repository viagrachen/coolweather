package com.example.coolweather.app.model;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.coolweather.app.db.MyDatabaseHelper;

public class CoolWeatherDB {

	public static final String DB_NAME="cool_weather"; //数据库名
	public static final int VERSION=1;//版本
	private static CoolWeatherDB coolweatherdb;
	private SQLiteDatabase db;
	
	//将构造方法私有化
	private CoolWeatherDB(Context context){
		MyDatabaseHelper dbhelper=new MyDatabaseHelper(context,DB_NAME,null,VERSION);
		//创建或打开一个现有的数据（如果数据库已存在则直接打开，否则创建一个新的数据库）并返回一个可对数据库进行读写操作的对象
		db=dbhelper.getWritableDatabase();
	}
	
	//获取CoolWeatherDB的实例,单例类,getInstance()方法来获取CoolWeatherDB 的实例,保证全局范围内只会有一个CoolWeatherDB 的实例
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolweatherdb==null){
			coolweatherdb=new CoolWeatherDB(context);
		}
		return coolweatherdb;
	}
	
	//将Provnice实例存储到数据库
	public void saveProvnice(Province province){
		if(province!=null){
			ContentValues values=new ContentValues();
			values.put("province_name", province.getProvincename());
			values.put("province_code", province.getProvincecode());
			db.insert("Province", null, values);
		}
	}
	
	//从数据库读书所有省份信息
	public List<Province> loadProvnice(){
		List<Province> list=new ArrayList<Province>();
		Cursor cursor=db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province1=new Province();
				province1.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province1.setProvincename(cursor.getString(cursor.getColumnIndex("province_name")));
				province1.setProvincecode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province1);
			}while(cursor.moveToNext());
		}
		return list;
	}
	
	//将City实例存储到数据库
	public void saveCity(City city){
		if(city!=null){
			ContentValues values=new ContentValues();
			values.put("city_name", city.getCityname());
			values.put("city_code", city.getCitycode());
			values.put("province_id",city.getProvinceid());
			db.insert("City", null, values);
		}
	}
	
	//从数据库读取某省下所有城市信息
	public List<City> loadCities(int provinceId){
		List<City> list=new ArrayList<City>();
		Cursor cursor=db.query("City", null, "province_id=?", new String[] {String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityname(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCitycode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceid(provinceId);//传递的参数
				list.add(city);
			}while(cursor.moveToNext());
		}
		return list;
	}
	
	//将county实例存储到数据库
	public void saveCounty(County county){
		if(county !=null){
		ContentValues values=new ContentValues();
		values.put("county_name", county.getCountyname());
		values.put("county_code", county.getCountycode());
		values.put("city_id", county.getCityid());
		db.insert("county", null, values);
		}
	}
	
	//读取所有county信息
	public List<County> loadcountries(int cityId){
		List<County> list=new ArrayList<County>();
		Cursor cursor=db.query("County", null, "city_id=?", new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				County county=new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyname(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountycode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityid(cityId);
				list.add(county);
			}while(cursor.moveToNext());
		}
		return list;
	}
}
