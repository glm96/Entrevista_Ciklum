package com.gonzalo;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.googlecode.objectify.ObjectifyService;

@WebServlet(
    name = "HelloAp",
    urlPatterns = {"/testjson"}
)
public class postService extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
	
	response.setContentType("text/plain");
	response.setCharacterEncoding("UTF-8");	
	HolidayPackage holiday = createDummyHoliday();
	
	ObjectMapper mapper = new ObjectMapper();
	mapper.findAndRegisterModules();
	
	ObjectifyService.register(HolidayPackage.class);
	
	//ofy().save().entity(holiday).now();
	
	//HolidayPackage fetched2 = ofy().load().type(HolidayPackage.class).id(holiday.getId()).now();
	
	response.getWriter().print(mapper.writeValueAsString(holiday));	

  }

@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// TODO Auto-generated method stub
	//super.doPost(req, resp);
	
	HolidayPackage holiday;
	
	resp.setContentType("text/plain");
	resp.setCharacterEncoding("UTF-8");
	
	ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()); // new module, NOT JSR310Module
	
	holiday = mapper.readValue(req.getInputStream(), HolidayPackage.class);

	
	
	resp.getWriter().print(holiday.toString());
	
	
}

private HolidayPackage createDummyHoliday () {
	Flight ib, ob;
	Hotel hotel;
	double precio = Math.random()*100;
	
	ib = new Flight("SU2529", "AGP", "SVO", LocalDate.now(), LocalDate.now(), LocalTime.now(), LocalTime.MIDNIGHT);
	ob = ib;
	hotel = new Hotel((long) 12305314, "Suimeikan", (short) 5, new GeoLocation(Math.random()*100,Math.random()*100));
	
	HolidayPackage holiday = new HolidayPackage(ib,ob,hotel,precio);
	return holiday;
}

}