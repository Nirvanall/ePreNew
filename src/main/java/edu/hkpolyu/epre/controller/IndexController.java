package edu.hkpolyu.epre.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import edu.hkpolyu.epre.service.AnnouncementService;
import edu.hkpolyu.epre.model.Message;

/**
 * Homepage
 * Login page and the first 5 announcements
 */
@Controller
public class IndexController {
	private AnnouncementService announcementService;
	@Autowired
	public void setAnnouncementService(AnnouncementService announcementService) {
		this.announcementService = announcementService;
	}
	
	@RequestMapping(value = "/index.do", method = RequestMethod.GET)
	public String indexAction(
			@RequestParam(value = "source", required = false) String source,
			Model model) {
		Iterable<Message> announcements = announcementService.listAnnouncement(1, 6);
		model.addAttribute("announcements", announcements);
		
		if (null != source && source.equalsIgnoreCase("login")) {
			model.addAttribute("error_message", "Incorrect UserID / Incorrect Password");
		}
		return "index";
	}
}
