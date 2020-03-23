package com.idontchop.dateLocation.controllers;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.idontchop.dateLocation.dto.LocationPage;
import com.idontchop.dateLocation.dto.SuccessMessage;
import com.idontchop.dateLocation.entities.Location;
import com.idontchop.dateLocation.service.LocationService;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

/**
 * API:
 * 
 *  Location manipulation:
 *  
 *  /api/location/{username}/{type}/{lat}/{lng}
 *  
 *  POST/PUT - All arguments required
 *  DELETE - username - required, type - optional, lat/lng - optional
 *  GET - username - required, type - optional, lat/lng - optional (likely rarely used)
 *  
 *  Searches:
 *  
 *  /api/search-location/{type}/{lat}/{lng}/{km}  
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
	
	private final String ANY = "any";
	
	@Autowired
	LocationService locationService;
	
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
	
	/**
	 * location management
	 */
	
	@DeleteMapping ( value = { "/api/location/{username}/{type}/{lat}/{lng}/{km}",
			"/api/location/{username}/{type}/{lat}/{lng}"
	})
	public SuccessMessage deleteLocation ( @PathVariable ( name = "username", required=true) String username,
			@PathVariable ( name = "lat", required=true)  String lat, 
			@PathVariable ( name = "lng", required=true)  String lng, 
			@PathVariable  ( name = "type", required=true) String type,
			@PathVariable ( name = "km", required = false) Optional<Integer> km ) {
		
		// check for "any" being passed, service looks for nulls
		if (username.equalsIgnoreCase(ANY)) username = null; 
		if (type.equalsIgnoreCase(ANY)) type = null;
		if ( lat.equalsIgnoreCase(ANY) || lng.equalsIgnoreCase(ANY)) {
			lat = null; lng = null;
		}
		
		
		long numDeleted = locationService
				.deleteLocation(username, type, lat, lng, km.orElse(Integer.valueOf(0)));
		
		if ( numDeleted == 0 ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		return SuccessMessage.message("Deleted: " + numDeleted );
		
	}
	
	@RequestMapping ( value = "/api/location/{username}/{type}/{lat}/{lng}",
			method = { RequestMethod.POST, RequestMethod.PUT } )
	public Location postLocation ( @PathVariable ( name = "username", required=true) String username,
			@PathVariable ( name = "lat", required=true)  String lat, 
			@PathVariable ( name = "lng", required=true)  String lng, 
			@PathVariable  ( name = "type", required=true) String type) {		
		
		return locationService.newLocation(username, lat, lng, type);
	}
	
	/**
	 * Used when searching for a username specifically and therefor will allow
	 * large returns.
	 */
	@GetMapping ( value = { "/api/location/{username}/{type}",
			"/api/location/{username}/{type}/{lat}/{lng}",
			"/api/location/{username}"
	})
	public Page<Location> getLocation ( @PathVariable ( name = "username", required=true) String username,
			@PathVariable ( name = "lat", required=false)  String lat, 
			@PathVariable ( name = "lng", required=false)  String lng, 
			@PathVariable  ( name = "type", required=false) String type,
			@RequestParam ( name = "page", required=false, defaultValue="0") int page) {
		
		return locationService.getLocationWithUsername(username, type, lat, lng, page);
	}
	
	@GetMapping ( value = {"/api/search-location/{types}/{lat}/{lng}/{km}"} )
	public LocationPage getLocationsNear (@PathVariable ( name = "lat", required=false)  String lat, 
			@PathVariable ( name = "lng", required=false)  String lng, 
			@PathVariable  ( name = "types", required=false) List<String> types,
			@PathVariable ( name = "km", required=true) int km,
			@RequestParam ( name = "page", required=false, defaultValue="0") int page
			) {
		
		return locationService.getLocationsNear(lat, lng, km, types, page);
	
	}


}
