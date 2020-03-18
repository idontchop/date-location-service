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
		
		locationService.newLocation("1");
	}

}
