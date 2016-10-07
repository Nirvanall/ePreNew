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
import org.springframework.web.bind.annotation.ResponseBody;

import edu.hkpolyu.common.response.JsonResponse;
import edu.hkpolyu.epre.model.Message;
import edu.hkpolyu.epre.model.User;

/**
 * System announcement (toUser IS NULL (to_user_id IS NULL))
 * And Message
 */
@Controller
public class AnnouncementController {
	private SessionFactory sessionFactory;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * System announcement creation/edition page
	 * @param  Integer id	   Edit: The id of the announcement to be edited
	 *						  Create: NULL
	 * @param  HttpSession httpSession
	 * @param  Model model
	 * @return String
	 */
	@RequestMapping(value = "/admin/announcement/edit.do", method = RequestMethod.GET)
	public String editPageAction(
			@RequestParam(value = "id", required = false) Integer id,
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user || !user.isAdmin()) return "redirect:index.do";
		model.addAttribute("user_id", user.getUserId());
		model.addAttribute("user_name", user.getName());
		
		if (null != id) {
			Session session = sessionFactory.openSession();
			Query query = session.createQuery("FROM Message AS m WHERE m.id = :id AND m.toUser IS NULL");
			query.setInteger("id", id);
			Message announcement = (Message)query.uniqueResult();
			if (null != announcement) {
				model.addAttribute("announcement", announcement);
			}
		}
		return "admin/announcement/edit";
	}
	
	/**
	 * Create/edit a system announcement
	 * @param  Integer id	   Edit: The id of the announcement to be edited
	 *						  Create: NULL
	 * @param  String title	 The title of the announcement
	 * @param  String content   The content of the announcement
	 * @param  HttpSession httpSession
	 * @param  Model model
	 * @return JsonResponse
	 */
	@RequestMapping(value = "/admin/announcement/edit.do", method = RequestMethod.POST)
	public @ResponseBody JsonResponse editAction(
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "title", required = true) String title,
			@RequestParam(value = "content", required = true) String content,
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user || !user.isAdmin()) {
			return JsonResponse.getNeedLoginInstance(null);
		}
		
		Session session = sessionFactory.openSession();
		Message announcement = null;
		if (null == id || 0 == id) {
			announcement = new Message();
			announcement.setFromUser(user);
			announcement.setTitle(title);
			announcement.setContent(content);
			session.save(announcement);
		} else {
			Query query = session.createQuery("FROM Message AS m WHERE m.id = :id");
			query.setInteger("id", id);
			announcement = (Message)query.uniqueResult();
			if (null == announcement) {
				return JsonResponse.getMessageNotFoundInstance(null);
			}
			if (announcement.getToUser() != null) {
				return JsonResponse.getMessageNotFoundInstance(
						"The message of the requested id is not an annoucement");
			}
			announcement.setTitle(title);
			announcement.setContent(content);
			session.update(announcement);
		}
		return new JsonResponse();
	}
	
	@RequestMapping(value = "/announcement/table.do", method = RequestMethod.GET)
	public String listAction(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "number", required = false) Integer number,
			// TODO: search filter
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null != user) {
			model.addAttribute("user_id", user.getUserId());
			model.addAttribute("user_name", user.getName());
			model.addAttribute("isAdmin", user.isAdmin());
		}
		
		if (null == page || page <= 0) page = 1;
		if (null == number || number <= 0) number = 10;
		int offset = number * (page - 1);
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(
				"SELECT COUNT(*) FROM Message WHERE toUser IS NULL AND status = 0");
		Long totalCount = (Long)query.uniqueResult();
		Long totalPages = totalCount / number + (totalCount % number != 0 ? 1 : 0);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("page", page);
		
		query = session.createQuery(
				"FROM Message WHERE toUser IS NULL AND status = 0 ORDER BY create_time DESC");
		query.setFirstResult(offset).setMaxResults(number);
		@SuppressWarnings("unchecked") List<Message> announcements = (List<Message>)query.list();
		model.addAttribute("announcements", announcements);
		
		return "announcement/table";
	}
	
}
