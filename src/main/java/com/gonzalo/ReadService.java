package com.gonzalo;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.appengine.api.datastore.DatastoreNeedIndexException;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadException;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

@WebServlet(
    name = "readServlet",
    urlPatterns = {"/read"}
)
public class ReadService extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
	
	ObjectifyService.register(HolidayPackage.class);
	response.setContentType("text/plain");
	response.setCharacterEncoding("UTF-8");	
	
	String sort = getSorting(request);
	String IATAarr = getIATAarrival(request);
	String IATAdep = getIATAorigin(request);
	int rating = getStarRating(request);
	ArrayList<Filter> filterlist = new ArrayList<Filter>();
	Filter finalfilter = null;
	boolean firstflag = true;
	
	if(!IATAdep.equals("")) {
		Filter f1 = new FilterPredicate("inbound.departureCode",FilterOperator.EQUAL,IATAdep);
		filterlist.add(f1);
	}
	if(!IATAarr.equals("")) {
		Filter f1 = new FilterPredicate("inbound.arrivalCode",FilterOperator.EQUAL,IATAarr);
		filterlist.add(f1);
	}
	if(rating != -1) {
		Filter f1 = new FilterPredicate("lodging.starRating", FilterOperator.EQUAL,rating);
		filterlist.add(f1);
	}
	
	for(Filter filter : filterlist) {
		if(firstflag) {
			finalfilter = filter;
			firstflag = false;
		}
		else {
			finalfilter = CompositeFilterOperator.and(finalfilter,filter);
		}
	}
	
	//try {
		Query<HolidayPackage> ofyQuery;
		if(finalfilter!=null) {
			ofyQuery = ofy().load().type(HolidayPackage.class).order(sort).filter(finalfilter).limit(1000);
		}
		else {
			ofyQuery = ofy().load().type(HolidayPackage.class).order(sort).limit(1000);
		}
		List<HolidayPackage> list = ofyQuery.list();
		ObjectMapper mapper = new ObjectMapper();
		
		for(HolidayPackage hp : list)
				response.getWriter().println(mapper.writeValueAsString(hp));		
		
	//}	//catch(DatastoreNeedIndexException e) {response.getWriter().println("Unsupported query, contact admin");} //Unindexed query
		//catch(LoadException e) {response.getWriter().println("Database is getting ready, please try again later");} //
		//catch(IllegalArgumentException e) {response.getWriter().println("Datastore error");} //Empty datastore throws IllegalArgumentException
		//catch (IOException e) {e.printStackTrace();} //Error while parsing JSON

  }

private String getSorting(HttpServletRequest req) {
	String p = req.getParameter("sort");
	String res = "-inbound.departureDate";
	if(p!=null) {
		switch(p) {
		case "asctime":
			res = "inbound.departureDate";
			break;
		case "desctime":
			res = "-inbound.departureDate";
			break;
		case "ascstarrating":
			res = "lodging.starRating";
			break;
		case "descstarrating":
			res = "-lodging.starRating";
			break;
		default:		// Invalid sorting 
			break;
		}
	}
	return res;
}

private String getIATAarrival(HttpServletRequest req) {
	String p = req.getParameter("IATAarrival");
	String res = ""; //default value
	String IATAregex = "[A-Z]{3}";
	if(p!=null)
		if(p.matches(IATAregex))
			res = p;
	return res;
}

private String getIATAorigin(HttpServletRequest req) {
	String p = req.getParameter("IATAorigin");
	String res = ""; //default value
	String IATAregex = "[A-Z]{3}";
	if(p!=null)
		if(p.matches(IATAregex))
			res = p;
	return res;
}

private int getStarRating(HttpServletRequest req) {
	String p = req.getParameter("rating");
	int res = -1; //default value
	if(p!=null) {
		int temp = Integer.parseInt(p);
		if(temp>=0 && temp<=5)
			res = temp;
	}
	return res;
}

}