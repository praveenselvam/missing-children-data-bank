package controllers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

import javax.imageio.ImageIO;

import extender.RoleAwareAction;

import models.Child;
import models.Home;
import models.Interview;
import models.Photo;
import models.Transfer;
import play.Logger;
import play.data.Form;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import utils.Base64Coder;
import utils.ImageUtils;

@Security.Authenticated(Secured.class)
public class Admin extends Controller{

	@With(RoleAwareAction.class)
	public static Result index()
	{
		return ok(views.html.admin.home.render());
	}
	
	public static Result newChild()
	{
		return ok(views.html.admin.child.render(form(Child.class),Home.all(),false));
	}
	
	private static final String DEFAULT_PHOTO = "iVBORw0KGgoAAAANSUhEUgAAANoAAADaCAYAAADAHVzbAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAACxIAAAsSAdLdfvwAAC1iSURBVHhe7Z0Htyw3sYXv//9PmGSiySYYY5JNMmDAYKKBeXzjty/bRUmtzuoZnbVmzZmZbnW3VLvCrpL04jb+TumBf//73zde+vP/4w3Vftvy5nVP8X3LazxrWy+e9cHPfO4IMAdd9n8EpYNzCoSlc2mj9lvL72f24dWuPYB2wogNoJ3Q6SdfcgDt5AHIXMiSCzdl2bLzao83ZQ393Cnrd3I3dn/5AbQThigT2lJcNAUGPy+6lNm58fipYwbAthGQAbRt+nFWKxk4aiCZG2e1tj/HQs56wHHw//TAANpJQlFj9qaslH7/17/+9THmMnP1OEbHTVmnwTbuJwwDaPv1bdoywhwFv2aBWm+PNv/5z3++ZBKnXM4MlANorb09/7gBtPl9tuoMB1oUdoDyj3/84/aXv/zl9sEHH9zf//a3v91ffP/3v//9/t2f/vSn++v999+//e53v7v96le/uv3sZz+7vfPOO/f3n//857df/vKXt3fffff23nvv3f7whz/cj/3jH/94b5fXX//613ubvLiugz8+4BzQruqcBz55AO2AwS0RDgg44Pnzn/98B8Ovf/3rO1jeeuut2/e///3bD37wg9uPf/zj209/+tPb22+/ffvJT35y/+2NN964ffe73719+9vfvn3jG9+4ffnLX7594QtfuH3xi198+frSl750e+21125f+9rXbt/85jfvx37nO9+5n0u7P/rRj+7tAsxf/OIXL0EJILkf7sst5AHd9NCXGEArDG+Jdq+5XFPuWAYshB1QARiAAVg+//nP3z73uc/d3wUg3vms31599dUbr89+9rP312c+85mX/+s7/a5jefd21bZA+dWvfvX2rW99634/APs3v/nNXQFgPQGfrGB0Ub2vpgiWrI8eGmH//3ADaBOjLDKhJCDRWjlR8eGHH954yWrhxmGxEGSsDMASgBw0AkoETAYmP9YBFf/PjvP2dTxA5AXgv/KVr9wVwOuvv363hFhTLCDuKi4sz0b/6M/JGe8X/Z+5zc/ilg6gNVo0HVZjBNH0CCBWAGug2An373vf+97t61//+g3rgVUSsABPtE6lzyUgRWDy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";
	private static byte[]  PHOTO = null;
	public static Result photo(Long childId)
	{	
		// stream from db as blob		
		Photo thumbnail = Photo.findByChildId(childId);
		//Logger.info(thumbnail.childId + " Child ID" + thumbnail.id + " photo id , image" + (thumbnail.image == null));
		if(thumbnail == null || thumbnail.image == null)
		{
			//Logger.info("Thumbnain is null");
			if(PHOTO == null || PHOTO.length == 0)
			{
				try{
					PHOTO = Base64Coder.decode(DEFAULT_PHOTO);
				}catch(Exception bde){
					Logger.error("Error decoding default image");
					PHOTO = new byte[]{};
				}
			}
			thumbnail = new Photo(-1L, PHOTO);
		}
		return ok(thumbnail.image);
	}
	
	public static Result addChild()
	{
		Form<Child> addChildForm = form(Child.class).bindFromRequest();
		
		String home = form().bindFromRequest().get("home");
		String name = form().bindFromRequest().get("name");
		String age = form().bindFromRequest().get("age");
		String dob = form().bindFromRequest().get("dob");
		String gender = form().bindFromRequest().get("gender");
		
		int iAge = -1;
		try{iAge = Integer.parseInt(age);}catch(Exception e){};
		
		Home _home = Home.findById(Long.valueOf(home));
		
		boolean noErrors = true;
		noErrors = (_home != null && _home.id > 0);
		
		noErrors &= (name!=null && !name.trim().isEmpty());
		
		//Logger.info("name hasErrors ["+noErrors+"]"+name);
		
		noErrors &= (iAge>0);
		//Logger.info("age hasErrors ["+noErrors+"] + ["+iAge+"]");
				
		if(!noErrors)
		{						
			return ok(views.html.admin.child.render(addChildForm,Home.all(),noErrors));
		}	
		
		//Logger.debug(home + " home id from ui");
		
		
		String cwcId = form().bindFromRequest().get("cwcId");
		String homeAdmissionId = form().bindFromRequest().get("homeAdmissionId");
		String parent = form().bindFromRequest().get("parent");
		String nativeTown = form().bindFromRequest().get("nativeTown");
		String state = form().bindFromRequest().get("nativeState");
		
		Child c = Child.create(name,
							   iAge, 
							   new Date(), 
							   gender,
							   _home,
							   cwcId,
							   homeAdmissionId,
							   parent,
							   nativeTown,
							   state
							  );
		
		MultipartFormData formdata = request().body().asMultipartFormData();
		FilePart photo = formdata.getFile("photo");
		
		if(photo!=null)
		{	
			File _f = photo.getFile();
			try
			{
				BufferedImage  _i = ImageIO.read(_f);
				
				Dimension actualSize = new Dimension(_i.getWidth(),_i.getHeight());
				Dimension preferedSize = new Dimension(200,200);
				
				Dimension size = ImageUtils.resizePreservingAspectRatio(actualSize, preferedSize);				
				
				BufferedImage imageBuff = new BufferedImage((int)size.width, (int)size.height, BufferedImage.TYPE_INT_RGB);
				
		        Graphics g = imageBuff.createGraphics();
		        g.drawImage(_i.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH), 0, 0, new Color(0,0,0), null);
		        g.dispose();
		        
		        ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ImageIO.write(imageBuff, "png", bos);
				
				byte[] tn = bos.toByteArray();
				
				Logger.info(tn.length +  " size of image for " + c.id);
				
				
				Photo childThumbnail = new Photo(c.id, tn);
				childThumbnail.save();
							
			}catch(Exception e){
				Logger.error(e.getMessage() + "");
			}
		}
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
		Transfer t = new Transfer(c, c.home,
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
		return redirect(routes.Admin.childSummary(c.id));
	}
	
	public static Result allChildren()
	{
		return ok(views.html.admin.all.render());
	}
	
	public static Result childSummary(Long id){
		Child c = Child.find.byId(id);
		c.fill();
		return ok(views.html.admin.summary.render(c));
	}
}
