package com.gonzalo;

import java.util.Date;
import com.googlecode.objectify.annotation.Index;

/**
 * This class contains all data of a flight, including flight code,
 *  airport (arrival and departure) codes and date/time of arrival and departure
 * @author Gonzalo Luque
 *
 */
public class Flight {

	private String fcode;
	@Index private String departureCode, arrivalCode;
	private Date arrivalDate;
	@Index private Date departureDate;

	/**
	 * Default constructor, used for Objectify and Jackson object management
	 */
	public Flight() {}
	
	/**
	 * Constructor used for random test data generation.
	 * @param fcode Flight code of the flight
	 * @param departureCode Departure airport code
	 * @param arrivalCode Arrival airport code
	 * @param arrivalDate Arrival flight time
	 * @param departureDate Departure flight time
	 */
	public Flight(String fcode, String departureCode, String arrivalCode, Date arrivalDate,
			Date departureDate) {
		super();
		this.fcode = fcode;
		this.departureCode = departureCode;
		this.arrivalCode = arrivalCode;
		this.arrivalDate = arrivalDate;
		this.departureDate = departureDate;
	}

	/**
	 * Checks if every parameter on the entity is according to expected format
	 * @return Returns true if it is a correct declaration
	 */
	public boolean checkCorrect() {
		String fcoderegex = "^([A-Z]{2}|[A-Z]\\d|\\d[A-Z])[1-9](\\d{1,3})?$", IATAregex = "[A-Z]{3}";
		if(!fcode.matches(fcoderegex))
			return false;
		if(!departureCode.matches(IATAregex))
			return false;
		if(!arrivalCode.matches(IATAregex))
			return false;
		if(arrivalDate.compareTo(departureDate)<0)
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
		result = prime * result + ((arrivalCode == null) ? 0 : arrivalCode.hashCode());
		result = prime * result + ((arrivalDate == null) ? 0 : arrivalDate.hashCode());
		result = prime * result + ((departureCode == null) ? 0 : departureCode.hashCode());
		result = prime * result + ((departureDate == null) ? 0 : departureDate.hashCode());
		result = prime * result + ((fcode == null) ? 0 : fcode.hashCode());
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
		Flight other = (Flight) obj;
		if (arrivalCode == null) {
			if (other.arrivalCode != null)
				return false;
		} else if (!arrivalCode.equals(other.arrivalCode))
			return false;
		if (arrivalDate == null) {
			if (other.arrivalDate != null)
				return false;
		} else if (!arrivalDate.equals(other.arrivalDate))
			return false;
		if (departureCode == null) {
			if (other.departureCode != null)
				return false;
		} else if (!departureCode.equals(other.departureCode))
			return false;
		if (departureDate == null) {
			if (other.departureDate != null)
				return false;
		} else if (!departureDate.equals(other.departureDate))
			return false;
		if (fcode == null) {
			if (other.fcode != null)
				return false;
		} else if (!fcode.equals(other.fcode))
			return false;
		return true;
	}

	/**
	 * 
	 * @return Flight code of this specific flight
	 */
	public String getFcode() {
		return fcode;
	}

	/**
	 * 
	 * @param fcode Flight code to be set
	 */
	public void setFcode(String fcode) {
		this.fcode = fcode;
	}

	/**
	 * 
	 * @return Departure airport's IATA code
	 */
	public String getDepartureCode() {
		return departureCode;
	}

	/**
	 * 
	 * @param departureCode IATA code to be set for the departure airport
	 */
	public void setDepartureCode(String departureCode) {
		this.departureCode = departureCode;
	}

	/**
	 * 
	 * @return Arrival airport's IATA code
	 */
	public String getArrivalCode() {
		return arrivalCode;
	}

	/**
	 * 
	 * @param arrivalCode IATA code to be set for the arrival airport
	 */
	public void setArrivalCode(String arrivalCode) {
		this.arrivalCode = arrivalCode;
	}

	/**
	 * 
	 * @return Date and time of arrival
	 */
	public Date getArrivalDate() {
		return arrivalDate;
	}
	
	/**
	 * 
	 * @param arrivalDate Date and time of arrival to be set
	 */
	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	/**
	 * 
	 * @return Date and time of departure
	 */
	public Date getDepartureDate() {
		return departureDate;
	}

	/**
	 * 
	 * @param departureDate Date and time of arrival to be set
	 */
	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}
	
}
