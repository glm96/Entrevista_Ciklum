package com.gonzalo;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.appengine.api.datastore.DatastoreNeedIndexException;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.googlecode.objectify.LoadException;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

/**
 * This class holds every response to petitions for HolidayPackages, implementing basic CRUD methods
 * @author Gonzalo Luque
 *
 */
@SuppressWarnings("serial")
@WebServlet(
	    name = "hpServlet",
	    urlPatterns = {"/holidaypackages/*"}
	    )
public class HolidayPackagesServlet extends HttpServlet {

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
	String sort1 = getSorting(request,1);
	String sort2 = getSorting(request,2);
	String IATAarr = getIATAarrival(request);
	String IATAdep = getIATAorigin(request);
	int rating = getStarRating(request);
	
	Long id = new Long(0);
	ArrayList<Filter> filterlist = new ArrayList<Filter>();
	Filter finalfilter = null;
	boolean firstflag = true;
	
	//grab id from petition
	String str = request.getRequestURI();
	str = str.substring(str.lastIndexOf("/")+1);
	try {
		id = Long.parseLong(str);
	}catch(Exception e) {}
	
	if(id!= 0) {
		finalfilter = new FilterPredicate("id",FilterOperator.EQUAL,id);
	}
	
	else {
		if(!IATAdep.equals("")) { //IATAdep argument was found
		Filter f1 = new FilterPredicate("outbound.departureCode",FilterOperator.EQUAL,IATAdep);
		filterlist.add(f1);
		}
		if(!IATAarr.equals("")) { //IATAarr argument was found
			Filter f1 = new FilterPredicate("outbound.arrivalCode",FilterOperator.EQUAL,IATAarr);
			filterlist.add(f1);
		}
		if(rating != -1) {	//rating argument was found
			Filter f1 = new FilterPredicate("lodging.starRating", FilterOperator.GREATER_THAN_OR_EQUAL,rating);
			filterlist.add(f1);
			if(sort1.substring(0,sort1.lastIndexOf(".")).equals("lodging")) { //if filter is an inequality type, first sorting should be by that type
				String aux;
				aux = sort1;
				sort1 = sort2;
				sort2 = aux;
			}
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
	}
	
	try {
		
		ObjectMapper mapper = new ObjectMapper();
		String JSONstring;
		
		if(id!=0) { //There was an ID specified
			HolidayPackage hp = ofy().load().type(HolidayPackage.class).id(id).now();
			JSONstring = mapper.writeValueAsString(hp);
		}
		else {
			List<HolidayPackage> list;
			Query<HolidayPackage> ofyQuery;
			if(finalfilter!=null) {
				ofyQuery = ofy().load().type(HolidayPackage.class).filter(finalfilter).order(sort1).order(sort2).limit(1000);
			}
			else {
				ofyQuery = ofy().load().type(HolidayPackage.class).order(sort1).order(sort2).limit(1000);
			}
			list = ofyQuery.list();
			JSONstring = mapper.writeValueAsString(list);
		}
		
		response.getWriter().println(JSONstring);
		
		
	}	catch(DatastoreNeedIndexException e) {response.getWriter().println("{\"error\": \"Unsupported "
			+ "query, please contact an administrator\"}");} //Unindexed query
		catch(LoadException e) {response.getWriter().println("{\"error\": \"Database "
				+ "is not ready yet, please try again in a few minutes\"}");} //Index loading
		catch(IllegalArgumentException e) {response.getWriter().println("{\"error\": \"Database error\"}");} //Empty datastore throws IllegalArgumentException
		catch (IOException e) {e.printStackTrace();} //Error while parsing JSON
		
		
  }
  
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
		String str = req.getRequestURI();
		str = str.substring(str.lastIndexOf("/")+1);
		try {
		long id = Long.parseLong(str);
		HolidayPackage hp = ofy().load().type(HolidayPackage.class).id(id).now();
		if(hp.checkCorrect())
			ofy().delete().type(HolidayPackage.class).id(id).now();
		}catch(Exception e) {success = false;}//Entity not found in database
		if(success) {
			msg = "Done";
		}
		resp.getWriter().println(msg);
		
	}
  
  	/**
	 * Method for UPDATE petition management. Checks whether id stored exists within the database and, 
	 * if so, updates its entry
	 */
  @Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
	  	ObjectifyService.register(HolidayPackage.class); //Register HolidayPackages in order to be able to use them with Objectify library
		HolidayPackage holiday;
		Long id;
		//sets response type to plain text
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		
		//grab id from petition
		String str = req.getRequestURI();
		str = str.substring(str.lastIndexOf("/")+1);
		if(str!=null) {
			if(str.matches("^[0-9]*$"))
				id = Long.parseLong(str);
			else
				id = new Long(0);
		}
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
	   * Gets sorting type argument for the query
	   * @param req petition request
	   * @param i sorting argument wanted
	   * @return String containing the sorting type of the query
	   */
	private String getSorting(HttpServletRequest req, int i) {
		String p = req.getParameter("sort"+Integer.toString(i));
		String res = (i==1) ? "-lodging.starRating":"outbound.departureDate";
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
