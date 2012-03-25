package models;

import constants.ApplicationConstants;

public class Login {
	public Login() {
		
	}
	public String email;
	public String password;
	
	public String validate() {
		
		return (User.authenticate(email, password) == null)?ApplicationConstants.INVALID_LOGIN
				:ApplicationConstants.LOGIN_SUCCESS;
		
	}
}
