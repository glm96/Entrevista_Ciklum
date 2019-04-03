package com.gonzalo;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;

/**
 * Test for GET requests. Sends out a GET request with two different parameters and checks if the response is correct
 * @author Gonzalo Luque Martín
 *
 */
public class DoGetTest {

	private final LocalServiceTestHelper helper =
		      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	@Test
	public void test() throws IOException {
		
		
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setServerName("ciklumentrevista.appspot.com");
		request.setRequestURI("/holidaypackages");
		request.setQueryString("sort1=descstarrating&sort2=asctime");

		// when
		String url = request.getRequestURL() + "?" + request.getQueryString(); // assuming there is always queryString.

		// then
		assertTrue(url.equals("http://ciklumentrevista.appspot.com/holidaypackages?sort1=descstarrating&sort2=asctime"));
		
		ObjectifyService.begin();
		
		new HolidayPackagesServlet().doGet(request, response);
		
	    Assert.assertEquals("application/json", response.getContentType());
	    Assert.assertEquals("UTF-8", response.getCharacterEncoding());
	    String s = response.getWriterContent().toString();
	    Assert.assertTrue(s.trim().equals("[]"));
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
