package fyp.controllers;

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

@Controller
public class MessageController {
	private SessionFactory sessionFactory;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@RequestMapping(value = "admin/announcement/edit.do", method = RequestMethod.GET)
	public String announcementAction(
			@RequestParam(value = "id") Integer id,
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
		return "admin" + File.pathSeparator + "annoucement" + File.pathSeparator + "edit";
	}
	
	@RequestMapping(value = "admin/announcement/edit.do", method = RequestMethod.POST)
	public @ResponseBody JsonResponse editAnnoucementAction(
			@RequestParam(value = "id") Integer id,
			@RequestParam(value = "title", required = true) String title,
			@RequestParam(value = "content", required = true) String content,
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user || !user.isAdmin()) {
			return JsonResponse.getFailLoginInstance(null);
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
				return JsonResponse.getFailNotFoundInstance(null, Message.class);
			}
			if (announcement.getToUser() != null) {
				return JsonResponse.getFailInstance(
						"The message of the requested id is not an annoucement");
			}
			announcement.setTitle(title);
			announcement.setContent(content);
			session.update(announcement);
		}
		return new JsonResponse("{message_id:" + announcement.getId() + "}");
	}
	
	@RequestMapping(value = "admin/announcement/table.do", method = RequestMethod.GET)
	public String listMessageAction(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "number") Integer number,
			// TODO: search filter
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return "redirect:index.do";
		model.addAttribute("user_id", user.getUserId());
		model.addAttribute("user_name", user.getName());
		
		if (null == page || page <= 0) page = 1;
		if (null == number || number <= 0) number = 10;
		int offset = number * (page - 1);
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(
				"FROM Message AS m WHERE m.toUser IS NULL ORDER BY m.create_time DESC");
		query.setFirstResult(offset).setMaxResults(number);
		@SuppressWarnings("unchecked")
		List<Message> announcements = (List<Message>)query.list();
		model.addAttribute("announcements", announcements);
		if (user.isAdmin()) {
			// model.addAttribute("", "");
		}
		return "admin" + File.pathSeparator + "announcement" + File.pathSeparator + "table";
	}
	
	
	@RequestMapping(value = "message/edit.do", method = RequestMethod.GET)
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
	
	@RequestMapping(value = "message/edit.do", method = RequestMethod.POST)
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
					"FROM Message AS m WHERE m.id = :id AND m.toUser IS NOT NULL");
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
		return new JsonResponse("{message_id:" + message.getId() + "}");
	}
	
	@RequestMapping(value = "message/table.do", method = RequestMethod.GET)
	public String messageTableAction(
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
				"FROM Message AS m WHERE m.toUser = :toUser ORDER BY m.create_time DESC");
		query.setParameter("toUser", user).setFirstResult(offset).setMaxResults(number);
		@SuppressWarnings("unchecked")
		List<Message> messages = (List<Message>)query.list();
		model.addAttribute("messages", messages);
		return "message" + File.pathSeparator + "table";
	}
}
