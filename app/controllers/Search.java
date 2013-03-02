package controllers;

import models.Child;
import models.Home;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(Secured.class)
public class Search extends Controller {

	public static Result index(Long homeId)
	{
		Home currentHome = Home.findById(homeId);
		if(currentHome == null)
		{
			currentHome = new Home();
			currentHome.id = -1L;
		}
		
		return ok(views.html.search.index.render(
					Child.find.where().eq("home_id", currentHome.id).findList(),
					currentHome)
				);
	}
	
}
