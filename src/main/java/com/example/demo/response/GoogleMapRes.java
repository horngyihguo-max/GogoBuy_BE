package com.example.demo.response;

public class GoogleMapRes extends BasicRes{

	private double lng;
	
	private double lat;

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public GoogleMapRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GoogleMapRes(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public GoogleMapRes(int code, String message, double lng, double lat) {
		super(code, message);
		this.lng = lng;
		this.lat = lat;
	}


	
	
}
