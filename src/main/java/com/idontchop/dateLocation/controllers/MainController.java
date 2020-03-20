package com.idontchop.dateLocation.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	@RequestMapping ("/helloWorld")
	public String helloWorld () {
		return "{\n\"message\": \"Hello from Location Service\"\n}";
	}

}
