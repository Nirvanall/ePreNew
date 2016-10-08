package edu.hkpolyu.epre.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.domain.Page;
import edu.hkpolyu.common.response.JsonResponse;
import edu.hkpolyu.epre.service.AnnouncementService;
import edu.hkpolyu.epre.model.Message;
import edu.hkpolyu.epre.model.User;

/**
 * System announcement (toUser IS NULL (to_user_id IS NULL))
 * And Message
 */
@Controller
public class AnnouncementController {
	private AnnouncementService announcementService;
	@Autowired
	public void setAnnouncementService(AnnouncementService announcementService) {
		this.announcementService = announcementService;
	}
	
	/**
	 * System announcement creation/edition page
	 * @param  Integer id	   Edit: The id of the announcement to be edited
	 *						  Create: NULL
	 * @param  HttpSession httpSession
	 * @param  Model model
	 * @return String
	 */
	@RequestMapping(value = "/admin/announcement/edit.do", method = RequestMethod.GET)
	public String editPageAction(
			@RequestParam(value = "id", required = false) Integer id,
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user || !user.isAdmin()) return "redirect:index.do";
		model.addAttribute("user_id", user.getUserName());
		model.addAttribute("user_name", user.getName());
		
		if (null != id) {
			Message announcement = announcementService.getAnnouncementById(id);
			if (null != announcement) {
				model.addAttribute("announcement", announcement);
			}
		}
		return "admin/announcement/edit";
	}
	
	/**
	 * Create/edit a system announcement
	 * @param  Integer id	   Edit: The id of the announcement to be edited
	 *						  Create: NULL
	 * @param  String title	 The title of the announcement
	 * @param  String content   The content of the announcement
	 * @param  HttpSession httpSession
	 * @param  Model model
	 * @return JsonResponse
	 */
	@RequestMapping(value = "/admin/announcement/edit.do", method = RequestMethod.POST)
	public @ResponseBody JsonResponse editAction(
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "title", required = true) String title,
			@RequestParam(value = "content", required = true) String content,
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user || !user.isAdmin()) {
			return JsonResponse.getNeedLoginInstance(null);
		}
		
		Message announcement = null;
		if (null == id || 0 == id) {
			announcement = new Message();
			announcement.setFromUser(user);
			announcement.setTitle(title);
			announcement.setContent(content);
		} else {
			announcement = announcementService.getAnnouncementById(id);
			if (null == announcement) {
				return JsonResponse.getMessageNotFoundInstance(null);
			}
			if (announcement.getToUser() != null) {
				return JsonResponse.getMessageNotFoundInstance(
						"The message of the requested id is not an annoucement");
			}
			announcement.setTitle(title);
			announcement.setContent(content);
		}
		announcement = announcementService.saveAnnouncement(announcement);
		return new JsonResponse();
	}
	
	@RequestMapping(value = "/announcement/table.do", method = RequestMethod.GET)
	public String listAction(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			// TODO: search filter
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null != user) {
			model.addAttribute("user_id", user.getUserName());
			model.addAttribute("user_name", user.getName());
			model.addAttribute("isAdmin", user.isAdmin());
		}
		
		if (null == page || page <= 0) page = 1;
		if (null == size || size <= 0) size = 10;
		
		Page<Message> announcements = announcementService.listAnnouncement(
                page, size);
		model.addAttribute("totalCount", announcements.getTotalElements());
		model.addAttribute("totalPages", announcements.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("announcements", announcements);
		
		return "announcement/table";
	}
	
}
