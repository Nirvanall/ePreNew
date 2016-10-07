package edu.hkpolyu.epre.controller;

import java.util.List;

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

import edu.hkpolyu.common.response.JsonResponse;
import edu.hkpolyu.epre.model.Assessment;
import edu.hkpolyu.epre.model.Presentation;
import edu.hkpolyu.epre.model.User;
import edu.hkpolyu.epre.model.Video;

@Controller
@RequestMapping("/video")
public class VideoController {
	private SessionFactory sessionFactory;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@RequestMapping(value = "/view.do", method = RequestMethod.GET)
	public String viewAction(
			@RequestParam(value = "id") Integer videoId,
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return "redirect:../index.do";
		model.addAttribute("user_id", user.getUserId());
		model.addAttribute("user_name", user.getName());
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM Video v WHERE v.id = :videoId AND v.status = 0");
		query.setInteger("videoId", videoId);
		Video v = (Video)query.uniqueResult();
		if (null == v)
			return "errors/video-not-found"; // TODO: video not found page
		
		if (v.getOwner().getId() != user.getId()) {
			query = session.createQuery("FROM Assessment a " +
					"WHERE a.video.id = :videoId AND a.viewer.id = :userId AND a.status = 0");
			query.setInteger("videoId", videoId).setInteger("userId", user.getId());
			Assessment a = (Assessment)query.uniqueResult();
			if (null == a)
				return "errors/no-permission"; // TODO: no access permission page
			
			model.addAttribute("assessment", a);
		} else {
			model.addAttribute("assessment", null);
		}
		model.addAttribute("video", v);
		
		return "video";
	}
	
	@RequestMapping(value = "/add.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResponse addAction(
			@RequestParam(value = "presentation_id") Integer presentationId,
			@RequestParam(value = "user_id") String userId,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "info") String info,
			HttpSession httpSession
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return JsonResponse.getNeedLoginInstance(null);
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM Presentation WHERE id = :presentationId AND status = 0");
		query.setInteger("presentationId", presentationId);
		Presentation p = (Presentation)query.uniqueResult();
		if (null == p) {
			return JsonResponse.getPresentationNotFoundInstance(null);
		}
		
		query = session.createQuery("FROM User WHERE userId = :userId AND status = 0");
		query.setString("userId", userId);
		User u = (User)query.uniqueResult();
		if (null == u) {
			return JsonResponse.getUserNotFoundInstance(null);
		}
		
		Video v = new Video();
		v.setPresentation(p);
		v.setOwner(u);
		v.setName(name);
		v.setInfo(info);
		session.saveOrUpdate(v);
		
		return new JsonResponse();
	}
	
	@RequestMapping(value = "/upload.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResponse uploadAction(
			@RequestParam(value = "id", required = true) Integer videoId,
			HttpSession httpSession
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return JsonResponse.getNeedLoginInstance(null);
		
		Session session = sessionFactory.openSession();
		
		return new JsonResponse();
	}
	
	@RequestMapping(value = "/update.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResponse updateAction(
			@RequestParam(value = "id", required = true) Integer videoId,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "info", required = false) String info,
			HttpSession httpSession
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return JsonResponse.getNeedLoginInstance(null);
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM Video WHERE id = :videoId AND status = 0");
		query.setInteger("videoId", videoId);
		Video v = (Video)query.uniqueResult();
		if (null == v)
			return JsonResponse.getVideoNotFoundInstance(null);
		
		if (null != userId && userId.length() > 0) {
			query = session.createQuery("FROM User WHERE userId = :userId AND status = 0");
			query.setString("userId", userId);
			User u = (User)query.uniqueResult();
			v.setOwner(u);
		}
		if (null != name && name.length() > 0) v.setName(name);
		if (null != info && info.length() > 0) v.setInfo(info);
		session.saveOrUpdate(v);
		
		return new JsonResponse();
	}
	
	public JsonResponse deleteAction(
			@RequestParam(value = "id", required = true) Integer videoId,
			HttpSession httpSession
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return JsonResponse.getNeedLoginInstance(null);
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM Video WHERE id = :videoId AND status = 0");
		query.setInteger("videoId", videoId);
		Video v = (Video)query.uniqueResult();
		if (null == v)
			return JsonResponse.getVideoNotFoundInstance(null);
		
		v.statusDeleted();
		session.saveOrUpdate(v);
		
		return new JsonResponse();
	}
}
