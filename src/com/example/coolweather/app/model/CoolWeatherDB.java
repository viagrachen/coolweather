package com.example.coolweather.app.model;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.coolweather.app.db.MyDatabaseHelper;

public class CoolWeatherDB {

	public static final String DB_NAME="cool_weather"; //���ݿ���
	public static final int VERSION=1;//�汾
	private static CoolWeatherDB coolweatherdb;
	private SQLiteDatabase db;
	
	//�����췽��˽�л�
	private CoolWeatherDB(Context context){
		MyDatabaseHelper dbhelper=new MyDatabaseHelper(context,DB_NAME,null,VERSION);
		//�������һ�����е����ݣ�������ݿ��Ѵ�����ֱ�Ӵ򿪣����򴴽�һ���µ����ݿ⣩������һ���ɶ����ݿ���ж�д�����Ķ���
		db=dbhelper.getWritableDatabase();
	}
	
	//��ȡCoolWeatherDB��ʵ��,������,getInstance()��������ȡCoolWeatherDB ��ʵ��,��֤ȫ�ַ�Χ��ֻ����һ��CoolWeatherDB ��ʵ��
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolweatherdb==null){
			coolweatherdb=new CoolWeatherDB(context);
		}
		return coolweatherdb;
	}
	
	//��Proviceʵ���洢�����ݿ�
	public void saveProvice(Provice provice){
		if(provice!=null){
			ContentValues values=new ContentValues();
			values.put("provice_name", provice.getProvicename());
			values.put("provice_code", provice.getProvicecode());
			db.insert("Provice", null, values);
		}
	}
	
	//�����ݿ��������ʡ����Ϣ
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
	
	//��Cityʵ���洢�����ݿ�
	public void saveCity(City city){
		if(city!=null){
			ContentValues values=new ContentValues();
			values.put("city_name", city.getCityname());
			values.put("city_code", city.getCitycode());
			values.put("provice_id",city.getProviceid());
			db.insert("City", null, values);
		}
	}
	
	//�����ݿ��ȡĳʡ�����г�����Ϣ
	public List<City> loadCities(int proviceId){
		List<City> list=new ArrayList<City>();
		Cursor cursor=db.query("City", null, "provice_id=?", new String[] {String.valueOf(proviceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityname(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCitycode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProviceid(proviceId);//���ݵĲ���
				list.add(city);
			}while(cursor.moveToNext());
		}
		return list;
	}
	
	//��countryʵ���洢�����ݿ�
	public void saveCountry(Country country){
		if(country !=null){
		ContentValues values=new ContentValues();
		values.put("country_name", country.getCountryname());
		values.put("country_code", country.getCountrycode());
		values.put("city_id", country.getCityid());
		db.insert("country", null, values);
		}
	}
	
	//��ȡ����country��Ϣ
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
