package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(Secured.class)
public class Reports extends Controller{

	public static Result index()
	{
		return ok(views.html.reports.index.render());
	}

	public static Result poster()
	{
		return ok(views.html.reports.poster.render());
	}
}
