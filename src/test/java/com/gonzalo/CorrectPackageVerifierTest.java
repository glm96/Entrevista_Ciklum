package com.gonzalo;

import static org.junit.Assert.*;


import org.junit.Test;

/**
 * Verifies if the method for creating random test data works correctly
 * @author Gonzalo Luque 
 *
 */
public class CorrectPackageVerifierTest {

	
	@Test
	public void test() {	
		HolidayPackage hp1 = new CreateService().createDummyHoliday();
		assertTrue(hp1.checkCorrect());					
	}

}
