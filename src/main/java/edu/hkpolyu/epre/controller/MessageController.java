package edu.hkpolyu.epre.controller;

import java.io.File;
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
import edu.hkpolyu.epre.service.UserService;
import edu.hkpolyu.epre.service.MessageService;
import edu.hkpolyu.epre.model.Message;
import edu.hkpolyu.epre.model.User;

@Controller
public class MessageController {
	private MessageService messageService;
	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	
	private UserService userService;
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(value = "/message/edit.do", method = RequestMethod.GET)
	public String messageAction(
			@RequestParam(value = "id") Integer id,
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return "redirect:index.do";
		model.addAttribute("user_id", user.getUserName());
		model.addAttribute("user_name", user.getName());
		
		if (null != id) {
			Message message = messageService.getMessageById(id);
			if (null != message) {
				model.addAttribute("message", message);
			}
		}
		return "message" + File.pathSeparator + "edit";
	}
	
	@RequestMapping(value = "/message/edit.do", method = RequestMethod.POST)
	public @ResponseBody JsonResponse editMessageAction(
			@RequestParam(value = "id") Integer id,
			@RequestParam(value = "to_user_name", required = true) String toUserName,
			@RequestParam(value = "title", required = true) String title,
			@RequestParam(value = "content", required = true) String content,
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return JsonResponse.getNeedLoginInstance(null);
		
		Message message = null;
		if (null == id || 0 == id) {
			message = new Message();
			message.setFromUser(user);
		} else {
			message = messageService.getMessageById(id);
			if (null == message) {
				return JsonResponse.getMessageNotFoundInstance(null);
			}
			if (message.getFromUser().getId() != user.getId()) {
				return JsonResponse.getNoPermissionInstance("You cannot edit the message that is not sent by you");
			}
		}
		User toUser = userService.getUserByUserName(toUserName);
		if (null == toUser) {
			return JsonResponse.getUserNotFoundInstance(null);
		}
		message.setTitle(title);
		message.setContent(content);
		messageService.saveMessage(message);

		return new JsonResponse(); // TODO: 
	}
	
	@RequestMapping(value = "/message/table/from.do", method = RequestMethod.GET)
	public String listFromAction(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "size") Integer size,
			// TODO: search filter
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return "redirect:index.do";
		
		if (null == page || page <= 0) page = 1;
		if (null == size || size <= 0) size = 10;
		int offset = size * (page - 1);
		
		Page<Message> messages = messageService.listMessageByFromUserId(
                user.getId(), page, size);
		model.addAttribute("totalCount", messages.getTotalElements());
		model.addAttribute("totalPages", messages.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("messages", messages);
		model.addAttribute("from", true);
		
		return "message" + File.pathSeparator + "table";
	}

	@RequestMapping(value = "/message/table/to.do", method = RequestMethod.GET)
	public String listToAction(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "size") Integer size,
			// TODO: search filter
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return "redirect:index.do";
		
		if (null == page || page <= 0) page = 1;
		if (null == size || size <= 0) size = 10;
		int offset = size * (page - 1);
		
		Page<Message> messages = messageService.listMessageByToUserId(
                user.getId(), page, size);
		model.addAttribute("totalCount", messages.getTotalElements());
		model.addAttribute("totalPages", messages.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("messages", messages);
		model.addAttribute("from", false);
		
		return "message" + File.pathSeparator + "table";
	}
}
