package fyp.controllers;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fyp.models.Message;

@Controller
public class IndexController {
	private SessionFactory sessionFactory;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@RequestMapping(value = "/index.do", method = RequestMethod.GET)
	public String indexAction(
			@RequestParam(value = "source", required = false) String source,
			Model model) {
		Session session = sessionFactory.openSession();
    	Query query = session.createQuery("FROM Message WHERE toUser IS NULL ORDER BY createTime DESC, id DESC");
    	query.setMaxResults(6);
    	@SuppressWarnings("unchecked") List<Message> announcements = (List<Message>)query.list();
    	model.addAttribute("announcements", announcements);
    	
    	if (null != source && source.equalsIgnoreCase("login")) {
    		model.addAttribute("error_message", "Incorrect UserID / Incorrect Password");
    	}
        return "index";
	}
}
