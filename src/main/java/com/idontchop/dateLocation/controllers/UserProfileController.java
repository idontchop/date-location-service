package com.idontchop.dateLocation.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.idontchop.dateLocation.dto.UserProfileDto;
import com.idontchop.dateLocation.service.ProfileService;

/**
 * Responsible for UserProfileDto calls
 * 
 * @author nathan
 *
 */
@RestController
public class UserProfileController {
	
	@Autowired
	ProfileService profileService;
	
	@GetMapping ("/api/profile/{names}")
	public List<UserProfileDto> getHomeLocs ( @PathVariable (name = "names", required = true) List<String> names) {
		return profileService.getUserProfilesInList(names);
	}

}
