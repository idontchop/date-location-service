package com.idontchop.dateLocation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.idontchop.dateLocation.service.LocationService;

@SpringBootTest
class DateLocationApplicationTests {
	
	@Autowired
	LocationService locationService;

	@Test
	void contextLoads() {
	}
	
	@Test
	void testMakeDefault () {
		
		for ( int c = 2; c < 20; c = c + 2 ) {
			locationService.newLocation(Integer.toString(c), 36.1699, 115.1398);
		}
	}

}
