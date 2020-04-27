package com.idontchop.dateLocation.dto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.idontchop.dateLocation.dto.nested.LatLng;

public class UserProfileDto {
	
	private String username;			// Not editable

	// Location
	private String lat;
	private String lng;
	private String locType;
	
	private Map<String, LatLng> locations;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName () {
		return username;
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
	public String getLocType() {
		return locType;
	}
	public void setLocType(String locType) {
		this.locType = locType;
	}

	public Map<String, LatLng> getLocations() {
		return locations;
	}
	public void setLocations(Map<String, LatLng> locations) {
		this.locations = locations;
	}
	public void addLocation ( String key, String lat, String lng ) {
		if ( locations == null ) {
			locations = new HashMap<>();
		}
		
		locations.put(key, new LatLng(lat,lng));
	}
}
