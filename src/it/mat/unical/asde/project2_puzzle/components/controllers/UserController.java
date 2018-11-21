package it.mat.unical.asde.project2_puzzle.components.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.mat.unical.asde.project2_puzzle.components.services.AccountService;

@Controller
public class UserController {

	@Autowired
	private AccountService accountService;

	@GetMapping("/")
	public String index(HttpSession session, Model model) {

		if (session.getAttribute("username") != null) {
			return "lobby";
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
		} else
		{
			if(username=="")
			model.addAttribute("loginFailed1", "Fields cannot be empty!");
			if(password=="")
				model.addAttribute("loginFailed2", "Fields cannot be empty!");
		}
		return "index";
	}

	@GetMapping("/creationAccount")
	public String creationAccount(@RequestParam String firstName, @RequestParam String lastName,
			@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
		if (!(username == "" || password == "" || firstName == "" || lastName == "")) {
			if (accountService.accountCreated(firstName,lastName,username, password)) {
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

}