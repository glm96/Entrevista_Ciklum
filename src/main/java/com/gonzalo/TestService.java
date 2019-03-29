package com.gonzalo;

import java.time.LocalDate;
import static com.googlecode.objectify.ObjectifyService.ofy;
import java.time.LocalTime;

import javax.servlet.annotation.WebServlet;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

@WebServlet(
	    name = "testEngine",
	    urlPatterns = {"/test"}
	)
public class TestService {

	public TestService() {
		Flight ib, ob;
		Hotel hotel;
		double precio = 2.35;
		
		ib = new Flight("SU2529", "AGP", "SVO", LocalDate.now(), LocalDate.now(), LocalTime.now(), LocalTime.MIDNIGHT);
		ob = ib;
		hotel = new Hotel((long) 12305314, "Suimeikan", (short) 5, new GeoLocation(124,80));
		
		HolidayPackage holiday = new HolidayPackage(ib,ob,hotel,precio);
		
		ObjectifyService.register(Flight.class);
		ObjectifyService.register(Hotel.class);
		ObjectifyService.register(HolidayPackage.class);
		
		ofy().save().entity(holiday).now();
	}
}
