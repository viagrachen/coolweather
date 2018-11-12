package com.example.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper{//抽象类

	public static final String CREATE_PROVINCE="create table province(" +
			"id integer primary key autoincrement," +
			"province_name text," +
			"province_code text)";
	
	public static final String CREATE_CITY="create table city(" +
			"id integer primary key autoincrement," +
			"city_name text," +
			"city_code text," +
			"province_id integer)";
	
	public static final String CREATE_COUNTY="create table county(" +
			"id integer primary key autoincrement," +
			"county_name text," +
			"county_code text," +
			"city_id integer)";
	
	private Context mContext;
	
	//Context必须要有才能对数据库进行操作，数据库名，查询数据的时候返回一个自定义的Cursor，数据库的版本号
	public MyDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
			mContext = context;
			}
	
	public void onCreate(SQLiteDatabase db) {//重写，创建
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
	Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//升级
	}
}
