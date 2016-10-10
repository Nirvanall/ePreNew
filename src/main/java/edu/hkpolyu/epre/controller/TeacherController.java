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

@Controller
public class TeacherController {
	private VideoService videoService;
	@Autowired
	public void setVideoService(VideoService videoService) {
		this.videoService = videoService;
	}
	
	@RequestMapping(value = "/teacher.do", method = RequestMethod.GET)
	public String indexAction(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user || !user.isTeacher()) return "redirect:index.do";
		model.addAttribute("user_id", user.getUserName());
		model.addAttribute("user_name", user.getName());
		
		if (null == page || page <= 0) page = 1;
		if (null == size || size <= 0) size = 10;
		int offset = size * (page - 1);
		
		Page<Video> videos = videoService.listVideoByAssessor(
                user.getId(), page, size);
		model.addAttribute("totalCount", videos.getTotalElements());
		model.addAttribute("totalPages", videos.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("videos", videos);
		
		return "teacher";
	}
}
