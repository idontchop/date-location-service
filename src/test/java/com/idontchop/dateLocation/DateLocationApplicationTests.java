package com.idontchop.dateLocation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;

import com.idontchop.dateLocation.entities.Location;
import com.idontchop.dateLocation.repositories.LocationRepository;
import com.idontchop.dateLocation.service.LocationService;


@SpringBootTest
class DateLocationApplicationTests {
	
	@Autowired
	LocationService locationService;
	
	@Autowired
	LocationRepository locationRepository;

	@Test
	void contextLoads() {
	}
	
	void testMakeDefault () {
		
		for ( int c = 1; c < 4; c = c + 2 ) {
			locationService.newLocation(Integer.toString(c), 115.1398, 36.1688);
		}
	}
	
	@Test
	void testCompareLocation () {
		
		Location testLocation = new Location();
		testLocation.setUsername("Test");
		testLocation.setAsHome();
		testLocation.setLoc(new Point (1.0,1.0));
		
		Location[] variableLocation = new Location[4];
		for ( int i = 0; i < 4; i++ ) { 
			variableLocation[i] = new Location();
			variableLocation[i].setUsername("Test");
		}
		
		//1
		assertTrue ( variableLocation[0].getLoc() == null);
		variableLocation[0].setAsNull();
		assertTrue ( variableLocation[0].getPingType() == Location.PingType.NULL);
		
		//2
		variableLocation[1].setAsHome();
		assertTrue( variableLocation[1].getLoc() == null);
		
		//3
		variableLocation[2].setAsNull();
		assertTrue( variableLocation[2].getLoc() == null);
		
		//4
		variableLocation[3].setUsername("Test");
		variableLocation[3].setAsSearch();
		variableLocation[3].setLoc(new Point ( 1.0,1.0));
		System.out.println(testLocation.compareTo(variableLocation[3]));
		
		
		// Comparisons
		assertEquals( 0, testLocation.compareTo(variableLocation[0]));
		assertEquals ( 0, testLocation.compareTo(variableLocation[1]));
		assertEquals ( 0, testLocation.compareTo(variableLocation[2]));
		assertTrue ( testLocation.compareTo(variableLocation[3]) != 0);
		
		
		
	}
	
	
	void testPageFind () {
		Pageable p = PageRequest.of(0,10);
		
		String username = "1";
		List<Point> points = new ArrayList<>();
			points.add( new Point ( 36.1699,115.1398 ) );
			points.add( new Point ( -1,1  ));
		List<Location.PingType> pingType = new ArrayList<>();
			pingType.add(Location.PingType.LOC);
			pingType.add(Location.PingType.HOME);
		
		
		//Page<Location> l = locationRepository.findByUsernameAndPingTypeInAndLocIn(username, pingType, points, p);
		//Page<Location> l = locationRepository.findByUsername(username, p);
		//Page<Location> l = locationRepository.findByUsernameAndLocIn(username, points, p);
		Page<Location> l = locationRepository.findByUsernameAndPingTypeIn(username, pingType, p);
		
		assertTrue (l.hasContent());
		assertEquals(1,l.getTotalPages());
		assertEquals(3,l.getTotalElements());
		
		List<Location> list =  l.getContent();
		assertEquals(3,list.size());
		
		list.forEach(e -> System.out.println(e.toString()));
	}
	
	@Test
	void testGeo () {
		
		Point point = new Point ( 115.0, 36.0 );
		Distance distance = new Distance ( 25, Metrics.KILOMETERS);
		
		Page<Location> p = locationRepository.findByLocNear(point, distance,PageRequest.of(0, 100));
		assertTrue (p.getTotalElements() > 0);
	}

}
