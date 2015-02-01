package com.worldWeather.app.activity;

import java.security.PublicKey;

import com.example.worldweather.R;
import com.worldWeather.app.util.HttpCallbackListener;
import com.worldWeather.app.util.HttpUtil;
import com.worldWeather.app.util.Utility;

import android.app.Activity;
import android.app.DownloadManager.Query;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends Activity implements OnClickListener{

	private LinearLayout weatherInfoLayout ;
	
	private TextView publishText ;
	
	private TextView cityNameText ;
	
	private TextView  temp1Text ;
	
	private TextView temp2Text ;
	
	private TextView weatherDespText ;
	
	private TextView currentDateText ;
	
	private Button switchCity ;
	
	private Button refreshWeather ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE) ;
		setContentView(R.layout.weather_layout) ;
		//初始化个控件
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout) ;
		switchCity = (Button) findViewById(R.id.switch_city) ;
		refreshWeather = (Button) findViewById(R.id.refresh_weather) ;
		switchCity.setOnClickListener(this) ;
		refreshWeather.setOnClickListener(this) ;
		publishText = (TextView) findViewById(R.id.publish_text) ;
		cityNameText = (TextView) findViewById(R.id.city_name) ;
		currentDateText = (TextView) findViewById(R.id.current_data) ;
		temp1Text = (TextView) findViewById(R.id.temp1) ;
		temp2Text = (TextView) findViewById(R.id.temp2) ;
		weatherDespText = (TextView) findViewById(R.id.weather_desp) ;
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout) ;
		String countyCode = getIntent().getStringExtra("county_code") ;
		if(!TextUtils.isEmpty(countyCode)) {
			//有县级代码时去查询天气
			publishText.setText("同步中.....") ;
			weatherInfoLayout.setVisibility(View.INVISIBLE) ;
			cityNameText.setVisibility(View.INVISIBLE) ;
			queryWeatherCode(countyCode) ;
		}else {
			showWeather();
		}
	}

	/**
	 * 查询县级代号所对应的天气
	 * @param countyCode
	 * 20152015-2-1下午4:45:35
	 * author gaolifeng
	 * void
	 */
	private void queryWeatherCode(String countyCode) {
		// TODO Auto-generated method stub
		String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml" ;
		queryFromServer(address , "countyCode") ;
	}
	
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html" ;
		queryFromServer(address, "weatherCode") ;
	}

	//查询天气代号所对应的天气
	private void queryFromServer(final String address, final String type) {
		// TODO Auto-generated method stub
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onfinish(String response) {
				// TODO Auto-generated method stub
				if("countyCode".equals(type)) {
					if(!TextUtils.isEmpty(response)) {
						String[] array = response.split("\\|") ;
						if(array!=null && array.length==2) {
							String weatherCode = array[1] ;
							queryWeatherInfo(weatherCode) ;
						}
						
					}
				}else if("weatherCode".equals(type)) {
					Utility.handleWeatherResponse(WeatherActivity.this, response) ;
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showWeather() ;
						}
					}) ;
				}
				
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						publishText.setText("同步失败") ;
					}
				}) ;
			}
		}) ;
	}
	
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this) ;
		cityNameText.setText(prefs.getString("city_name", "")) ;
		temp1Text.setText(prefs.getString("temp1", "")) ;
		temp2Text.setText(prefs.getString("temp2", "")) ;
		weatherDespText.setText(prefs.getString("weather_desp", "")) ;
		publishText.setText("今天"+prefs.getString("publish_time", "")+"发布") ;
		currentDateText.setText(prefs.getString("current_date", "")) ;
		weatherInfoLayout.setVisibility(View.VISIBLE) ;
		cityNameText.setVisibility(View.VISIBLE) ;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this,ChooseAreaActivity.class) ;
			intent.putExtra("from_weather_activity", true) ;
			startActivity(intent) ;
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("同步中....") ;
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this) ;
			String weatherCode = prefs.getString("weather_code", "") ;
			if(!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode) ;
			}
			break ;
		default:
			break;
		}
	}
}

