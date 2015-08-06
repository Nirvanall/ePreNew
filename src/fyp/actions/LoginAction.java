package fyp.actions;

import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String user;
	private String password;
	
	@Override
	public String execute() throws Exception {
		if (user.equalsIgnoreCase("admin") && password.equals("admin")) {
			return SUCCESS;
		} else {
			return INPUT;
		}
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}