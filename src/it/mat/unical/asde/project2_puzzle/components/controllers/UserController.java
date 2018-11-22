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

import it.mat.unical.asde.project2_puzzle.components.services.AccountService;
import it.mat.unical.asde.project2_puzzle.model.Credentials;
import it.mat.unical.asde.project2_puzzle.model.User;

@Controller
public class UserController {

	@Autowired
	private AccountService accountService;

	@GetMapping("/")
	public String index(HttpSession session, Model model) {

		if (session.getAttribute("username") != null) {
//			return "lobby";
			return goToUserProfile((String) session.getAttribute("username"), model);
		}
		return "index";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session, Model model) {
		session.invalidate();
		return "redirect:/";
	}

	@GetMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, HttpSession session,
			Model model) {

		if (!(username == "" || password == "")) {
			if (accountService.loginAccepted(username, password)) {
				session.setAttribute("username", username);
				return "redirect:/";

			}
			model.addAttribute("loginFailed", "Username or Password wrong!");
		} else {
			if (username == "")
				model.addAttribute("loginFailed1", "Fields cannot be empty!");
			if (password == "")
				model.addAttribute("loginFailed2", "Fields cannot be empty!");
		}
		return "index";
	}

	@GetMapping("/creationAccount")
	public String creationAccount(@RequestParam String firstName, @RequestParam String lastName,
			@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
		if (!(username == "" || password == "" || firstName == "" || lastName == "")) {
			if (accountService.accountCreated(firstName, lastName, username, password)) {
				session.setAttribute("username", username);
				return "redirect:/";
			}
			model.addAttribute("creationFailed", "Username already used.");
			model.addAttribute("creationFailedValue3", 3);
		} else {
			model.addAttribute("creationFailed", "Fields cannot be empty!");
			if (firstName == "")
				model.addAttribute("creationFailedValue1", 1);
			if (lastName == "")
				model.addAttribute("creationFailedValue2", 2);
			if (username == "")
				model.addAttribute("creationFailedValue3", 3);
			if (password == "")
				model.addAttribute("creationFailedValue4", 4);

		}
		return "index";
	}

	@GetMapping("goToProfileSettings")
	public String goToProfileSettings(HttpSession session, Model model) {

		if (session.getAttribute("username") != null) {
			String username = (String) session.getAttribute("username");
			return goToUserProfile(username, model);
		}
		return "index";
	}

	@PostMapping("updateUserInformation")
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

	// TODO testing only user profile page
	public String goToUserProfile(String username, Model model) {
		User user = accountService.getUser(username);
		Credentials credentials = accountService.getCredentials(username);
		model.addAttribute("firstname", user.getFirstName());
		model.addAttribute("lastname", user.getLastName());
		model.addAttribute("password", credentials.getPassword());
		model.addAttribute("matches", accountService.getMatches(username));

		return "userProfile";
	}

}