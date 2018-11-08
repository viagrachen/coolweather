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
	
	//将Provice实例存储到数据库
	public void saveProvice(Provice provice){
		if(provice!=null){
			ContentValues values=new ContentValues();
			values.put("provice_name", provice.getProvicename());
			values.put("provice_code", provice.getProvicecode());
			db.insert("Provice", null, values);
		}
	}
	
	//从数据库读书所有省份信息
	public List<Provice> loadProvice(){
		List<Provice> list=new ArrayList<Provice>();
		Cursor cursor=db.query("Provice", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Provice provice1=new Provice();
				provice1.setId(cursor.getInt(cursor.getColumnIndex("id")));
				provice1.setProvicename(cursor.getString(cursor.getColumnIndex("provice_name")));
				provice1.setProvicecode(cursor.getString(cursor.getColumnIndex("provice_code")));
				list.add(provice1);
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
			values.put("provice_id",city.getProviceid());
			db.insert("City", null, values);
		}
	}
	
	//从数据库读取某省下所有城市信息
	public List<City> loadCities(int proviceId){
		List<City> list=new ArrayList<City>();
		Cursor cursor=db.query("City", null, "provice_id=?", new String[] {String.valueOf(proviceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityname(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCitycode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProviceid(proviceId);//传递的参数
				list.add(city);
			}while(cursor.moveToNext());
		}
		return list;
	}
	
	//将country实例存储到数据库
	public void saveCountry(Country country){
		if(country !=null){
		ContentValues values=new ContentValues();
		values.put("country_name", country.getCountryname());
		values.put("country_code", country.getCountrycode());
		values.put("city_id", country.getCityid());
		db.insert("country", null, values);
		}
	}
	
	//读取所有country信息
	public List<Country> loadcountries(int cityId){
		List<Country> list=new ArrayList<Country>();
		Cursor cursor=db.query("Country", null, "city_id=?", new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Country country=new Country();
				country.setId(cursor.getInt(cursor.getColumnIndex("id")));
				country.setCountryname(cursor.getString(cursor.getColumnIndex("country_name")));
				country.setCountrycode(cursor.getString(cursor.getColumnIndex("country_code")));
				country.setCityid(cityId);
				list.add(country);
			}while(cursor.moveToNext());
		}
		return list;
	}
}
