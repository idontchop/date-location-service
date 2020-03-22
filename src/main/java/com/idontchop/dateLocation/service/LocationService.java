package com.idontchop.dateLocation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.idontchop.dateLocation.dto.UsersPage;
import com.idontchop.dateLocation.entities.Location;
import com.idontchop.dateLocation.repositories.LocationRepository;

import com.mongodb.client.result.DeleteResult;

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
		Point point = new Point(-1.0, 1.0);
		
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
		
		Point loc = new Point ( lat,lng );
		
		Location location = new Location(username, loc);
		
		locationRepository.save(location);
		
		return location;
		
	}
	
	/**
	 * returns a mongo Query which can then be used with mongoTemplate for a find
	 * or delete.
	 * 
	 * Ok, here is hte plan: 
	 * 
	 * Pretty much start over. Have this buildQuery accept non-distance stuff
	 * Have a buildNearQuery which accepts a query and distance.
	 * Perhaps setup a builder pattern... (nah, just need the two, queries already builder)
	 * 
	 * @param username
	 * @param type
	 * @param latArg
	 * @param lngArg
	 * @return
	 */
	private Query buildQuery ( String username, String type, String latArg, String lngArg, int km ) {
		
		Query query = new Query();
		
		// username
		if ( username != null ) {
			query.addCriteria(Criteria.where("username").is(username));
		}
		
		// Check for lat/lng
		double lat,lng;
		try {
			lat = Double.parseDouble(latArg);
			lng = Double.parseDouble(lngArg);
			
			Point point = new Point ( lng, lat );
			if ( km > 0 ) {
				Distance distance = new Distance(km, Metrics.KILOMETERS);
				
			}
			else query.addCriteria(Criteria.where("loc").is(point));
		} catch ( NumberFormatException e ) {
			// expected if not passed
		} catch (NullPointerException e ) {
			// expected if not passed
		}
		
		// Check for type		
		try {
			Location.PingType pingType = Location.PingType.valueOf(type);
			query.addCriteria(Criteria.where("pingType").is(pingType));
		} catch (IllegalArgumentException e ) {
			// expected if not passed
		} catch (NullPointerException e ) {
			// expected if not passed
		}
		
		return query;
	}
	
	/**
	 * Deletes a location from the database. Only the username is required and will delete
	 * all records with that username. Each additional parameter supplied will do a more
	 * specific delete.
	 * 
	 * @param username
	 * @param type
	 * @param lat
	 * @param lng
	 * @return #number of deleted records
	 */
	public long deleteLocation ( String username, String type, String latArg, String lngArg ) {
						
		DeleteResult count = mongoTemplate.remove(buildQuery(username, type, latArg, lngArg, 0));
		
		return count.getDeletedCount();
		
	}
	
	/**
	 * Overloaded delete Locations
	 */
	public long deleteLocation ( String username ) { return deleteLocation ( username, null, null, null);}
	public long deleteLocation (String username, String type) { return deleteLocation ( username, type, null, null);}
	public long deleteLocationWithCoords (String username, String latArg, String lngArg) {
		return deleteLocation (username, null, latArg, lngArg);	}
	
	
	
	/**
	 * Number of records that match arguments.
	 * 
	 * @param username
	 * @param type
	 * @param latArg
	 * @param lngArg
	 * @return
	 */
	public long countLocation ( String username, String type, String latArg, String lngArg ) {
		return mongoTemplate.count(buildQuery(username, type, latArg, lngArg, 0), Location.class);
	}
	
	public Page<Location> getLocationWithUsername ( String username, String type, String latArg, String lngArg, int page) 
		throws IllegalArgumentException {
		
		Pageable p = PageRequest.of(page, 100);
		
		Point point = null;
		Location.PingType pingType = null;
		
		if ( username == null ) throw new IllegalArgumentException ("locationService.getLocation passed null");
		
		// Check for lat/lng
		double lat,lng;
		try {
			lat = Double.parseDouble(latArg);
			lng = Double.parseDouble(lngArg);
			
			point = new Point ( lat, lng );
			
		} catch ( NumberFormatException e ) {
			// expected if not passed
		} catch (NullPointerException e ) {
			// expected if not passed
		}
		
		// Check for type		
		try {
			pingType = Location.PingType.valueOf(type);
		} catch (IllegalArgumentException e ) {
			// expected if not passed
		} catch (NullPointerException e ) {
			// expected if not passed
		}
		
		if ( point == null && pingType == null) {
			return locationRepository.findByUsername(username, p);
		} else if ( point == null && pingType != null ) {
			return locationRepository.findByUsernameAndLoc(username, point, p);
		} else if ( point != null && pingType == null ) {
			return locationRepository.findByUsernameAndPingType(username, pingType, p);
		} else {
			return locationRepository.findByUsernameAndPingTypeAndLoc(username, pingType, point, p);
		}
		
	}
	
	/** Overloaded getLocationWithUsername */
	public Page<Location> getLocationWithUsername ( String username ) { return getLocationWithUsername (username,null, null, null, 0); }
	public Page<Location> getLocationWithUsername ( String username, int page ) { return getLocationWithUsername(username,page); }

	/**
	 * Gets all from a specific location, doesn't do ranges.
	 * 
	 * @param latArg
	 * @param lngArg
	 * @param type
	 * @return
	 */
	public Page<Location> getLocation ( String latArg, String lngArg, String type, int page) {
		
		Pageable p = PageRequest.of(page, 100);
		// Check for lat/lng
		double lat,lng;
		Point point;
		try {
			lat = Double.parseDouble(latArg);
			lng = Double.parseDouble(lngArg);
			
			point = new Point ( lat, lng );
			
		} catch ( NumberFormatException e ) {
			throw new IllegalArgumentException ("locationService.getLocation requires two args");
		} catch (NullPointerException e ) {
			throw new IllegalArgumentException ("locationService.getLocation requires two args");
		}
		
		Location.PingType pingType = null;
		try {
			pingType = Location.PingType.valueOf(type);
		} catch (IllegalArgumentException e ) {
			// expected if not passed
		} catch (NullPointerException e ) {
			// expected if not passed
		}
		
		if ( pingType == null ) {
			return locationRepository.findByLoc(point, p);
		} else {
			return locationRepository.findByLocAndPingType(point, pingType, p);
		}
		
	}
	
	/**
	 * Main search.
	 * 
	 * Most users will make a search at a specific geolocation. types, page is optional
	 * 
	 * @param latArg
	 * @param lngArg
	 * @param km
	 * @param type
	 * @param page
	 * @return
	 */
	public Page<Location> getLocationNear ( String latArg, String lngArg, int km, String[] types, int page ) {
		
		/*
		 * This will have to be a mongoTemplate.
		 * https://stackoverflow.com/questions/29030542/pagination-with-mongotemplate
		 * 
		 * query.with() can be set with Pageable, then pageable managed manually
		 * 
		 * 
		 */
		double lat,lng;
		Point point;
		try {
			lat = Double.parseDouble(latArg);
			lng = Double.parseDouble(lngArg);
			
			point = new Point ( lng, lat );
			
		} catch ( NumberFormatException e ) {
			throw new IllegalArgumentException ("locationService.getLocationNear requires two args");
		} catch (NullPointerException e ) {
			throw new IllegalArgumentException ("locationService.getLocationNear requires two args");
		}
		
		Distance distance = new Distance ( km, Metrics.KILOMETERS);
		
		if ( types != null && types.length > 0) {
			
		}
		
		
		
		return null;
		
	}
	
	public UsersPage getUsersNear ( String latArg, String lngArg, int km, String [] types, int page) {
		return null;
	}
}
