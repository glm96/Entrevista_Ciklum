package com.gonzalo;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.googlecode.objectify.annotation.Index;

public class Flight {

	private String fcode;
	@Index private String departureCode, arrivalCode;
	private Date arrivalDate;
	@Index private Date departureDate;

	public Flight() {}
	
	public Flight(String fcode, String departureCode, String arrivalCode, Date arrivalDate,
			Date departureDate) {
		super();
		this.fcode = fcode;
		this.departureCode = departureCode;
		this.arrivalCode = arrivalCode;
		this.arrivalDate = arrivalDate;
		this.departureDate = departureDate;
	}

	public boolean checkCorrect() {
		String fcoderegex = "^([A-Z]{2}|[A-Z]\\d|\\d[A-Z])[1-9](\\d{1,3})?$", IATAregex = "[A-Z]{3}";
		if(!fcode.matches(fcoderegex))
			return false;
		if(!departureCode.matches(IATAregex))
			return false;
		if(!arrivalCode.matches(IATAregex))
			return false;
		/*if(arrivalDate.compareTo(departureDate)>=0)
			return false;*/
		return true;
		
	}
	
	@Override
	public String toString() {
		//return "Flight [fcode=" + fcode + ", departureCode=" + departureCode + ", arrivalCode=" + arrivalCode + "]";
		return "Flight [fcode=" + fcode + ", departureCode=" + departureCode + ", arrivalCode="
				+ arrivalCode + ", arrivalDate=" + arrivalDate + ", departureDate=" + departureDate + "]";
	}

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

	public String getFcode() {
		return fcode;
	}

	public void setFcode(String fcode) {
		this.fcode = fcode;
	}

	public String getDepartureCode() {
		return departureCode;
	}

	public void setDepartureCode(String departureCode) {
		this.departureCode = departureCode;
	}

	public String getArrivalCode() {
		return arrivalCode;
	}

	public void setArrivalCode(String arrivalCode) {
		this.arrivalCode = arrivalCode;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}
	
}
