package edu.hkpolyu.epre.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
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
import edu.hkpolyu.epre.service.AssessmentService;
import edu.hkpolyu.epre.service.VideoService;
import edu.hkpolyu.epre.service.PresentationService;
import edu.hkpolyu.epre.service.UserService;

@Controller
@RequestMapping("/video")
public class PresentationController {
	private PresentationService presentationService;
	@Autowired
	public void setPresentationService(PresentationService presentationService) {
		this.presentationService = presentationService;
	}
	
	private UserService userService;
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
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
	
	@RequestMapping(value = "/view.do", method = RequestMethod.GET)
	public String viewAction(
			@RequestParam(value = "id") Integer videoId,
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return "redirect:../index.do";
		model.addAttribute("user_id", user.getUserName());
		model.addAttribute("user_name", user.getName());
		
		Video video = videoService.getVideoById(videoId);
		if (null == video)
			return "errors/video-not-found"; // TODO: video not found page
		
		if (video.getOwner().getId() != user.getId()) {
			Assessment assessment = assessmentService.getAssessmentByVideoIdAndViewerId(
                    videoId, user.getId());
			if (null == assessment)
				return "errors/no-permission"; // TODO: no access permission page
			
			model.addAttribute("assessment", assessment);
		} else {
			model.addAttribute("assessment", null);
		}
		model.addAttribute("video", video);
		
		return "video";
	}
	
	@RequestMapping(value = "/add.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResponse addAction(
			@RequestParam(value = "presentation_id") Integer presentationId,
			@RequestParam(value = "user_name") String userName,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "info") String info,
			HttpSession httpSession
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return JsonResponse.getNeedLoginInstance(null);
		
		Presentation p = presentationService.getPresentationById(presentationId);
		if (null == p) {
			return JsonResponse.getPresentationNotFoundInstance(null);
		}
		
		User u = userService.getUserByUserName(userName);
		if (null == u) {
			return JsonResponse.getUserNotFoundInstance(null);
		}
		
		Video v = new Video();
		v.setPresentation(p);
		v.setOwner(u);
		v.setName(name);
		v.setInfo(info);
		videoService.saveVideo(v);
		
		return new JsonResponse();
	}
	
	@RequestMapping(value = "/upload.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResponse uploadAction(
			@RequestParam(value = "id", required = true) Integer videoId,
			HttpSession httpSession
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return JsonResponse.getNeedLoginInstance(null);
		
		// TODO:
		return new JsonResponse();
	}
	
	@RequestMapping(value = "/update.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResponse updateAction(
			@RequestParam(value = "id", required = true) Integer videoId,
			@RequestParam(value = "user_name", required = false) String userName,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "info", required = false) String info,
			HttpSession httpSession
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return JsonResponse.getNeedLoginInstance(null);
		
		Video v = videoService.getVideoById(videoId);
		if (null == v)
			return JsonResponse.getVideoNotFoundInstance(null);
		
		if (null != userName && userName.length() > 0) {
			User u = userService.getByUserName(userName);
			v.setOwner(u);
		}
		if (null != name && name.length() > 0) v.setName(name);
		if (null != info && info.length() > 0) v.setInfo(info);
		videoService.saveVideo(v);
		
		return new JsonResponse();
	}
	
	public JsonResponse deleteAction(
			@RequestParam(value = "id", required = true) Integer videoId,
			HttpSession httpSession
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return JsonResponse.getNeedLoginInstance(null);
		
		Video v = videoService.getVideoById(videoId);
		if (null == v)
			return JsonResponse.getVideoNotFoundInstance(null);
		
		v.statusDeleted();
		videoService.saveVideo(v);
		
		return new JsonResponse();
	}
}
