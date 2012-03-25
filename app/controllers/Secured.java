package controllers;
import constants.ApplicationConstants;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.Http.Context;


public class Secured extends Security.Authenticator{

	
	@Override
    public String getUsername(Context ctx) {
        return ctx.session().get(ApplicationConstants.SESSION_KEY);
    }
	
	@Override
	public Result onUnauthorized(Context arg0) {
		return redirect(routes.Application.login());
	}
}
