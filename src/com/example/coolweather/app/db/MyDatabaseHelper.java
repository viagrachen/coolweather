package com.example.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper{//������

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
	
	//Context����Ҫ�в��ܶ����ݿ���в��������ݿ�������ѯ���ݵ�ʱ�򷵻�һ���Զ����Cursor�����ݿ�İ汾��
	public MyDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
			mContext = context;
			}
	
	public void onCreate(SQLiteDatabase db) {//��д������
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
	Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//����
	}
}
