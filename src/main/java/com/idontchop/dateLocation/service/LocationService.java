package com.idontchop.dateLocation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.idontchop.dateLocation.entities.Location;
import com.idontchop.dateLocation.repositories.LocationRepository;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

@Service
public class LocationService {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	LocationRepository locationRepository;
	
	/**
	 * Creates default (test) point with supplied username 
	 * 
	 * @param username
	 * @return
	 */
	public Location newLocation ( String username ) {
		Point point = new Point(new Position(-1.0, 1.0));
		
		Location location = new Location();
		
		location.setLoc(point);
		location.setUsername(username);
		
		locationRepository.save(location);
		
		return location;
		
	}
	
	/**
	 * Receives lat and lng and saves a new point
	 * 
	 * @param username
	 * @param lat
	 * @param lng
	 * @return
	 */
	public Location newLocation ( String username, double lat, double lng ) {
		
		Point loc = new Point ( new Position(lat,lng));
		
		Location location = new Location(username, loc);
		
		locationRepository.save(location);
		
		return location;
		
	}

}
