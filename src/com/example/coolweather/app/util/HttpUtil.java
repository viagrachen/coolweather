package com.example.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//和服务器交互
public class HttpUtil {

	public static void sendHttpRequest(final String address,final HttpCallbackListener listener){//来回调服务返回的结果
		new Thread(new Runnable(){
			public void run(){
				HttpURLConnection connection=null;
				try {
					URL url=new URL(address);//new 出一个URL对象，并传入目标的网络地址
					connection=(HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");//从服务器那里获取数据
					connection.setConnectTimeout(8000);//设置连接超时
					connection.setReadTimeout(8000);//读取超时的毫秒数
					
					InputStream in=connection.getInputStream();//获取到服务器返回的输入流
					// 下面对获取到的输入流进行读取
					BufferedReader reader=new BufferedReader(new InputStreamReader(in));
					StringBuilder response=new StringBuilder();
					String line;
					while((line=reader.readLine())!=null){
					response.append(line);
					}

					/*Message message = new Message();
					message.what = SHOW_RESPONSE;
					// 将服务器返回的结果存放到Message中
					message.obj = response.toString();
					handler.sendMessage(message);*/
					
					if(listener!=null){
						//回调onFinish()
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if(listener!=null){
						//回调onError()方法
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
