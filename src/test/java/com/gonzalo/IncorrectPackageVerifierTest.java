package com.gonzalo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Verifies if the method for verifying HolidayPackage entities works correctly
 * @author Gonzalo Luque 
 *
 */
public class IncorrectPackageVerifierTest {

	@Test
	public void test() {
		HolidayPackage hp1 = new CreateService().createDummyHoliday();
		hp1.getLodging().setStarRating((short)1234); //incorrect star rating
		assertFalse(hp1.checkCorrect());	
	}

}
