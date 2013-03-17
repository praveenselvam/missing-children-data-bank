package utils;

import java.text.ParseException;

public class ImportUtils {

	public static final String[] IMPORT_NAME_KEYS = {"name","age","dob","gender","cwcId","homeAdmissionId","parent","town","state"};
	public static String validateChildInput(String[] params)
	{
		String error = null;
		
			if(params[0] ==null && params[0].isEmpty()){
				return "name is required";
			}
			
			if(params[1] == null && params[1].isEmpty()) {
				//return "age is required";
				// lets default to 0 and consider age as unknown
				// the volunteers can update the age the next time they
				// see the child / need to add an interview.				
			}else  if(params[1] != null && !params[1].isEmpty()){
				try{					
				int age = Integer.parseInt(params[1]);
				}catch(NumberFormatException pe){
					return "age should be a number";
				}
			}
			int GENDER=3;
			if(params[GENDER] == null)
			{
				return "gender required M|F";
			}
			if(! (params[GENDER].trim().equalsIgnoreCase("m") || params[GENDER].trim().equalsIgnoreCase("f")))
			{
				return "gender required M|F";
			}
		return error;
	}
	
	public static class ChildUtil
	{
		private String[] params = null;
		private ChildUtil(){}
		
		public ChildUtil(String[] params)
		{
			this.params = params;
		}
		
		public String getName(){return params[0];}
		public int getAge(){ return (params[1]==null || params[1].trim().isEmpty())?0:Integer.parseInt(params[1]);}		
		public String getDob(){ return params[2]; }
		public String getGender(){ return params[3]; }
		public String getCwcId(){ return params[4]; }
		public String getHomeAdmissionId(){ return params[5]; }
		public String getParent(){ return params[6]; }
		public String getTown(){ return params[7]; }
		public String getState(){ return params[8]; }
		
	}
	
}
