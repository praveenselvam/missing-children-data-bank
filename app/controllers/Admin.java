package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.Child;
import models.Home;
import models.Interview;
import play.data.Form;
import play.mvc.*;

@Security.Authenticated(Secured.class)
public class Admin extends Controller{

	public static Result index()
	{
		return ok(views.html.admin.child.render(form(Child.class),Home.all()));
	}
	
	public static Result newChild()
	{
		Form<Child> addChildForm = form(Child.class).bindFromRequest();
		
		
		String name = form().bindFromRequest().get("name");
		String age = form().bindFromRequest().get("age");
		String dob = form().bindFromRequest().get("dob");
		String gender = form().bindFromRequest().get("gender");
		String home = form().bindFromRequest().get("home");
		
		
		System.out.println(name + ": "+age+":"+dob+":"+gender+":"+home );
		
		Child c = Child.create(name, Integer.valueOf(age), new Date(), gender, Home.findById(Long.valueOf(home)));
		
		//return ok(views.html.admin.child.render(addChildForm,Home.all()));
		return ok(views.html.admin.interview.render(form(Interview.class),c));
	}
	
	public static Result addInterview(Long childId)
	{
		Child c = Child.find.byId(childId);
		return ok(views.html.admin.interview.render(form(Interview.class),c));
	}
	
	public static Result newInterview(Long childId)
	{
		Child c = Child.find.byId(childId);
		/// all validation here.
		String transcript = form().bindFromRequest().get("transcript");	
		Interview i = Interview.create(new Date(), transcript, c);
		/*List<Object> additionalInfo = new ArrayList<Object>();
		additionalInfo.add(i);
		c.addInformation(additionalInfo);*/
		
		return ok(views.html.admin.summary.render(c));
	}
	
	public static Result allChildren()
	{
		return ok(views.html.admin.all.render());
	}
	
	public static Result childSummary(Long id){
		return ok(views.html.admin.summary.render(Child.find.byId(id)));
	}
}
