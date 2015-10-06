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
    	Query query = session.createQuery("FROM Message AS m WHERE m.toUser IS NULL");
    	query.setMaxResults(6);
    	@SuppressWarnings("unchecked") List<Message> announcements = (List<Message>)query.list();
    	if (!announcements.isEmpty()) {
    		model.addAttribute("announcements", announcements);
    		if (announcements.size() > 5) {
    			model.addAttribute("more_announcements", true);
    		}
    	}
    	
    	if (null != source && source.equalsIgnoreCase("login")) {
    		model.addAttribute("error_message", "Incorrect UserID / Incorrect Password");
    	}
        return "index";
	}
}
