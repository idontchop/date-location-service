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
	 * int values are supplied for comparison
	 * 
	 * @author nathan
	 *
	 */
	public enum PingType {
		SEARCH(0),
		LOC(1),
		HOME(2);
		
		private final int intValue;
		private PingType (int intValue) {
			this.intValue = intValue;
		}
		
		public int getIntValue() {
			return intValue;
		}
	};
	
	/**
	 * Sets this location's type to Search:
	 * The user searched in this location.
	 */
	public void setSearch () {
		pingType = PingType.SEARCH;
	}
	
	/**
	 * Sets this location's type to LOC:
	 * The user was pinged in this location.
	 * 
	 */
	public void setLoc () {
		pingType = PingType.LOC;
	}
	
	/**
	 * Sets this location's type to Home:
	 * The user set this location has his/her home.
	 */
	public void setHome () {
		pingType = PingType.HOME;
	}
	
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

	/**
	 * Compare to will check for nulls in the passed in loc. If a null
	 * is set, that field will not be used for hte compareto.
	 * 
	 * username must always be set.
	 * 
	 * For example, if usernames are equal but lat/lng in loc argument
	 * is set to null, all fields with username will match.
	 * 
	 * @param loc
	 * @return
	 */
	@Override
	public int compareTo ( Location loc ) {
		
		int retVal = this.username.compareTo(loc.getUsername()) * 1000;
		
		if ( loc.getPingType() != null) {
			retVal +=
		}
		
	}
}
