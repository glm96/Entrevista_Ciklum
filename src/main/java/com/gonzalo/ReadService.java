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

/**
 * Servlet for READ operations via a GET petition with desired parameters on /read
 * specified on the URL
 * @author Gonzalo Luque
 *
 */
@WebServlet(
    name = "readServlet",
    urlPatterns = {"/read"}
)
public class ReadService extends HttpServlet {

	/**
	 * Method for GET petition management. Gets parameters sort, IATAarr, IATAdep and rating parameters 
	 * and gives the response with JSON formatting.
	 */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
	
	ObjectifyService.register(HolidayPackage.class);//Register HolidayPackages in order to be able to use them with Objectify library
	//set response type as json
	response.setContentType("application/json");
	response.setCharacterEncoding("UTF-8");	

	//Get parameters out of the request and store them
	String sort = getSorting(request);
	String IATAarr = getIATAarrival(request);
	String IATAdep = getIATAorigin(request);
	int rating = getStarRating(request);
	
	ArrayList<Filter> filterlist = new ArrayList<Filter>();
	Filter finalfilter = null;
	boolean firstflag = true;
	
	if(!IATAdep.equals("")) { //IATAdep argument was found
		Filter f1 = new FilterPredicate("outbound.departureCode",FilterOperator.EQUAL,IATAdep);
		filterlist.add(f1);
	}
	if(!IATAarr.equals("")) { //IATAarr argument was found
		Filter f1 = new FilterPredicate("outbound.arrivalCode",FilterOperator.EQUAL,IATAarr);
		filterlist.add(f1);
	}
	if(rating != -1) {	//rating argument was found
		Filter f1 = new FilterPredicate("lodging.starRating", FilterOperator.EQUAL,rating);
		filterlist.add(f1);
	}
	
	for(Filter filter : filterlist) { //Create a single filter out of everything stored in finalfilter
		if(firstflag) {
			finalfilter = filter;
			firstflag = false;
		}
		else {
			finalfilter = CompositeFilterOperator.and(finalfilter,filter);
		}
	}
	
	try {
		Query<HolidayPackage> ofyQuery;
		if(finalfilter!=null) {
			ofyQuery = ofy().load().type(HolidayPackage.class).order(sort).filter(finalfilter).limit(1000);
		}
		else {
			ofyQuery = ofy().load().type(HolidayPackage.class).order(sort).limit(1000);
		}
		List<HolidayPackage> list = ofyQuery.list();
		ObjectMapper mapper = new ObjectMapper();
		
		response.getWriter().println(mapper.writeValueAsString(list));
		
	}	catch(DatastoreNeedIndexException e) {response.getWriter().println("Unsupported query, contact admin");} //Unindexed query
		catch(LoadException e) {response.getWriter().println("Database is getting ready, please try again later");} //Index loading
		catch(IllegalArgumentException e) {response.getWriter().println("Datastore error");} //Empty datastore throws IllegalArgumentException
		catch (IOException e) {e.printStackTrace();} //Error while parsing JSON

  }

  /**
   * Gets sorting type argument for the query
   * @param req petition request
   * @return String containing the sorting type of the query
   */
private String getSorting(HttpServletRequest req) {
	String p = req.getParameter("sort");
	String res = "-outbound.departureDate";
	if(p!=null) {
		switch(p) {
		case "asctime":
			res = "outbound.departureDate";
			break;
		case "desctime":
			res = "-outbound.departureDate";
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

	/**
	 * Gets IATA code for arrival airport
	 * @param req petition request
	 * @return String containing IATA code to use on the query
	 */
private String getIATAarrival(HttpServletRequest req) {
	String p = req.getParameter("IATAarrival");
	String res = ""; //default value
	String IATAregex = "[A-Z]{3}";
	if(p!=null)
		if(p.matches(IATAregex))
			res = p;
	return res;
}

/**
 * Gets IATA code for departure airport
 * @param req petition request
 * @return String containing IATA code to use on the query
 */
private String getIATAorigin(HttpServletRequest req) {
	String p = req.getParameter("IATAorigin");
	String res = ""; //default value
	String IATAregex = "[A-Z]{3}";
	if(p!=null)
		if(p.matches(IATAregex))
			res = p;
	return res;
}

/**
 * Gets star rating criteria for the query
 * @param req petition request
 * @return int containing starRating criteria to use on the query
 */
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