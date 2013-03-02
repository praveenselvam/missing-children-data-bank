package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(Secured.class)
public class Search extends Controller {

	public static Result index(Long homeId)
	{
		return ok(views.html.search.index.render());
	}
	
}
