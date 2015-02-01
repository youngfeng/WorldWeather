package com.worldWeather.app.util;

public interface HttpCallbackListener {

	void onfinish(String response) ;
	void onError(Exception e) ;
}
