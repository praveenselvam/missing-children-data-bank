package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class Photo extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8377736276238987321L;
	@Id
	public Long id;
	
	@Constraints.Required
	public Long childId;
	
	@Lob
	@Constraints.Required
	public byte[] image;
	
	public static Model.Finder<Long, Photo> find = new Model.Finder<Long, Photo>(Long.class, Photo.class);
	
	public static Photo findByChildId(Long childId)
	{
		return find.where().eq("child_id", childId).findUnique();
	}
	
	public Photo(Long childId,byte[] image)
	{
		this.image = image;
		this.childId = childId;
	}
}
