package com.gonzalo;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Test for DELETE requests. Sends out a POST request, DELETEs the result and checks whether it was correctly deleted via GET
 * @author Gonzalo Luque Mart�n
 *
 */
public class DoDeleteTest {

		private final LocalServiceTestHelper helper =
			      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockHttpServletRequest request = new MockHttpServletRequest();
		@Test
		public void test() throws IOException, ServletException {
			
			HolidayPackage hp = new CreateService().createDummyHoliday();
			hp.setId(new Long(12345));

			request.setServerName("ciklumentrevista.appspot.com");
			request.setRequestURI("/holidaypackages");
	       	
			// when
			String url = request.getRequestURL().toString(); // assuming there is always queryString.

			// then

			assertTrue(url.equals("http://ciklumentrevista.appspot.com/holidaypackages"));
			
			ObjectifyService.begin();
			ObjectMapper mapper = new ObjectMapper();
			request.setContent(mapper.writeValueAsBytes(hp));
			new HolidayPackagesServlet().doPost(request, response);
			
			//Check if POST went well
		    Assert.assertEquals("text/plain", response.getContentType());
		    Assert.assertEquals("UTF-8", response.getCharacterEncoding());
		    String s = response.getWriterContent().toString();
		    Assert.assertTrue(s.trim().equals("Done"));
		    
		    //Try to DELETE what was just posted
		    request.setRequestURI("/holidaypackages/12345");
		    response = new MockHttpServletResponse(); //Get an empty response to use in the new petition
		    new HolidayPackagesServlet().doDelete(request, response);
		    
		    Assert.assertEquals("text/plain", response.getContentType());
		    Assert.assertEquals("UTF-8", response.getCharacterEncoding());
		    String respuesta = response.getWriterContent().toString();
		    System.out.println(respuesta);
		    Assert.assertTrue(respuesta.trim().equals("Done"));
		    
		    //Check if GETing the item yields empty results
		    response = new MockHttpServletResponse(); //Get an empty response to use in the new petition
		    new HolidayPackagesServlet().doGet(request, response);
		    
		    Assert.assertEquals("application/json", response.getContentType());
		    Assert.assertEquals("UTF-8", response.getCharacterEncoding());
		    String str = response.getWriterContent().toString();
		    Assert.assertTrue(str.trim().equals("null"));
		}
		
		
		
		private Closeable closeable;
		@Before
	    public void setUp() {
	       helper.setUp();
	        closeable = ObjectifyService.begin();
	        ObjectifyService.register(HolidayPackage.class);
	    }
		
		@After
	    public void tearDown() {
			helper.tearDown();
	        closeable.close();
	    }

}
