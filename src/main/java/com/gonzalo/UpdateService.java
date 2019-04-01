package com.gonzalo;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

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
import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadException;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

@WebServlet(
    name = "updateServlet",
    urlPatterns = {"/update"}
)
public class UpdateService extends HttpServlet {

  @Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
	  	ObjectifyService.register(HolidayPackage.class);
		HolidayPackage holiday;
		Long id;
		
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		
		String s = req.getParameter("id");
		if(s!= null) 
			id = Long.parseLong(s);
		else
			id = new Long(0);
		
		try{
			ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()); 
			holiday = mapper.readValue(req.getInputStream(), HolidayPackage.class);
			
			if(holiday.checkCorrect() && id!=0) {
				// Transaction used for updating the db
				ofy().transact(() -> {
					ofy().load().type(HolidayPackage.class).id(id).now(); //Check if it exists on the db
					holiday.setId(id); //Assure id is the one specified on URL
				    ofy().save().entity(holiday);
				});
				resp.getWriter().print("Done");
			}
			else
				resp.getWriter().print("WRONG FORMAT");	
		}catch(JsonParseException e) {resp.getWriter().print("WRONG FORMAT");}  //Invalid or no JSON on body
		catch(Exception e) {resp.getWriter().print("That record doesn't exist");} //Non existent entity
	}



}