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

		if (session.getAttribute("username") != null) {
//			return "lobby";
			return goToProfileSettings(session, model);
		}
		return "index";
	}

	@GetMapping("logout")
	public String logout(HttpSession session, Model model) {
		session.invalidate();
		return "redirect:/";
	}

	@GetMapping("login")
	public String login(@RequestParam String username, @RequestParam String password, HttpSession session,
			Model model) {

		if (accountService.loginAccepted(username, password)) {
			session.setAttribute("username", username);
			return goToProfileSettings(session, model);
		}

		model.addAttribute("loginFailed", "Username or Password wrong!");
		return "index";
	}

	@GetMapping("creationAccount")
	public String creationAccount(@RequestParam String firstName, @RequestParam String lastName,
			@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {

		if (accountService.accountCreated(firstName, lastName, username, password)) {
			session.setAttribute("username", username);
			return "redirect:/";
		}
		model.addAttribute("creationFailed", "Username already used.");
		return "index";
	}

	@GetMapping("userProfile")
	public String goToProfileSettings(HttpSession session, Model model) {
		if (session.getAttribute("username") != null) {
			String username = (String) session.getAttribute("username");
			accountService.fillUserInformation(username, model);
			return "userProfile";
		}
		return "index";
	}

	@PostMapping("updateUserInformation")
	@ResponseBody
	public void updateUserInformation(@RequestParam String firstname, @RequestParam String lastname,
			@RequestParam String password, HttpServletResponse response, HttpSession session) {

		JSONObject json = new JSONObject();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String username = (String) session.getAttribute("username");

		try {
			if (accountService.updateUserInformation(firstname, lastname, password, username))
				json.append("status", "success");
			else
				json.append("status", "error");

			response.getWriter().write(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}