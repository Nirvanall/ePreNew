package edu.hkpolyu.epre.controller;

import java.util.HashMap;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import edu.hkpolyu.common.response.JsonResponse;
import edu.hkpolyu.epre.service.AssessmentService;
import edu.hkpolyu.epre.service.VideoService;
import edu.hkpolyu.epre.service.CommentService;
import edu.hkpolyu.epre.model.Assessment;
import edu.hkpolyu.epre.model.Comment;
import edu.hkpolyu.epre.model.User;
import edu.hkpolyu.epre.model.Video;

@RestController
@RequestMapping("/comment")
public class CommentController {
	private VideoService videoService;
	@Autowired
	public void setVideoService(VideoService videoService) {
		this.videoService = videoService;
	}
	
	private AssessmentService assessmentService;
	@Autowired
	public void setAssessmentService(AssessmentService assessmentService) {
		this.assessmentService = assessmentService;
	}
	
	private CommentService commentService;
	@Autowired
	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
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
		
		Video v = videoService.getVideoById();
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
