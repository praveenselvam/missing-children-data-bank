package controllers;

import java.util.ArrayList;
import java.util.List;

import com.avaje.ebean.ExpressionList;

import models.Child;
import models.Home;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(Secured.class)
public class Search extends Controller {

	public static Result index(Long homeId)
	{
		String name = null;
		int age = -1;
		
		Home currentHome = Home.findById(homeId);
		if(currentHome == null)
		{
			currentHome = new Home();
			currentHome.id = -1L;
		}
		
		List<Child> children = findChildren(currentHome, name, age);
		
		return ok(views.html.search.index.render(
					children,
					currentHome,name,age)
				);
	}
	
	public static Result byName()
	{
		String name = form().bindFromRequest().get("name");
		String __homeId = form().bindFromRequest().get("home");
		String __age  = form().bindFromRequest().get("age");
		
		Long homeId = -1L;
		int age = -1;
		
		try{homeId = Long.parseLong(__homeId);}catch(Exception e){Logger.error(e.getMessage());}
		try{age = Integer.parseInt(__age);}catch(Exception e){Logger.error(e.getMessage());}
		
		Home currentHome = Home.findById(homeId);
		if(currentHome == null)
		{
			currentHome = new Home();
			currentHome.id = -1L;
		}
				
		List<Child> children = findChildren(currentHome, name, age);
								
		Logger.info(children.size() + " search results for "+ name);
								
		return ok(views.html.search.index.render(
				children,
				currentHome,name,age)
			);
	}	
	
	private static List<Child> findChildren(Home _home,String likeName,int age){
		
		ExpressionList<Child> el = Child.find.where();
		boolean atLeastOneCondition = false;
			if(_home.id>0)
			{
				el = el.eq("home_id", _home.id);	
				atLeastOneCondition |= true;
			}
			if(likeName !=null && !likeName.trim().isEmpty())
			{
				el = el.ilike("name", likeName.trim());
				atLeastOneCondition |= true;
			}
			if(age > 0)
			{
				el = el.eq("age", age);
				atLeastOneCondition |= true;
			}
		
		if(atLeastOneCondition)
		{
			return el.findList();
		}
		return Child.find.all();
	}
	
}
