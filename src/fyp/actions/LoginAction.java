package fyp.actions;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import fyp.models.HibernateSessionFactory;
import org.hibernate.Query;
import fyp.models.User;

public class LoginAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String user;  // user ID
	private String password;  // AES-128-ECB PKCS5Padding of password 
	
	@Override
	public String execute() throws Exception {
		
		org.hibernate.Session hibernateSession = null;
		String result = INPUT;
		try{
			hibernateSession = HibernateSessionFactory.getSession();
			Query query = hibernateSession.createQuery("FROM User AS t WHERE t.userId=:userId AND t.password=:password");
			query.setString("userId", user).setString("password", User.sha256(password));
			@SuppressWarnings("rawtypes")
			List users = query.list();
			if(1 == users.size()){
				HttpServletRequest httpRequest = ServletActionContext.getRequest();
				HttpSession httpSession = httpRequest.getSession();
				httpSession.setAttribute("user", users.get(0));
				result = SUCCESS;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(null != hibernateSession){
				hibernateSession.close();
			}
		}
		return result;
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