package com.example.coolweather.app.util;

public interface HttpCallbackListener {//���ص����񷵻صĽ��

	void onFinish(String response);
	void onError(Exception e);
}
