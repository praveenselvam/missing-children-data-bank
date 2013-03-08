package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import models.Child;
import models.Home;
import models.Interview;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

// intentionally un-secured to facilitate import
public class Import extends Controller {
	
	private static final String UNKNOWN_HOME_NAME = "UN-KNOWN";
	
	private static String[] importFieldNames = {
		"name","age","gender",
		"cwcId","homeAdmissionId","parent",
		"nativeTown","nativeState"
	};
	
	public static Result importChild()
	{
		String home = form().bindFromRequest().get("home");
		String name = form().bindFromRequest().get("name");
		String age = form().bindFromRequest().get("age");
		String dob = form().bindFromRequest().get("dob");
		String gender = form().bindFromRequest().get("gender");
		
		String cwcId = form().bindFromRequest().get("cwcId");
		String homeAdmissionId = form().bindFromRequest().get("homeAdmissionId");
		String parent = form().bindFromRequest().get("parent");
		String nativeTown = form().bindFromRequest().get("nativeTown");
		String state = form().bindFromRequest().get("nativeState");
		
		String interviewText = form().bindFromRequest().get("interview");
		String interviewDate = form().bindFromRequest().get("interviewDate");
		
		int iAge = -1;
		try{iAge = Integer.parseInt(age);}catch(Exception e){};
		
		int iHomeId = -1;
		try{iHomeId = Integer.parseInt(home);}catch(Exception e){
			return badRequest("home was not found for givenId");
		};
		
		Home _home = null;
		
		if(home!=null && !home.trim().isEmpty())
		{
			if("-1".equals(home))
			{
				_home = Home.findByName(UNKNOWN_HOME_NAME);
				Logger.debug(_home.id + " home id");
			}else{
				
				_home = Home.findById((long)iHomeId);
			}
		}
		if(home == null || iAge == -1)
		{
			return badRequest("home was not found or age was incorrect");
		}
		
		for(String key : importFieldNames)
		{
			String value = form().bindFromRequest().get(key);
			if(value == null || value.trim().isEmpty())
			{
				return badRequest(key + " is expected. null or empty found");
			}
			if(importFieldNames[2].equals(key))
			{
				value = value.trim();
				if(!(value.equalsIgnoreCase("M") || value.equalsIgnoreCase("F")))
				{
					return badRequest(key + " expected M|F");			
				}
			}
		}
		
		if((interviewText!=null && !interviewText.trim().isEmpty()) && (interviewDate!=null && !interviewDate.trim().isEmpty()))
		{
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			Date d = null;
			try{
				d = sdf.parse(interviewDate);
			}catch(ParseException pe){
				return badRequest("interviewDate format is MM/dd/yyyy");
			}
		}
		
		Child c = Child.create(name,
				   iAge, 
				   new Date(), 
				   gender,
				   _home,
				   cwcId,
				   homeAdmissionId,
				   parent,
				   nativeTown,
				   state
				  );
		c.fill();
		if((interviewText!=null && !interviewText.trim().isEmpty()) && (interviewDate!=null && !interviewDate.trim().isEmpty()))
		{
			Interview i = Interview.create(new Date(), interviewText, c);
		}
		
		return ok();
	}
}
