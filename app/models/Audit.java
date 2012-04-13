package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import constants.AuditType;

import play.db.ebean.Model;

@Entity
public class Audit extends Model{

	@Id
	public Long id;
	
	public String typeOf;
	public String entity;
	public String entityIdentifiedBy;
	
	public String by;
	
	public Date when;
	
	private Audit(){}
	
	public static Model.Finder<Long,Audit> find = new Model.Finder(Long.class, Audit.class);
	
	public Audit(AuditType t,Class auditFor,String byUser,String idBy)
	{
		this.typeOf = t.name();
		this.entity = auditFor.getName().replaceAll("models\\.", "");
		this.when = new Date();
		this.by = byUser;
		this.entityIdentifiedBy = idBy;
	}
	
	
	
}
