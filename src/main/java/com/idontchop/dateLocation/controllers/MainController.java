package com.idontchop.dateLocation.controllers;

import java.net.NetworkInterface;
import java.net.SocketException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

/**
 * API:
 * 
 *  Location manipulation:
 *  
 *  /location/{username}/{type}/{lat}/{lng}
 *  
 *  POST/PUT - All arguments required
 *  DELETE - username - required, type - optional, lat/lng - optional
 *  GET - username - required, type - optional, lat/lng - optional (likely rarely used)
 *  
 *  Searches:
 *  
 *  
 *  
 *  type:
 *  	HOME: - only one in database, POST/PUT always replaces
 *  	SEARCH / LOC: multiple
 *  
 *  SEARCH is when a user wants to be found by people in a location, LOC is when
 *  the user's geolocation is reported in that location.
 *  
 * @author nathan
 *
 */
@RestController
public class MainController {
	
	@Value("${server.port}")
	private String serverPort;
	
	@Value("${spring.application.name}")
	private String appName;
	
	@RequestMapping ("/helloWorld")
	public String helloWorld () {
		String serverAddress,serverHost;
		try {
			serverAddress = NetworkInterface.getNetworkInterfaces().nextElement()
					.getInetAddresses().nextElement().getHostAddress();
		} catch (SocketException e) {
			serverAddress = e.getMessage();
		}
		try {
			serverHost = NetworkInterface.getNetworkInterfaces().nextElement()
					.getInetAddresses().nextElement().getHostName();
		} catch (SocketException e) {
			serverHost = e.getMessage();
		}

		return "{\n\"message\": \"Hello from " + appName + "\",\n"
				+ "\"host\": \"" + serverHost + "\",\n"
				+ "\"address\": \"" + serverAddress + "\",\n"
				+ "\"port\": \"" + serverPort + "\",\n"
				+ "\n}";
	}
	
	@RequestMapping ("/getPageRequest")
	public PageRequest getPageRequest() {
		return PageRequest.of(0, 10);
	}
	

}
