package controllers;

import models.Child;
import models.Interview;
import play.mvc.*;

@Security.Authenticated(Secured.class)
public class Admin extends Controller{

	public static Result index()
	{
		return ok(views.html.admin.child.render(form(Child.class)));
	}
	
	public static Result newChild()
	{
		return ok(views.html.admin.interview.render(form(Interview.class),new Child()));
	}
	
	public static Result newInterview()
	{
		return TODO;
	}
}
