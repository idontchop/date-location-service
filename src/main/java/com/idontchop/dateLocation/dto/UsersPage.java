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
public class UsersPage {

	List<Location> users;
	
	PageRequest pageRequest;

	public List<Location> getUsers() {
		return users;
	}

	public void setUsers(List<Location> users) {
		this.users = users;
	}

	public PageRequest getPageRequest() {
		return pageRequest;
	}

	public void setPageRequest(PageRequest pageRequest) {
		this.pageRequest = pageRequest;
	}
	
	
}
