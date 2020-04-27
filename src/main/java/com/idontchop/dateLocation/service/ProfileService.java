package com.idontchop.dateLocation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.idontchop.dateLocation.dto.UserProfileDto;
import com.idontchop.dateLocation.dto.nested.LatLng;
import com.idontchop.dateLocation.entities.Location;
import com.idontchop.dateLocation.repositories.LocationRepository;

@Service
public class ProfileService {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	LocationRepository locationRepository;

	public List<Location> findHomes( List<String> users ) {
		return locationRepository.findByUsernameInAndPingTypeIn(users, List.of(Location.PingType.HOME));
	}
	
	/**
	 * TODO: speed this up, very expensive queries.
	 * 
	 * @param users
	 * @return
	 */
	public Map<String, LatLng> findLatestEachType ( String user ) {
		
		Map <String, LatLng> locations = new HashMap<>();
		
		for ( Location.PingType pingType : Location.PingType.values() ) {
			Query query = new Query();		
			query.with( Sort.by (Sort.Direction.DESC, "created") );
			query.addCriteria( Criteria.where("username").is(user).and("pingType").is(pingType.toString()) ).limit(1);
			
			List<Location> locList = mongoTemplate.find(query, Location.class);
			
			if ( locList.size() == 1) {
				Location location = locList.get(0);
				locations.put(pingType.toString(), new LatLng(location.getLoc().getX(), location.getLoc().getY()) );
			}
		}
		
		return locations;
		
	}
	
	public List<UserProfileDto> getUserProfilesInList ( List<String> users ) {
		
		return users.stream().map ( user -> {
			UserProfileDto dto = new UserProfileDto();
			dto.setUsername(user);
			dto.setLocations( findLatestEachType(user) );
			return dto;
		})
		.collect(Collectors.toList());
		
	}
}
