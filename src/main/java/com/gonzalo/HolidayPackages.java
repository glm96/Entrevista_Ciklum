package com.gonzalo;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

@WebServlet(
	    name = "hpServlet",
	    urlPatterns = {"/holidaypackages/*"}
	)
public class HolidayPackages extends HttpServlet {

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
	 * if so, update its entry
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
		
		//get id parameter
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
