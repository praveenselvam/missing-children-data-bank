package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.data.validation.*;
import play.db.ebean.Model;

@Entity
public class Transfer extends AuditedModel {

	@Id
	public Long id;
	
	@OneToOne
	public Child child;
	
	@OneToOne
	public Home transferTo;
	
	@OneToOne
	public Home transferFrom;

	@Constraints.Required
	public String reason="",approvedBy="";
	
	public Date transferDate;
	
	public boolean complete = false;
	
	public static Model.Finder<Long,Transfer> find = new Model.Finder(Long.class, Transfer.class);
	private Transfer(){}
	
	public Transfer(Child c, Home from, Home to, String reason, String approvedBy)
	{
		this.child = c;
		this.transferTo = to;
		this.transferFrom = from;
		this.reason = reason;
		this.approvedBy = approvedBy;
		this.transferDate = new Date();
	}
	
	public static List<Transfer> byChild(Long childId)
	{
		List<Transfer> allTransfers = find.where().eq("child_id", childId).orderBy().asc("transferDate").findList();
		for(Transfer t : allTransfers)
		{
			t.fill();
		}
		return allTransfers;
	}
	
	public static List<Transfer> all()
	{
		List<Transfer> allTransfers = find.all();
		for(Transfer t : allTransfers)
		{
			t.fill();
		}
		return allTransfers;
	}
	
	public void fill()
	{
		child = Child.find.byId(child.id);
		child.fill();
		transferTo = Home.findById(transferTo.id);
		transferFrom = Home.findById(transferFrom.id);
		
	}
}
