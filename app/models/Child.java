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

@Entity
public class Child extends Model{

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
	
	public String photoPath;
	
	@OneToMany
	public List<Interview> interviews = new ArrayList<Interview>();
	
	@ManyToMany
	public List<Language> speaks = new ArrayList<Language>();
	
	@OneToOne
	public Home residesAt;
	
	@ManyToMany
	public List<Home> previousHomes = new ArrayList<Home>();
	//-----------
	/**
	 * This should be pretty easy to find!
	 */
	public String gender;
}
