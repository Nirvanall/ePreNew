package fyp.controllers;

import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fyp.JsonResponse;
import fyp.models.Assessment;
import fyp.models.Comment;
import fyp.models.StatusTimeModel;
import fyp.models.User;
import fyp.models.Video;

@Controller
@RequestMapping("/comment")
public class CommentController {
	private SessionFactory sessionFactory;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResponse createAction(
			@RequestParam(value = "video_id") Integer videoId,
			@RequestParam(value = "playtime") Float playtime,
			@RequestParam(value = "content") String content,
			HttpSession httpSession
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return JsonResponse.getFailLoginInstance(null);
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM Video v WHERE v.id = :videoId AND v.status = 0");
		query.setInteger("videoId", videoId);
		Video v = (Video)query.uniqueResult();
		if (null == v)
			return JsonResponse.getFailNotFoundInstance(null, Video.class);
		
		query = session.createQuery("FROM Assessment a " +
				"WHERE a.video.id = :videoId AND a.viewer.id = :userId AND a.status = 0");
		query.setInteger("videoId", videoId).setInteger("userId", user.getId());
		Assessment a = (Assessment)query.uniqueResult();
		if (null == a || !a.canComment())
			return JsonResponse.getFailPermissionInstance(null);
			
		Comment c = new Comment();
		c.setCommenter(user);
		c.setPlaytime(playtime);
		c.setContent(content);
		session.saveOrUpdate(c);
		
		return new JsonResponse();
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResponse updateAction(
			@RequestParam(value = "id") Integer commentId,
			@RequestParam(value = "content") String content,
			HttpSession httpSession
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return JsonResponse.getFailLoginInstance(null);
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM Comment WHERE id = :commentId AND status = 0");
		query.setInteger("commentId", commentId);
		Comment c = (Comment)query.uniqueResult();
		if (null == c)
			return JsonResponse.getFailNotFoundInstance(null, Comment.class);
		if (c.getCommenter().getId() != user.getId())
			return JsonResponse.getFailNotOwnInstance(null, Comment.class);
		
		c.setContent(content);
		session.saveOrUpdate(c);
		
		return new JsonResponse();
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResponse deleteAction(
			@RequestParam(value = "id") Integer commentId,
			HttpSession httpSession
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return JsonResponse.getFailLoginInstance(null);
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM Comment WHERE id = :commentId");
		query.setInteger("commentId", commentId);
		Comment c = (Comment)query.uniqueResult();
		if (null == c)
			return JsonResponse.getFailNotFoundInstance(null, Comment.class);
		if (c.getCommenter().getId() != user.getId())
			return JsonResponse.getFailNotOwnInstance(null, Comment.class);
		
		if (c.getStatus() != StatusTimeModel.STATUS_DELETED) {
			c.setStatus(StatusTimeModel.STATUS_DELETED);
			session.saveOrUpdate(c);
		}
		
		return new JsonResponse();
	}
}
