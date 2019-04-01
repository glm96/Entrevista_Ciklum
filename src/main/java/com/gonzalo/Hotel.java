package com.gonzalo;

import com.googlecode.objectify.annotation.Index;

/**
 * The class includes all data for a hotel, including its code, name, location 
 * and its star rating.
 * @author Gonzalo Luque
 *
 */
public class Hotel {
	private long code;
	private String name;
	@Index private short starRating;
	private GeoLocation geoLocation;
	
	/**
	 * Default constructor, used for Objectify and Jackson object management
	 */
	public Hotel() {}

	/**
	 * Constructor used for random test data generation
	 * @param code Code of the hotel
	 * @param name Name of the hotel
	 * @param starRating Rating in an scale of 0 to 5
	 * @param geoLocation Location of the hotel
	 */
	public Hotel(long code, String name, short starRating, GeoLocation geoLocation) {
		super();
		this.code = code;
		this.name = name;
		this.starRating = starRating;
		this.geoLocation = geoLocation;
	}
	
	/**
	 * Checks if every parameter on the entity is according to expected format
	 * @return Returns true if it is a correct declaration
	 */
	public boolean checkCorrect() {
		if(this.starRating<0 || this.starRating>5)
			return false;
		if(!geoLocation.checkCorrect())
			return false;
		return true;
	}
	
	/**
	  * {@inheritDoc}
	  */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (code ^ (code >>> 32));
		result = prime * result + ((geoLocation == null) ? 0 : geoLocation.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + starRating;
		return result;
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hotel other = (Hotel) obj;
		if (code != other.code)
			return false;
		if (geoLocation == null) {
			if (other.geoLocation != null)
				return false;
		} else if (!geoLocation.equals(other.geoLocation))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (starRating != other.starRating)
			return false;
		return true;
	}
	
	/**
	 * 
	 * @return code of the hotel
	 */
	public long getCode() {
		return code;
	}

	/**
	 * 
	 * @param code code of the hotel to be set
	 */
	public void setCode(long code) {
		this.code = code;
	}

	/**
	 * 
	 * @return name of the hotel
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name Name to be set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return Rating of the hotel (0-5)
	 */
	public short getStarRating() {
		return starRating;
	}

	/**
	 * 
	 * @param starRating Rating of the hotel to be set
	 */
	public void setStarRating(short starRating) {
		this.starRating = starRating;
	}

	/**
	 * 
	 * @return Location of the hotel
	 */
	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	/**
	 * 
	 * @param geoLocation Location of the hotel to be changed
	 */
	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}
	
	
}
