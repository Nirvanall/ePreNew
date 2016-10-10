package edu.hkpolyu.epre.controller;

import javax.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import edu.hkpolyu.common.response.JsonResponse;
import edu.hkpolyu.epre.service.VideoService;
import edu.hkpolyu.epre.model.User;
import edu.hkpolyu.epre.model.Video;

/**
 * Student Page
 * The list of the student's videos
 */
@Controller
public class StudentController {
	private VideoService videoService;
	@Autowired
	public void setVideoService(VideoService videoService) {
		this.videoService = videoService;
	}
	
	@RequestMapping(value = "/student.do", method = RequestMethod.GET)
	public String indexAction(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user || !user.isStudent()) return "redirect:index.do";
		model.addAttribute("user_id", user.getUserName());
		model.addAttribute("user_name", user.getName());
		
		if (null == page || page <= 0) page = 1;
		if (null == size || size <= 0) size = 10;
		int offset = size * (page - 1);
		model.addAttribute("page", page);
		
		Page<Video> videos = videoService.listVideoOfUser(
                user.getId(), page, size);
		model.addAttribute("totalCount", videos.getTotalElements());
		model.addAttribute("totalPages", videos.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("videos", videos);
		
		return "student";
	}
}
