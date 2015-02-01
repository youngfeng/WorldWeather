package com.worldWeather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.worldWeather.app.model.City;
import com.worldWeather.app.model.County;
import com.worldWeather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WorldWeatherDB {
	
	public static final String DB_NAME = "world_weather.db" ;
	
	private static WorldWeatherDB worldWeatherDB ;
	
	private SQLiteDatabase db ;
	
	private WorldWeatherDB(Context context) {
		
		WorldWeatherOpenHelper dbHelper = new WorldWeatherOpenHelper(context, DB_NAME, null, 1) ;
		db = dbHelper.getWritableDatabase() ;
	}
	
	public synchronized static WorldWeatherDB getInstance(Context context) {
		if(worldWeatherDB==null) {
			worldWeatherDB = new WorldWeatherDB(context) ;
		}
		return worldWeatherDB ;
	}
	
	public void saveProvince(Province province) {
		if(province!=null) {
			ContentValues values = new ContentValues() ;
			values.put("province_name", province.getProvinceName()) ;
			values.put("province_code", province.getProvinceCode()) ;
			db.insert("Province", null, values) ;
		}
	}
	
	public List<Province> loadProvince() {
		List<Province> list = new ArrayList<Province>() ;
		Cursor cursor = db.query("Province", null, null, null, null, null, null) ;
		if(cursor.moveToFirst()) {
			do {
				Province province = new Province() ;
				province.setId(cursor.getInt(cursor.getColumnIndex("id"))) ;
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name"))) ;
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code"))) ;
				list.add(province) ;
			} while (cursor.moveToNext());
		}
		return list ;
	}
	
	/**
	 * 20142014-12-18下午9:17:52
	 * author gaolifeng
	 * 将City实例存储到数据库
	 */
	public void saveCity(City city) {
		if(city!=null) {
			ContentValues values = new ContentValues() ;
			values.put("city_name", city.getCityName()) ;
			values.put("city_code", city.getCityCode()) ;
			values.put("province_id", city.getProvinceId()) ;
			db.insert("City", null, values) ;
		}
	}
	
	/**
	 * 20142014-12-18下午9:21:07
	 * author gaolifeng
	 * 从数据库中读取某省下所有的城市信息
	 */
	public List<City> loadCities(int provinceId) {
		List<City> list = new ArrayList<City>() ; 
		Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null) ;
		if(cursor.moveToFirst()) {
			do{
				City city = new City() ;
				city.setId(cursor.getInt(cursor.getColumnIndex("id"))) ;
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name"))) ;
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code"))) ;
				list.add(city) ;
			}while(cursor.moveToNext()) ;
		}
		return list ;
	}
	
	/**
	 * 
	 * 20142014-12-18下午9:58:03
	 * author gaolifeng
	 * 将county实例存储到数据库
	 */
	public void saveCounty(County county) {
		if(county!=null) {
			ContentValues values = new ContentValues() ;
			values.put("county_name", county.getCountyName()) ;
			values.put("county_code", county.getCountyCode()) ;
			values.put("city_id", county.getCityId()) ;
			db.insert("County", null, values) ;
		}
	}
	
	/**
	 * 20142014-12-18下午10:02:47
	 * author gaolifeng
	 * 从数据库读取某城市下的所有县信息
	 */
	public List<County> loadCounties(int cityId) {
		List<County> list = new ArrayList<County>() ;
		Cursor cursor = db.query("County", null, "city_id=?", new String[]{String.valueOf(cityId)}, null, null, null) ;
		if(cursor.moveToFirst()) {
			do{
				County county = new County() ;
				county.setId(cursor.getInt(cursor.getColumnIndex("id"))) ;
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code"))) ;
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name"))) ;
				list.add(county) ;
			}while(cursor.moveToNext()) ;
		}
		return list ;
	}


}
