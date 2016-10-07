package edu.hkpolyu.epre.controller;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.hkpolyu.common.response.JsonResponse;
import edu.hkpolyu.epre.model.Assessment;
import edu.hkpolyu.epre.model.Comment;
import edu.hkpolyu.epre.model.User;
import edu.hkpolyu.epre.model.Video;

@RestController
@RequestMapping("/comment")
public class CommentController {
	private SessionFactory sessionFactory;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@RequestMapping(value = "/create.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResponse createAction(
			@RequestParam(value = "video_id") Integer videoId,
			@RequestParam(value = "playtime") Float playtime,
			@RequestParam(value = "content") String content,
			HttpSession httpSession
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return JsonResponse.getNeedLoginInstance(null);
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM Video v WHERE v.id = :videoId AND v.status = 0");
		query.setInteger("videoId", videoId);
		Video v = (Video)query.uniqueResult();
		if (null == v)
			return JsonResponse.getVideoNotFoundInstance(null);
		
		query = session.createQuery("FROM Assessment a " +
				"WHERE a.video.id = :videoId AND a.viewer.id = :userId AND a.status = 0");
		query.setInteger("videoId", videoId).setInteger("userId", user.getId());
		Assessment a = (Assessment)query.uniqueResult();
		if (null == a || !a.canComment())
			return JsonResponse.getNoPermissionInstance("You do not have the permission to comment this video");
			
		Comment c = new Comment();
		c.setCommenter(user);
		c.setVideo(v);
		c.setPlaytime(playtime);
		c.setContent(content);
		session.saveOrUpdate(c);
		
		HashMap<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("name", user.getName());
		HashMap<String, Object> commentMap = new HashMap<String, Object>();
		commentMap.put("id", c.getId());
		commentMap.put("commenter", userMap);
		return new JsonResponse(commentMap);
	}
	
	@RequestMapping(value = "/update.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResponse updateAction(
			@RequestParam(value = "id") Integer commentId,
			@RequestParam(value = "content") String content,
			HttpSession httpSession
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return JsonResponse.getNeedLoginInstance(null);
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM Comment WHERE id = :commentId AND status = 0");
		query.setInteger("commentId", commentId);
		Comment c = (Comment)query.uniqueResult();
		if (null == c)
			return JsonResponse.getCommentNotFoundInstance(null);
		if (c.getCommenter().getId() != user.getId())
			return JsonResponse.getNoPermissionInstance("You cannot edit the comment that is not yours");
		
		c.setContent(content);
		session.saveOrUpdate(c);
		
		return new JsonResponse();
	}
	
	@RequestMapping(value = "/delete.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResponse deleteAction(
			@RequestParam(value = "id") Integer commentId,
			HttpSession httpSession
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return JsonResponse.getNeedLoginInstance(null);
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM Comment WHERE id = :commentId");
		query.setInteger("commentId", commentId);
		Comment c = (Comment)query.uniqueResult();
		if (null == c)
			return JsonResponse.getCommentNotFoundInstance(null);
		if (c.getCommenter().getId() != user.getId())
			return JsonResponse.getNoPermissionInstance("You cannot delete the comment that is not yours");
		
		if (!c.isStatusDeleted()) {
			c.statusDeleted();
			session.saveOrUpdate(c);
		}
		
		return new JsonResponse();
	}
}
