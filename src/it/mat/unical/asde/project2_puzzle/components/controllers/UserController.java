package it.mat.unical.asde.project2_puzzle.components.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import it.mat.unical.asde.project2_puzzle.components.services.AccountService;

@Controller
public class UserController {

	@Autowired
	private AccountService accountService;

	@GetMapping("/")
	public String index(HttpSession session, Model model) {
		if (checkUserName(session))
			return "redirect:lobby";
		return goToIndex(session, model);
	}

	@GetMapping("logout")
	public String logout(HttpSession session, Model model) {
		session.invalidate();
		return "redirect:/";
	}

	@PostMapping("login")
	public String login(@RequestParam String username, @RequestParam String password, HttpSession session,
			Model model) {

		if (accountService.loginAccepted(username, password)) {
			session.setAttribute("username", username);
			session.setAttribute("avatar", accountService.getAvatarUser(username));
			return "redirect:lobby";
		}

		model.addAttribute("loginFailed", "Username or Password wrong!");
		return goToIndex(session, model);
	}

	@PostMapping("creationAccount")
	public String creationAccount(@RequestParam String firstName, @RequestParam String lastName,
			@RequestParam String username, @RequestParam String password, @RequestParam String avatar,
			HttpSession session, Model model) {

		if (accountService.accountCreated(firstName, lastName, username, password, avatar)) {
			session.setAttribute("username", username);
			session.setAttribute("avatar", accountService.getAvatarUser(username));
			return "redirect:/";
		}
		model.addAttribute("creationFailed", "Username already used.");
		return goToIndex(session, model);
	}

	@GetMapping("userProfile")
	public String goToProfileSettings(HttpSession session, Model model) {
		if (checkUserName(session)) {
			String username = (String) session.getAttribute("username");
			accountService.fillUserInformation(username, model);
			return "userProfile";
		}
		return goToIndex(session, model);
	}

	@PostMapping("updateUserInformation")
	@ResponseBody
	public void updateUserInformation(@RequestParam String firstname, @RequestParam String lastname,
			@RequestParam String password, @RequestParam String avatar, HttpServletResponse response,
			HttpSession session) {

		JSONObject json = new JSONObject();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String username = (String) session.getAttribute("username");

		try {
			if (accountService.updateUserInformation(firstname, lastname, password, username, avatar)) {
				json.append("status", "success");
				session.setAttribute("avatar", accountService.getAvatarUser(username));
			} else
				json.append("status", "error");

			response.getWriter().write(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private boolean checkUserName(HttpSession session) {
		return session.getAttribute("username") != null;
	}

	private String goToIndex(HttpSession session, Model model) {
		model.addAttribute("avatars", accountService.getAvatarsList());
		return "index";
	}
}