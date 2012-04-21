package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.Child;
import models.Home;
import models.Interview;
import models.Transfer;
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
	
	public static Result beginTransfer(Long childId)
	{
		Child c = Child.find.byId(childId);
		c.fill();
		return ok(
					views.html.admin.transfer.render(form(Transfer.class),Home.all(),c)
				);
	}
	
	public static Result showTransfer(Long transferId)
	{
		Transfer t = Transfer.find.byId(transferId);
		t.fill();
		
		return ok(
					views.html.transfer.summary.render(t)
				);
	}
	
	public static Result allTransfers()
	{	
		return ok(
					views.html.transfer.all.render(Transfer.all())
				);
	}

	public static Result doTransfer(Long childId)
	{
		// toHome, approvedBy, reason
		
		String toHome = form().bindFromRequest().get("toHome");
		String approvedBy = form().bindFromRequest().get("approvedBy");
		String reason = form().bindFromRequest().get("reason");
		
		Child c = Child.find.byId(childId);
		c.fill(); // stupid play. I need to fix this.
		
		// this needs to be validated
		// if we get botched up values
		// this will fail flat on.
		Transfer t = new Transfer(c, c.residesAt,
				Home.findById(Long.valueOf(toHome)),
				reason, approvedBy);
		t.save();
		t.fill();
		
		return ok(
					views.html.transfer.summary.render(t)
				);
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
		c.fill();
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
