package com.gonzalo;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.googlecode.objectify.ObjectifyService;

/**
 * Servlet for CREATE operations via a POST petition on /create with a HolidayPackage JSON on its body
 * @author Gonzalo Luque
 *
 */
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
   * Manages POST petitions on /create, inserting a new entity on the DB, provided a valid JSON is 
   * passed via the petition body
   */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		ObjectifyService.register(HolidayPackage.class); //Register HolidayPackages in order to be able to use them with Objectify library
		HolidayPackage holiday;
		
		//Set response type as raw text
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		
		//Create an object out of the petition and try to insert it on the database
		try {
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()); 
		holiday = mapper.readValue(req.getInputStream(), HolidayPackage.class);
		
		if(holiday.checkCorrect()) {
			ofy().save().entity(holiday).now();
			resp.getWriter().print("Done"); //Format was correct and the new entity is now on the database
		}
		else
			resp.getWriter().print("WRONG FORMAT");	//Object contained some wrong attribute and will not be included on the database
		}catch(JsonParseException e) {resp.getWriter().print("WRONG FORMAT");}  //Invalid or no JSON on body
		
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