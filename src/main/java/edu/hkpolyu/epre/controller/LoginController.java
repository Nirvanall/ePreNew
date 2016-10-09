package edu.hkpolyu.epre.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import edu.hkpolyu.common.response.JsonResponse;
import edu.hkpolyu.epre.service.UserService;
import edu.hkpolyu.epre.service.UserPasswordService;
import edu.hkpolyu.epre.model.User;
import edu.hkpolyu.epre.model.UserPassword;

@Controller
public class LoginController {
	private UserService userService;
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	private UserPasswordService passwordService;
	@Autowired
	public void setUserPasswordService(UserPasswordService passwordService) {
		this.passwordService = passwordService;
	}
	
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String loginAction(
			@RequestParam(value="user", required=true) String userName,
			@RequestParam(value="password", required=true) String password,
			HttpSession httpSession,
			Model model
	) {
		User user = userService.getUserByUserNameAndPassword(userName, password);
		if (null == user) return "redirect:index.do?source=login";

		httpSession.setAttribute("user", user);
		
		if (user.isAdmin()) return "redirect:admin.do";
		if (user.isTeacher()) return "redirect:teacher.do";
		if (user.isStudent()) return "redirect:student.do";
		return "redirect:guest.do";
	}
	
	@RequestMapping(value = "/logout.do")
	public String logoutAction(
			HttpSession httpSession,
			Model model
	) {
		httpSession.removeAttribute("user");
		return "redirect:index.do";
	}
	
	@RequestMapping(value = "/password.do", method = RequestMethod.GET)
	public String passwordAction(
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return "redirect:index.do";
		model.addAttribute("user_id", user.getUserName());
		model.addAttribute("user_name", user.getName());
		return "password";
	}
	
	
	@RequestMapping(value = "/password.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JsonResponse updatePasswordAction(
			@RequestParam(value = "old_password", required = true) String oldPassword,
			@RequestParam(value = "new_password", required = true) String newPassword,
			HttpSession httpSession,
			Model model
	) {
		User user = (User)httpSession.getAttribute("user");
		if (null == user) return JsonResponse.getNeedLoginInstance(null);
		
		if (passwordService.isPasswordCorrect(user.getId(), oldPassword)) {
            return JsonResponse.getWrongPasswordInstance(null);
        }
		
		passwordService.savePassword(user.getId(), newPassword);
		httpSession.setAttribute("user", user);
		
		return new JsonResponse();
	}
}
