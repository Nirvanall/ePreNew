package fyp.controllers;

import javax.servlet.http.HttpSession;

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
public class StudentController {
	private SessionFactory sessionFactory;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@RequestMapping(value = "/student.do", method = RequestMethod.GET)
	public String indexAction(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "number") Integer number,
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user || !user.isStudent()) return "redirect:index.do";
		model.addAttribute("user_id", user.getUserId());
		model.addAttribute("user_name", user.getName());
		
		if (null == page || page <= 0) page = 1;
		if (null == number || number <= 0) number = 10;
		int offset = number * (page - 1);
		
		// TODO: Retrieve Department->Presentation->Video
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM Video AS v WHERE v.user=:user ORDER BY v.presentation");
		query.setParameter("user", user).setFirstResult(offset).setMaxResults(number);
		
		return "student";
	}
}
