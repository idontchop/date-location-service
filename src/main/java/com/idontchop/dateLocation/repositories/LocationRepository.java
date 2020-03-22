package com.idontchop.dateLocation.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.idontchop.dateLocation.entities.Location;

@RepositoryRestResource(collectionResourceRel = "location", path = "location")
public interface LocationRepository extends MongoRepository<Location, String> {

	Page<Location> findByUsernameAndPingTypeInAndLocIn( String username, List<Location.PingType> pingType, List<Point> points, Pageable p);
	Page<Location> findByUsernameAndPingTypeAndLoc( String username, Location.PingType pingType, Point point, Pageable p);
	Page<Location> findByUsernameAndPingTypeIn ( String username, List<Location.PingType> pingType, Pageable p);
	Page<Location> findByUsernameAndPingType ( String username, Location.PingType pingType, Pageable p);
	Page<Location> findByUsernameAndLocIn ( String username, List<Point> points, Pageable p);
	Page<Location> findByUsernameAndLoc ( String username, Point point, Pageable p);
	Page<Location> findByUsername(String username, Pageable p);
	
	Page<Location> findByLocIn( List<Point> points, Pageable p);
	Page<Location> findByLoc ( Point point, Pageable p);
	Page<Location> findByLocAndPingType ( Point point, Location.PingType pingType, Pageable p);
	Page<Location> findByLocInAndPingTypeIn ( List<Location> points, List<Location.PingType> pingTypes, Pageable p);
	
	Page<Location> findByLocNear (Point point, Distance distance, Pageable p);
}
