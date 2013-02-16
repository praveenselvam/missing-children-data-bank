package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.ebean.Model;
import play.data.validation.*;

@Entity
public class Child extends AuditedModel{

	@Id
	public Long id;
	
	// basic identification
	/**
	 * It is possible that none of the below could 
	 * be known.
	 */
	public String name;
	public int age;
	public Date dob;
	
	public String cwcId,homeAdmissionId,parent,nativeTown,nativeState,status;
		
	@OneToMany
	public List<Interview> interviews = new ArrayList<Interview>();
	
	@ManyToMany
	public List<Language> speaks = new ArrayList<Language>();
	
	@OneToOne
	@Constraints.Required
	public Home home;
	
	public void fill()
	{
		home = Home.findById(home.id);
	}
	
	//-----------
	/**
	 * This should be pretty easy to find!
	 */
	public String gender;
	
	public static Model.Finder<Long,Child> find = new Model.Finder(Long.class, Child.class);
	
	public Child(String _name,int _age,Date _dob,String _gender,Home _home,String cwcId,String homeAdmissionId,String parent,String nativeTown,String state)
	{
		this.name = _name;
		this.age = _age;
		this.dob = _dob;
		this.gender = _gender;
		this.home = _home;
		
		this.cwcId = cwcId;
		this.homeAdmissionId = homeAdmissionId;
		this.parent = parent;
		this.nativeTown = nativeTown;
		this.nativeState = state;
				
	}
	
	public static Child create(String _name,int _age,Date _dob,String _gender,Home _home,String cwcId,String homeAdmissionId,String parent,String nativeTown,String state)
	{
		Child c = new Child(_name,_age,_dob,_gender,_home,cwcId,homeAdmissionId,parent,nativeTown,state);
		c.save();
		c.fill();		
		return c;
	}
	
	public void setCurrentStatus(String status)
	{
		this.status = status;
	}
	
	
	private boolean isInterviewDirty = false;
	private boolean isPrevousHomesDirty = false;
	private boolean isLanguageDirty = false;
	
	private void addInterview(Interview i)
	{	
		this.isInterviewDirty = this.interviews.add(i) || this.isInterviewDirty;
	}
	private void addLanguage(Language l)
	{
		this.isLanguageDirty = this.speaks.add(l) || this.isLanguageDirty;
	}
	
	private void saveDirty()
	{
		if(this.isInterviewDirty)
		{
			this.saveManyToManyAssociations("interviews");
			this.isInterviewDirty = false;
		}
		if(this.isLanguageDirty)
		{
			this.saveManyToManyAssociations("speaks");
			this.isLanguageDirty = false;
		}
		if(this.isPrevousHomesDirty)
		{
			this.saveManyToManyAssociations("previousHomes");
			this.isPrevousHomesDirty = false;
		}
		
	}
	public <T> void addInformation(List<?> info)
	{
		for(Object add : info)
		{
			if(add instanceof Language)
			{
				addLanguage((Language)add);
			}else if(add instanceof Interview)
			{
				addInterview((Interview)add);
			}
		}
		saveDirty();
	}
	
	
}
