package fyp.controllers;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fyp.models.User;

@Controller
public class LoginController {
	private SessionFactory sessionFactory;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String loginAction(
			@RequestParam(value="user", required=true) String userId,
    		@RequestParam(value="password", required=true) String password,
    		Model model) {
		Session session = sessionFactory.openSession();
    	Query query = session.createQuery("FROM User AS u WHERE u.userId=:userId AND u.password=sha2(:password, 256)");
    	query.setString("userId", userId).setString("password", password);
    	User user = (User)query.uniqueResult();
    	if (null == user) return new IndexController().indexAction("login", model);
        if (user.isAdmin()) return "admin";
        // if (user.isTeacher()) return new TeacherController().indexAction("login", model);
        // if (user.isStudent()) return new StudentController().indexAction("login", model);
        // return new GuestController().indexAction("login", model);
        return "student";
	}
}
