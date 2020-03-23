package com.idontchop.dateLocation.dto;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.GeoResults;

import com.idontchop.dateLocation.entities.Location;

/**
 * Returned by endpoint. 
 * 
 * A List of usernames determined from a search param. 
 * 
 * PageRequest object.
 * 
 * Setters deploy builder pattern
 * 
 * @author nathan
 *
 */
public class LocationPage {
	
	LocationPage () {}

	private GeoResults<Location> results;
	
	private long numElements;
	
	private long page;
	
	private long numPages;
	
	public static LocationPage build () {
		 return new LocationPage();
	}
	
	public LocationPage setPageInfo (long numElements, long page, long perPage) {
		this.numElements = numElements;
		this.page = page;
		this.numPages = (numElements / perPage );
		return this;
	}

	public GeoResults<Location> getResults() {
		return results;
	}

	public LocationPage setResults(GeoResults<Location> results) {
		this.results = results;
		return this;
	}

	public long getNumElements() {
		return numElements;
	}

	public LocationPage setNumElements(long numElements) {
		this.numElements = numElements;
		return this;
	}

	public long getPage() {
		return page;
	}

	public LocationPage setPage(long page) {
		this.page = page;
		return this;
	}

	public long getNumPages() {
		return numPages;
	}

	public LocationPage setNumPages(long numPages) {
		this.numPages = numPages;
		return this;
	}
	
	
	
}
