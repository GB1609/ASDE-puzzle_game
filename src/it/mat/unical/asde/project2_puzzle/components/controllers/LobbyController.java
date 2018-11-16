package it.mat.unical.asde.project2_puzzle.components.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.mat.unical.asde.project2_puzzle.components.services.LobbyService;

@Controller
public class LobbyController {
	
	@Autowired
	LobbyService lobbyService;
	
	@GetMapping("/")
	public String goToGame(Model model) {
		model.addAttribute("lobbies", lobbyService.getLobbies());
		return "lobby";
	}
}
