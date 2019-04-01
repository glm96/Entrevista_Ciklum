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

@WebServlet(
    name = "deleteServlet",
    urlPatterns = {"/delete"}
)
public class DeleteService extends HttpServlet {

  @Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
	  
	  	boolean success = true;
	  	String msg = "Failed to delete";
	  	
		//super.doDelete(req, resp);
		
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		
		ObjectifyService.register(HolidayPackage.class);
		long id = Long.parseLong(req.getParameter("id"));
		try {
		HolidayPackage hp = ofy().load().type(HolidayPackage.class).id(id).now();
		if(hp.checkCorrect())
			ofy().delete().type(HolidayPackage.class).id(id).now();
		}catch(Exception e) {success = false;}
		if(success) {
			msg = "Done";
		}
		resp.getWriter().print(msg);
		
	}
}