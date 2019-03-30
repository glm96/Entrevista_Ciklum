package com.gonzalo;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;

@WebServlet(
    name = "HelloAppEngine",
    urlPatterns = {"/hello"}
)
public class getService extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
	
	response.setContentType("text/plain");
	response.setCharacterEncoding("UTF-8");	
	
	Flight ib, ob;
	Hotel hotel;
	double precio = 2.35;
	
	ib = new Flight("SU2529", "AGP", "SVO", LocalDate.now(), LocalDate.now(), LocalTime.now(), LocalTime.MIDNIGHT);
	ob = ib;
	hotel = new Hotel((long) 12305314, "Suimeikan", (short) 5, new GeoLocation(124,80));
	
	HolidayPackage holiday = new HolidayPackage(ib,ob,hotel,precio);
	
	
	ObjectifyService.register(HolidayPackage.class);
	
	ofy().save().entity(holiday).now();
	
	HolidayPackage fetched2 = ofy().load().type(HolidayPackage.class).id(holiday.getId()).now();
	
	response.getWriter().print(fetched2.toString());
	

  }
}