package com.example.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.R;
import com.example.coolweather.app.model.City;
import com.example.coolweather.app.model.CoolWeatherDB;
import com.example.coolweather.app.model.County;
import com.example.coolweather.app.model.Province;
import com.example.coolweather.app.util.HttpCallbackListener;
import com.example.coolweather.app.util.HttpUtil;
import com.example.coolweather.app.util.Utility;

public class ChooseAreaActivity extends Activity{

	public static final int LEVEL_PROVINCE=0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	private ProgressDialog progressdialog;
	private TextView titletext;
	private Button backbutton;
	private ListView listview;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolweatherdb;
	private List<String> datalist=new ArrayList<String>();
	//省列表
	private List<Province> provincelist;
	//市列表
	private List<City> citylist;
	//县列表
	private List<County> countylist;
	//选中的省份
	private Province selectedprovince;
	//选中的城市
	private City selectedcity;
	//选中的级别
	private int currentlevel;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		listview=(ListView)findViewById(R.id.list_view);
		titletext=(TextView)findViewById(R.id.title_text);
		backbutton=(Button)findViewById(R.id.back_button);
		
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datalist);
		listview.setAdapter(adapter);
		coolweatherdb=CoolWeatherDB.getInstance(this);
		
		backbutton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if(currentlevel==LEVEL_COUNTY){
					queryCities();
				}else if(currentlevel==LEVEL_CITY){
					queryProvinces();
				}
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0,View view,int index,long arg3){
				if(currentlevel==LEVEL_PROVINCE){
					selectedprovince=provincelist.get(index);
					queryCities();//
				}else if(currentlevel==LEVEL_CITY){
					selectedcity=citylist.get(index);
					queryCounties();
				}
			}
		});
		
		queryProvinces();//加载省级数据
	}
	
	//查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
	private void queryProvinces(){
		provincelist=coolweatherdb.loadProvnice();
		if(provincelist.size()>0){
			datalist.clear();
			for(Province province:provincelist){
				datalist.add(province.getProvincename());
			}
			adapter.notifyDataSetChanged();//?
			listview.setSelection(0);
			titletext.setText("中国");
			currentlevel=LEVEL_PROVINCE;
		}else{
			String address="http://guolin.tech/api/china";
			queryFromSever(address,"province");//服务器查询
		}
	}
	
	//查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询。
	private void queryCities(){
		citylist=coolweatherdb.loadCities(selectedprovince.getId());//
		if(citylist.size()>0){
			datalist.clear();
			for(City city:citylist){
				datalist.add(city.getCityname());
			}
			adapter.notifyDataSetChanged();
			listview.setSelection(0);
			titletext.setText(selectedprovince.getProvincename());//
			currentlevel=LEVEL_CITY;
		}else{
			String address="http://guolin.tech/api/china/"+selectedprovince.getProvincecode();
			queryFromSever(address,"city");//服务器查询
		}
		
	}
	//查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
	private void queryCounties(){
		countylist=coolweatherdb.loadcountries(selectedcity.getId());
		if(countylist.size()>0){
			datalist.clear();
			for(County county:countylist){
				datalist.add(county.getCountyname());
			}
			adapter.notifyDataSetChanged();
			listview.setSelection(0);
			titletext.setText(selectedcity.getCityname());//
			currentlevel=LEVEL_COUNTY;
		}else{
			String address="http://guolin.tech/api/china/"+selectedprovince.getProvincecode()+"/"+selectedcity.getCitycode();
			queryFromSever(address,"county");
		}
	}
	
	//服务器查询
	private void queryFromSever(String address,final String type){
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){
			public void onFinish(String response){
				boolean result=false;
				if("province".equals(type)){//?
					result=Utility.handleProvincesResponse(coolweatherdb, response);
				}else if("city".equals(type)){
					result=Utility.handleCitiesResponse(coolweatherdb, response, selectedprovince.getId());
				}else if("county".equals(type)){
					result=Utility.handleCountriesResponse(coolweatherdb, response, selectedcity.getId());
				}
				if(result){
					// 通过runOnUiThread()方法回到主线程处理逻辑
					runOnUiThread(new Runnable(){
						public void run(){
							closeProgressDialog();
							if("province".equals(type)){
								queryProvinces();
							}else if("city".equals(type)){
								queryCities();
							}else if("county".equals(type)){
								queryCounties();
							}
						}
					});
				}
			}
			
			public void onError(Exception e){
				// 通过runOnUiThread()方法回到主线程处理逻辑
				runOnUiThread(new Runnable(){
					public void run(){
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
					}
				});
			}
			
		});
	}
	
	//显示进度对话框
	private void showProgressDialog(){
		if(progressdialog==null){
			progressdialog=new ProgressDialog(this);
			progressdialog.setMessage("正在加载...");
			progressdialog.setCanceledOnTouchOutside(false);
		}
		progressdialog.show();
	}
	
	//关闭
	private void closeProgressDialog(){
		if(progressdialog!=null){
			progressdialog.dismiss();
		}
	}
	
	//捕获Back按键，根据当前的级别来判断，此时应该返回市列表、省列表、还是直接退出。
	public void onBackPressed(){
		if(currentlevel==LEVEL_COUNTY){
			queryCities();
		}else if(currentlevel==LEVEL_CITY){
			queryCounties();
		}else{
			finish();
		}
	}
}
