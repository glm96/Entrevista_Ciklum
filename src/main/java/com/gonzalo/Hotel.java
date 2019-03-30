package com.gonzalo;

public class Hotel {
	private long code;
	private String name;
	private short starRating;
	private GeoLocation geoLocation;
	
	public Hotel() {}
	
	@Override
	public String toString() {
		return "Hotel [code=" + code + ", name=" + name + ", starRating=" + starRating + ", geoLocation="
				+ geoLocation + "]";
	}

	public Hotel(long code, String name, short starRating, GeoLocation geoLocation) {
		super();
		this.code = code;
		this.name = name;
		this.starRating = starRating;
		this.geoLocation = geoLocation;
	}

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
	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getStarRating() {
		return starRating;
	}

	public void setStarRating(short starRating) {
		this.starRating = starRating;
	}

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}
	
	
}
