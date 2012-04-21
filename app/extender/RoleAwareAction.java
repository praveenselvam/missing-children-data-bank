package extender;

import play.Logger;
import play.mvc.Action;
import play.mvc.Result;
import play.mvc.Http.Context;

public class RoleAwareAction extends Action.Simple{

	@Override
	public Result call(Context ctx) throws Throwable {
		Logger.info("Calling action for :" + ctx.request().path());
		
		return delegate.call(ctx);
	}
}
