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
	//ʡ�б�
	private List<Province> provincelist;
	//���б�
	private List<City> citylist;
	//���б�
	private List<County> countylist;
	//ѡ�е�ʡ��
	private Province selectedprovince;
	//ѡ�еĳ���
	private City selectedcity;
	//ѡ�еļ���
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
		
		queryProvinces();//����ʡ������
	}
	
	//��ѯȫ�����е�ʡ�����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ
	private void queryProvinces(){
		provincelist=coolweatherdb.loadProvnice();
		if(provincelist.size()>0){
			datalist.clear();
			for(Province province:provincelist){
				datalist.add(province.getProvincename());
			}
			adapter.notifyDataSetChanged();//?
			listview.setSelection(0);
			titletext.setText("�й�");
			currentlevel=LEVEL_PROVINCE;
		}else{
			String address="http://guolin.tech/api/china";
			queryFromSever(address,"province");//��������ѯ
		}
	}
	
	//��ѯѡ��ʡ�����е��У����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
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
			queryFromSever(address,"city");//��������ѯ
		}
		
	}
	//��ѯѡ���������е��أ����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
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
	
	//��������ѯ
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
					// ͨ��runOnUiThread()�����ص����̴߳����߼�
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
				// ͨ��runOnUiThread()�����ص����̴߳����߼�
				runOnUiThread(new Runnable(){
					public void run(){
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
					}
				});
			}
			
		});
	}
	
	//��ʾ���ȶԻ���
	private void showProgressDialog(){
		if(progressdialog==null){
			progressdialog=new ProgressDialog(this);
			progressdialog.setMessage("���ڼ���...");
			progressdialog.setCanceledOnTouchOutside(false);
		}
		progressdialog.show();
	}
	
	//�ر�
	private void closeProgressDialog(){
		if(progressdialog!=null){
			progressdialog.dismiss();
		}
	}
	
	//����Back���������ݵ�ǰ�ļ������жϣ���ʱӦ�÷������б�ʡ�б�����ֱ���˳���
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
