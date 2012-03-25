package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.format.*;
import play.data.validation.*;
import play.db.ebean.Model;

@Entity
public class Language extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	public Long id;
	@Constraints.Required
	@Constraints.Pattern("[a-zA-Z]+")
	public String name;
	
	@ManyToMany
	public List<Child> speakers = new ArrayList<Child>();
}
