package com.idontchop.dateLocation.dto;

import java.util.List;

import org.springframework.data.domain.PageRequest;

import com.idontchop.dateLocation.entities.Location;

/**
 * Returned by endpoint. 
 * 
 * A List of usernames determined from a search param. 
 * 
 * PageRequest object.
 * 
 * @author nathan
 *
 */
public class LocationPage {

	private List<Location> users;
	
	private long numElements;
	
	private long page;
	
	private long numPages;

	public List<Location> getUsers() {
		return users;
	}

	public void setUsers(List<Location> users) {
		this.users = users;
	}
	
}
