package com.idontchop.dateLocation.dto.nested;

public class LatLng {
	
	public LatLng () {}

	private String lat;
	private String lng;
	
	
	public LatLng(String lat, String lng) {
		this.lat = lat;
		this.lng = lng;
	}
	
	public LatLng (double lat, double lng) {
		this.lat = String.valueOf(lat);
		this.lng = String.valueOf(lng);
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	
	
}
