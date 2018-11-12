package com.example.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//�ͷ���������
public class HttpUtil {

	public static void sendHttpRequest(final String address,final HttpCallbackListener listener){//���ص����񷵻صĽ��
		new Thread(new Runnable(){
			public void run(){
				HttpURLConnection connection=null;
				try {
					URL url=new URL(address);//new ��һ��URL���󣬲�����Ŀ��������ַ
					connection=(HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");//�ӷ����������ȡ����
					connection.setConnectTimeout(8000);//�������ӳ�ʱ
					connection.setReadTimeout(8000);//��ȡ��ʱ�ĺ�����
					
					InputStream in=connection.getInputStream();//��ȡ�����������ص�������
					// ����Ի�ȡ�������������ж�ȡ
					BufferedReader reader=new BufferedReader(new InputStreamReader(in));
					StringBuilder response=new StringBuilder();
					String line;
					while((line=reader.readLine())!=null){
					response.append(line);
					}

					/*Message message = new Message();
					message.what = SHOW_RESPONSE;
					// �����������صĽ����ŵ�Message��
					message.obj = response.toString();
					handler.sendMessage(message);*/
					
					if(listener!=null){
						//�ص�onFinish()
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if(listener!=null){
						//�ص�onError()����
						listener.onError(e);
					}
				}
				finally{
					if(connection!=null){
						connection.disconnect();
					}
				}
			}
		}).start();
	}
	

}
