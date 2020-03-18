package com.idontchop.dateLocation.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.mongodb.client.model.geojson.Point;

public class Location {
	
	public Location () {}
	
	public Location (String username, Point loc) {
		this.username = username;
		this.loc = loc;
	}
	
	@Id
	private String id;
	
	// provided by JWT Token
	@Indexed
	public String username;
	
	/**
	 * enum for classifying this location
	 * 
	 * SEARCH - the user searched in this location
	 * LOC - the user was pinged in this location
	 * HOME - where the user considers home (and wants to be found)
	 * 
	 * @author nathan
	 *
	 */
	public enum PingType {
		SEARCH,
		LOC,
		HOME
	};
	
	@Indexed
	private PingType pingType = PingType.LOC;
	
	private Point loc;
	
	private Date created = new Date();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public PingType getPingType() {
		return pingType;
	}

	public void setPingType(PingType pingType) {
		this.pingType = pingType;
	}

	public Point getLoc() {
		return loc;
	}

	public void setLoc(Point loc) {
		this.loc = loc;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	
}
