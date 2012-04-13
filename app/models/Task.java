package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Task extends AuditedModel {
	
	@Id
	public Long id;
	
	@OneToOne
	public User assignedTo;
}
