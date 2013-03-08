import java.util.List;
import java.util.Map;

import models.Home;
import models.User;

import com.avaje.ebean.Ebean;

import play.Application;
import play.GlobalSettings;
import play.libs.Yaml;


public class Global extends GlobalSettings {
	
	@Override
	public void onStart(Application app) {
		InitialData.insert(app);
	}
	
	static class InitialData
	{
		public static void insert(Application app)
		{
			Map<String,List<Object>> all = (Map<String,List<Object>>) Yaml.load("initial-data.yml");
			
			if(Ebean.find(User.class).findRowCount() == 0)
			{	
				List<Object> users = all.get("users");
				for(Object user : users)
				{
					User u = (User)user;
					u.password = User.getHashedPassword(u.password);					
					Ebean.save(u);
				}
			}
			
			if(Ebean.find(Home.class).findRowCount() == 0)
			{
				List<Object> homes = all.get("homes");
				if(homes!=null && homes.size()>0)
				{
					Ebean.save(homes);
				}
			}
			
			if(Home.findByName("UN-KNOWN") == null )
			{
				List<Object> homes = all.get("unknown-home");
				if(homes!=null && homes.size()>0)
				{
					Ebean.save(homes);
				}
			}
		}
	}

}
