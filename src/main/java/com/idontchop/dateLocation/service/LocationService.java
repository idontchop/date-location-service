package com.idontchop.dateLocation.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.idontchop.dateLocation.dto.LocationPage;
import com.idontchop.dateLocation.entities.Location;
import com.idontchop.dateLocation.repositories.LocationRepository;

import com.mongodb.client.result.DeleteResult;

@Service
public class LocationService {
	
	private final long PAGESIZE 	=	500;	// Size of each page, this should be fairly large,
												//too large for person, good for api that will be reduced
	
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
		return newLocation (username, 1.0, 1.0, "LOC");		
	}
	
	public Location newLocation ( String username, double lat, double lng ) {
		return newLocation (username, lat, lng, "LOC");
	}
	
	public Location newLocation ( String username, String latArg, String lngArg, String type ) {
		double lat = Double.parseDouble(latArg);
		double lng = Double.parseDouble(lngArg);
		return newLocation ( username, lat, lng, type);
	}
	
	/**
	 * Receives lat and lng and saves a new point
	 * 
	 * @param username
	 * @param lat
	 * @param lng
	 * @return
	 */
	public Location newLocation ( String username, double lat, double lng, String type ) {
		
		Point loc = new Point ( lng, lat );
		
		Location location = new Location(username, loc);
		
		location.setPingType(Location.PingType.valueOf(type));
		
		locationRepository.save(location);
		
		return location;
		
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
	public long deleteLocation ( String username, String type, String lat, String lng, int km ) {
		
		Query query = null;
		try {
			query = buildQuery ( username, type );
		} catch (IllegalArgumentException e ) {
			// if two nulls are passed:
			query = new Query();
		}
		
		try {			
			
			query.addCriteria(Criteria.where("loc").near(pointFromCoords(lat,lng))
					.maxDistance(distanceFromKm(km)));
			
		} catch (IllegalArgumentException e) {
			// this will fail if lat/lng are null
		}
				
		// If all arguments are wrong, the query could still be null
		if (query == null) throw new IllegalArgumentException (
				"service deleteLocation received: " + username + "-" + type + "-" +
				"-" + lat + "-" + lng + "-" + km);
		
		// delete
		DeleteResult count = mongoTemplate.remove(query, Location.class);
		
		return count.getDeletedCount();
		
	}
	
	/**
	 * Builds a NearQuery with the supplied point and distance parameters.
	 * 
	 * This is where the nearQuery is instantiated. Adding a Query and page
	 * will be done after the return from this method.
	 * 
	 * @param latArg
	 * @param lngArg
	 * @param km
	 * @return
	 */
	private NearQuery buildNearQuery ( String latArg, String lngArg, int km ) throws IllegalArgumentException {
		
		return buildNearQuery ( pointFromCoords( latArg, lngArg), km);	
	}
	
	private NearQuery buildNearQuery ( Point point, int km ) {
		
		NearQuery nearQuery = NearQuery.near(point).maxDistance(distanceFromKm(km));	
		return nearQuery;
		
	}
	
	/**
	 * Returns null if strings not parsed.
	 * 
	 * @param latArg
	 * @param lngArg
	 * @return
	 */
	private Point pointFromCoords ( String latArg, String lngArg ) throws IllegalArgumentException {
		
		double lat,lng;
		try {
			lat = Double.parseDouble(latArg);
			lng = Double.parseDouble(lngArg);
			
			Point point = new Point ( lng, lat );
			
			return point;
			
		} catch ( NumberFormatException e ) {
			throw new IllegalArgumentException ("pointFromCoords received: " + latArg + "-" + "lngArg");
		} catch (NullPointerException e ) {
			throw new IllegalArgumentException ("pointFromCoords received: " + latArg + "-" + "lngArg");
		}
	}
	
	/** convert km to radius, radius is 111.12 km */
	private double distanceFromKm ( int km ) {
		return km/111.12;
	}
	
	/**
	 * returns a mongo Query which can then be used with mongoTemplate for a find
	 * or delete.
	 * 
	 * 
	 * @param username
	 * @param type
	 * @param latArg
	 * @param lngArg
	 * @return
	 */
	private Query buildQuery ( String username, List<String> type ) throws IllegalArgumentException {
		
		Query query = new Query();

		// username
		if ( username != null ) {
			query.addCriteria(Criteria.where("username").is(username));
		}
				
		// Check for type		
		try {
			
			List<Location.PingType>pingTypeList = new ArrayList<>();
			type.forEach( e -> {
				pingTypeList.add(Location.PingType.valueOf(e));
			});
			
			query.addCriteria(Criteria.where("pingType").in(pingTypeList));
			
		} catch (IllegalArgumentException e ) {
			throw new IllegalArgumentException ("buildQuery received: " + username + "-" + type.toString());
		} catch (NullPointerException e ) {
			throw new IllegalArgumentException ("buildQuery received: " + username + "-" + type.toString());
		}
		
		return query;
	}
	
	/** Overloaded for single type */
	private Query buildQuery ( String username, String type ) throws IllegalArgumentException {
		List<String> typeList = new ArrayList<>();
		typeList.add(type);
		return buildQuery ( username, typeList);
	}
	

	
	
	/**
	 * Number of records that match arguments.
	 * 
	 * @param username
	 * @param type
	 * @param latArg
	 * @param lngArg
	 * @return
	 */
	public long countLocationWithUsername ( String username, String type, String latArg, String lngArg ) 
			throws IllegalArgumentException {
		
		Query query = buildQuery(username,type);
		query.addCriteria(Criteria.where("loc").near(pointFromCoords(latArg,lngArg)));
		
		return mongoTemplate.count(query, Location.class);
	}
	
	public long countLocation ( String latArg, String lngArg, int km, List<String> type ) 
			throws IllegalArgumentException {
		
		
		Query query = buildQuery ( null, type );
		
		query.addCriteria(
				Criteria.where("loc")
				.near( pointFromCoords(latArg, lngArg) )
				.maxDistance( distanceFromKm(km) )
				);
		return mongoTemplate.count(query, Location.class);
		
		/* test code
		Query query = new Query();
		query.addCriteria(Criteria.where("loc").near(new Point(1.0,1.0)));
		return mongoTemplate.count(query, Location.class);*/
		
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
	
	
	public LocationPage getLocationsNear ( String latArg, String lngArg, int km, List<String> types, int page) {
		
		NearQuery nearQuery = buildNearQuery(latArg, lngArg, km).skip(page * PAGESIZE).limit(PAGESIZE);
		nearQuery.query(buildQuery(null,types));
		
		// Total results needed for page count
		long total = countLocation(latArg, lngArg, km, types);
		
		GeoResults<Location> results = mongoTemplate.geoNear(nearQuery, Location.class);
		
		// Build LocationPage for transfer
		return LocationPage.build().setResults(results).setPageInfo(total, page, PAGESIZE);
		
	}
}
