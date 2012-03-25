package models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import play.data.validation.*;
import play.data.format.*;

import play.db.ebean.Model;

@Entity
@Table(name="account")
public class User extends Model {
	
	@Id
	@Constraints.Required
	@Formats.NonEmpty
	public String email;
	
	@Constraints.Required
	public String name;
	
	@Constraints.Required
	public String password;
	
	public static Model.Finder<String, User> find = new Model.Finder<String, User>(String.class, User.class);
	//public static Model.Finder<Long, User> findById = new Model.Finder<Long, User>(Long.class, User.class);
	
	private static final String PASSWORD_SALT = "<!@ra1nb0w*D3t3r%?>";
	
	public static List<User> findAll()
	{
		return find.all();
	}
	
	public static User findByEmail(String email)
	{
		return find.where().eq("email", email).findUnique();
	}
	
	public static User authenticate(String email,String password)
	{
		String hashedPassword = getHashedPassword(password);
		return find.where()
				.eq("email", email)
				.eq("password", hashedPassword)
				.findUnique();
		
	}
	
	public static String getHashedPassword(String plainPassword)
	{
		MessageDigest hasher;
		try {
			hasher = MessageDigest.getInstance("SHA-1");
			hasher.reset();
			hasher.update((plainPassword+PASSWORD_SALT).getBytes());
			return  new String(hasher.digest());
			
		} catch (NoSuchAlgorithmException e) {			
			//e.printStackTrace();
		}
		return "";
	}
	
}
