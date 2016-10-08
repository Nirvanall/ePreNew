package edu.hkpolyu.epre.controller;

import java.util.List;

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

import edu.hkpolyu.epre.model.User;
import edu.hkpolyu.epre.model.Video;

/**
 * Student Page
 * The list of the student's videos
 */
@Controller
public class StudentController {
	private SessionFactory sessionFactory;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@RequestMapping(value = "/student.do", method = RequestMethod.GET)
	public String indexAction(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "number", required = false) Integer number,
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user || !user.isStudent()) return "redirect:index.do";
		model.addAttribute("user_id", user.getUserName());
		model.addAttribute("user_name", user.getName());
		
		if (null == page || page <= 0) page = 1;
		if (null == number || number <= 0) number = 10;
		int offset = number * (page - 1);
		model.addAttribute("page", page);
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(
				"SELECT COUNT(*) FROM Video WHERE owner.id=:userId ");
		query.setInteger("userId", user.getId());
		Long totalCount = (Long)query.uniqueResult();
		Long totalPages = totalCount / number + (totalCount % number != 0 ? 1 : 0);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("totalPages", totalPages);
		
		query = session.createQuery(
				"FROM Video AS v WHERE v.owner.id=:userId " +
				"ORDER BY v.presentation.yearSemester DESC, " +
					"v.presentation.department.abbreviation, " +
					"v.presentation.createTime DESC, " +
					"v.createTime DESC");
		query.setInteger("userId", user.getId()).setFirstResult(offset).setMaxResults(number);
		@SuppressWarnings("unchecked") List<Video> videos = (List<Video>)query.list();
		model.addAttribute("videos", videos);
		
		return "student";
	}
}
