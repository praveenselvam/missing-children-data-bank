package controllers;

import models.Child;
import play.mvc.*;

@Security.Authenticated(Secured.class)
public class Admin extends Controller{

	public static Result index()
	{
		return ok(views.html.admin.child.render(form(Child.class)));
	}
	
	public static Result newChild()
	{
		return TODO;
	}
}
