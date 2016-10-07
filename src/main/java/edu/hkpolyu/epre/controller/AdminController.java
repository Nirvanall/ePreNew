package edu.hkpolyu.epre.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.hkpolyu.epre.model.User;

@Controller
public class AdminController {
	@RequestMapping(value = "/admin.do", method = RequestMethod.GET)
	public String indexAction(
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user || !user.isAdmin()) return "redirect:index.do";
		model.addAttribute("user_id", user.getUserId());
		model.addAttribute("user_name", user.getName());
		
		return "admin";
	}
	
}
