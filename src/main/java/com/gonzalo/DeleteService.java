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

import com.googlecode.objectify.ObjectifyService;

/**
 * Servlet for delete operations via a DELETE petition on /delete, passing object ID as an argument in URL 
 * @author Gonzalo Luque
 *
 */
@WebServlet(
    name = "deleteServlet",
    urlPatterns = {"/delete/*"}
)
public class DeleteService extends HttpServlet {

	/**
	 * Method for managing DELETE petitions. Deletes the specified entity if its id exists on the database
	 */
  @Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
	  
	  	ObjectifyService.register(HolidayPackage.class); //Register HolidayPackages in order to be able to use them with Objectify library
	  	boolean success = true; //petition status flag
	  	String msg = "Failed to delete"; // default message
	  		
	  	//Set response type as raw text
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		
		//grab id from petition
		long id = Long.parseLong(req.getParameter("id"));
		try {
		HolidayPackage hp = ofy().load().type(HolidayPackage.class).id(id).now();
		if(hp.checkCorrect())
			ofy().delete().type(HolidayPackage.class).id(id).now();
		}catch(Exception e) {success = false;}//Entity not found in database
		if(success) {
			msg = "Done";
		}
		resp.getWriter().println(msg);
		resp.getWriter().println(req.getRequestURI());
		
	}
}