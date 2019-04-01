package com.gonzalo;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;

/**
 * Servlet for creating random test data for its use in testing
 * @author Gonzalo Luque
 *
 */
@SuppressWarnings("serial")
@WebServlet(
    name = "createServlet",
    urlPatterns = {"/create"}
)
public class CreateService extends HttpServlet {

	/**
	 * Manages GET petitions on /create, generating random data for its use in testing.
	 */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
	  ObjectifyService.register(HolidayPackage.class);
	  for(int i = 0; i<10;i++) {
	  HolidayPackage holiday = createDummyHoliday();
	  ofy().save().entity(holiday).now();
	  }
  }
  
  /**
	 * Test method for generating random HolidayPackage instances
	 * @return Semi-random instance of HolidayPackage
	 */
	private HolidayPackage createDummyHoliday () {
		Flight ib, ob;
		Hotel hotel;
		double precio = 1.11;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		HolidayPackage holiday = new HolidayPackage();
		try {
			Random rand = new Random();
			int fecha = rand.nextInt(20)+1;
			String s1 = Integer.toString(fecha), s2 = Integer.toString(fecha+1);
			Date date1 = sdf.parse(s1+"/06/19 10:34:22");
			Date date2 = sdf.parse(s2+"/06/19 10:34:22");
			ib = new Flight("SU2529", "AGP", "SVO", date1, date2);
			ob = ib;
			hotel = new Hotel((long) 12305314, "Suimeikan", (short) rand.nextInt(5), new GeoLocation(Math.random()*100,Math.random()*100));
			holiday = new HolidayPackage(ib,ob,hotel,precio);
		}catch(Exception e) {e.printStackTrace();}
		
		return holiday;
	}

}