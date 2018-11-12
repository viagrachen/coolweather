package com.example.coolweather.app.util;

public interface HttpCallbackListener {//来回调服务返回的结果

	void onFinish(String response);
	void onError(Exception e);
}
