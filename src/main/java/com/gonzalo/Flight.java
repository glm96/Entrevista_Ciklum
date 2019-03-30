package com.gonzalo;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Flight {

	private String fcode, departureCode, arrivalCode;
	
//	@JsonDeserialize(using = LocalDateDeserializer.class)
//	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate arrivalDate, departureDate;
	private LocalTime arrivalTime, departureTime;
	
	public Flight() {}
	
	public Flight(String fcode, String departureCode, String arrivalCode, LocalDate arrivalDate,
			LocalDate departureDate, LocalTime arrivalTime, LocalTime departureTime) {
		super();
		this.fcode = fcode;
		this.departureCode = departureCode;
		this.arrivalCode = arrivalCode;
		this.arrivalDate = arrivalDate;
		this.departureDate = departureDate;
		this.arrivalTime = arrivalTime;
		this.departureTime = departureTime;
	}

	@Override
	public String toString() {
		//return "Flight [fcode=" + fcode + ", departureCode=" + departureCode + ", arrivalCode=" + arrivalCode + "]";
		return "Flight [fcode=" + fcode + ", departureCode=" + departureCode + ", arrivalCode="
				+ arrivalCode + ", arrivalDate=" + arrivalDate + ", departureDate=" + departureDate + ", arrivalTime="
				+ arrivalTime + ", departureTime=" + departureTime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arrivalCode == null) ? 0 : arrivalCode.hashCode());
		result = prime * result + ((arrivalDate == null) ? 0 : arrivalDate.hashCode());
		result = prime * result + ((arrivalTime == null) ? 0 : arrivalTime.hashCode());
		result = prime * result + ((departureCode == null) ? 0 : departureCode.hashCode());
		result = prime * result + ((departureDate == null) ? 0 : departureDate.hashCode());
		result = prime * result + ((departureTime == null) ? 0 : departureTime.hashCode());
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
		if (arrivalTime == null) {
			if (other.arrivalTime != null)
				return false;
		} else if (!arrivalTime.equals(other.arrivalTime))
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
		if (departureTime == null) {
			if (other.departureTime != null)
				return false;
		} else if (!departureTime.equals(other.departureTime))
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

	public LocalDate getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(LocalDate arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public LocalDate getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDate departureDate) {
		this.departureDate = departureDate;
	}

	public LocalTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(LocalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public LocalTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(LocalTime departureTime) {
		this.departureTime = departureTime;
	}
	
	
}
