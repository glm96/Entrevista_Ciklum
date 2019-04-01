package com.gonzalo;

/*
 * This class stores information about a location on Earth.  Locations are
 * specified using latitude and longitude.  The class includes a method for
 * computing the distance between two locations.
 *
 * This implementation is based off of the example from Stuart Reges at 
 * the University of Washington.
 */

/**
 * Class for storing 
 * @author matthewalangreen
 *
 */

public class GeoLocation 
{
    // Earth radius in miles
    public static final double RADIUS = 3963.1676;  

    private double latitude;
    private double longitude;

    /**
     * Constructs a geo location object with given latitude and longitude
     */
    public GeoLocation() {}
    
    public GeoLocation(double theLatitude, double theLongitude) 
    {
        latitude = theLatitude;
        longitude = theLongitude;
    }

    public boolean checkCorrect() {
    	if(this.latitude>90 || this.latitude < -90)
    		return false;
    	if(this.longitude>180 || this.longitude<-180)
    		return false;
    	return true;
    }
    
    /**
     * Returns the latitude of this geo location
     * @return latitude latitude of the location
     */
    public double getLatitude() 
    {
        return latitude;
    }

    /**
     * returns the longitude of this geo location
     * @return longitude longitude of the location
     */
    public double getLongitude() 
    {
        return longitude;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		GeoLocation other = (GeoLocation) obj;
		if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
			return false;
		return true;
	}

	/**
	 * Returns the distance between two GeoLocation entities
	 * @param other GeoLocation to compare with
	 * @return distance in miles
	 */
    public double distanceFrom(GeoLocation other) 
    {
        double lat1 = Math.toRadians(latitude);
        double long1 = Math.toRadians(longitude);
        double lat2 = Math.toRadians(other.latitude);
        double long2 = Math.toRadians(other.longitude);
        // apply the spherical law of cosines with a triangle composed of the
        // two locations and the north pole
        double theCos = Math.sin(lat1) * Math.sin(lat2) +
            Math.cos(lat1) * Math.cos(lat2) * Math.cos(long1 - long2);
        double arcLength = Math.acos(theCos);
        return arcLength * RADIUS;
    }
}