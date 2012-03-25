package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.data.validation.*;

import play.db.ebean.Model;

@Entity
public class Interview extends Model{

	@Id
	public Long id;
	
	@Constraints.Required
	public Date conductedOn;
	
	public String interviewTranscript;
	
	@ManyToOne
	public Child child;
	
	
	public static Interview create(Date on,String trans,Child kid)
	{
		Interview i = new Interview();
		i.conductedOn = on; i.interviewTranscript = trans;
		i.child = kid;
		i.save();
		return i;
	}
}
