package edu.hkpolyu.epre.controller;

import java.io.File;
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

import fyp.JsonResponse;
import fyp.models.Message;
import fyp.models.User;

/**
 * System announcement (toUser IS NULL (to_user_id IS NULL))
 * And Message
 */
@Controller
public class MessageController {
	private SessionFactory sessionFactory;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@RequestMapping(value = "/message/edit.do", method = RequestMethod.GET)
	public String messageAction(
			@RequestParam(value = "id") Integer id,
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return "redirect:index.do";
		model.addAttribute("user_id", user.getUserId());
		model.addAttribute("user_name", user.getName());
		
		if (null != id) {
			Session session = sessionFactory.openSession();
			Query query = session.createQuery("FROM Message AS m WHERE m.id = :id");
			query.setInteger("id", id);
			Message message = (Message)query.uniqueResult();
			if (null != message) {
				model.addAttribute("message", message);
			}
		}
		return "message" + File.pathSeparator + "edit";
	}
	
	@RequestMapping(value = "/message/edit.do", method = RequestMethod.POST)
	public @ResponseBody JsonResponse editMessageAction(
			@RequestParam(value = "id") Integer id,
			@RequestParam(value = "toUserId", required = true) String toUserId,
			@RequestParam(value = "title", required = true) String title,
			@RequestParam(value = "content", required = true) String content,
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return JsonResponse.getFailLoginInstance(null);
		
		Session session = sessionFactory.openSession();
		Message message = null;
		if (null == id || 0 == id) {
			message = new Message();
			message.setFromUser(user);
			message.setTitle(title);
			message.setContent(content);
			session.save(message);
		} else {
			Query query = session.createQuery(
					"FROM Message WHERE id = :id AND toUser IS NOT NULL AND status = 0");
			query.setInteger("id", id);
			message = (Message)query.uniqueResult();
			if (null == message) {
				return JsonResponse.getFailNotFoundInstance(null, Message.class);
			}
			if (message.getFromUser().getId() != user.getId()) {
				return JsonResponse.getFailNotOwnInstance(null, Message.class);
			}
			query = session.createQuery("FROM User AS u WHERE u.userId = :userId");
			query.setString("userId", toUserId);
			User toUser = (User)query.uniqueResult();
			if (null == toUser) {
				return JsonResponse.getFailNotFoundInstance(null, User.class);
			}
			message.setTitle(title);
			message.setContent(content);
			session.update(message);
		}
		return new JsonResponse(); // TODO: 
	}
	
	@RequestMapping(value = "/message/table/from.do", method = RequestMethod.GET)
	public String listFromAction(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "number") Integer number,
			// TODO: search filter
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return "redirect:index.do";
		
		if (null == page || page <= 0) page = 1;
		if (null == number || number <= 0) number = 10;
		int offset = number * (page - 1);
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(
				"SELECT COUNT(*) FROM Message WHERE fromUser.id = :userId:");
		query.setInteger("userId", user.getId());
		Long totalCount = (Long)query.uniqueResult();
		Long totalPages = totalCount / number + (totalCount % number != 0 ? 1 : 0);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("totalPages", totalPages);
		
		query = session.createQuery(
				"FROM Message WHERE fromUser.id = :userId ORDER BY create_time DESC");
		query.setInteger("userId", user.getId()).setFirstResult(offset).setMaxResults(number);
		@SuppressWarnings("unchecked") List<Message> messages = (List<Message>)query.list();
		model.addAttribute("messages", messages);
		model.addAttribute("from", true);
		
		return "message" + File.pathSeparator + "table";
	}

	@RequestMapping(value = "/message/table/to.do", method = RequestMethod.GET)
	public String listToAction(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "number") Integer number,
			// TODO: search filter
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return "redirect:index.do";
		
		if (null == page || page <= 0) page = 1;
		if (null == number || number <= 0) number = 10;
		int offset = number * (page - 1);
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(
				"SELECT COUNT(*) FROM Message WHERE toUser.id = :userId:");
		query.setInteger("userId", user.getId());
		Long totalCount = (Long)query.uniqueResult();
		Long totalPages = totalCount / number + (totalCount % number != 0 ? 1 : 0);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("totalPages", totalPages);
		
		query = session.createQuery(
				"FROM Message WHERE toUser.id = :userId ORDER BY create_time DESC");
		query.setInteger("userId", user.getId()).setFirstResult(offset).setMaxResults(number);
		@SuppressWarnings("unchecked") List<Message> messages = (List<Message>)query.list();
		model.addAttribute("messages", messages);
		model.addAttribute("from", false);
		
		return "message" + File.pathSeparator + "table";
	}
}
