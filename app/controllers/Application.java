package controllers;

import models.Login;

import play.data.Form;
import play.mvc.*;
import static constants.ApplicationConstants.SESSION_KEY;

public class Application extends Controller {
 	
	public static Result authenticate()
	{
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		if(!loginForm.hasErrors()) {
			session().put(SESSION_KEY, loginForm.get().email);
			return redirect(routes.Admin.index());
		}
		return ok(views.html.login.render(loginForm));
		
	}
	
	public static Result logout()
	{
		session().clear();
		return redirect(routes.Application.login());
	}
  
	public static Result login()
	{
	 return ok(
		  		views.html.login.render(form(Login.class))
		  	);
	}
  
}