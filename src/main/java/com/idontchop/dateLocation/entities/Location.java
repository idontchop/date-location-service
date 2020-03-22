package com.idontchop.dateLocation.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
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
		NULL(0),
		SEARCH(1),
		LOC(2),
		HOME(3);
		
		private final int intValue;
		private PingType (int intValue) {
			this.intValue = intValue;
		}
		
		public int getIntValue() {
			return intValue;
		}
	};
	
	/**
	 * Sets this locations type to Null:
	 * This would be used to ignore this in comparisons.
	 */
	public void setAsNull () {
		pingType = PingType.NULL;
	}
	/**
	 * Sets this location's type to Search:
	 * The user searched in this location.
	 */
	public void setAsSearch () {
		pingType = PingType.SEARCH;
	}
	
	/**
	 * Sets this location's type to LOC:
	 * The user was pinged in this location.
	 * 
	 */
	public void setAsLoc () {
		pingType = PingType.LOC;
	}
	
	/**
	 * Sets this location's type to Home:
	 * The user set this location has his/her home.
	 */
	public void setAsHome () {
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
	public int compareTo ( Location loc ) {
		
		if ( this.username == null || loc.getUsername() == null ) return -1;
		
		int retVal = this.username.compareTo(loc.getUsername()) * 1000;
		
		if ( loc.getPingType() != PingType.NULL) {
			retVal += ((this.pingType.getIntValue() - loc.getPingType().getIntValue()) * 100);
		}
		
		if ( loc.getLoc() != null ) {
			retVal += this.loc.equals(loc.getLoc()) ? 0 : 10;
		}
		
		return retVal;
		
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", username=" + username + ", pingType=" + pingType + ", loc=" + loc
				+ ", created=" + created + "]";
	}
	
	
	
}
