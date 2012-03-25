package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.*;
import play.data.format.*;

import play.db.ebean.Model;

@Entity
public class Home extends Model{

	@Id	
	public Long id;
	
	@Constraints.Required
	public String name;
	
	@Constraints.Required
	public String contactPerson;
	
	@Constraints.Required
	public String address;
	
	@Constraints.Required
	public String phoneNumber;
	
	
	public String alternateContactNumber;
	
	@OneToMany
	public List<Child> residents = new ArrayList<Child>();
}
