package com.gonzalo;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class HolidayPackage {
	@Id
	private Long id;
	private Flight inbound, outbound;
	private Hotel lodging;
	private double price;
	
	public HolidayPackage(Flight inbound, Flight outbound, Hotel lodging, double price) {
		super();
		this.inbound = inbound;
		this.outbound = outbound;
		this.lodging = lodging;
		this.price = price;
	}
	
	public String testString() {return Double.toString(price);}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Flight getInbound() {
		return inbound;
	}

	public void setInbound(Flight inbound) {
		this.inbound = inbound;
	}

	public Flight getOutbound() {
		return outbound;
	}

	public void setOutbound(Flight outbound) {
		this.outbound = outbound;
	}

	public Hotel getLodging() {
		return lodging;
	}

	public void setLodging(Hotel lodging) {
		this.lodging = lodging;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inbound == null) ? 0 : inbound.hashCode());
		result = prime * result + ((lodging == null) ? 0 : lodging.hashCode());
		result = prime * result + ((outbound == null) ? 0 : outbound.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
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
		HolidayPackage other = (HolidayPackage) obj;
		if (inbound == null) {
			if (other.inbound != null)
				return false;
		} else if (!inbound.equals(other.inbound))
			return false;
		if (lodging == null) {
			if (other.lodging != null)
				return false;
		} else if (!lodging.equals(other.lodging))
			return false;
		if (outbound == null) {
			if (other.outbound != null)
				return false;
		} else if (!outbound.equals(other.outbound))
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		return true;
	}

	
	
}
