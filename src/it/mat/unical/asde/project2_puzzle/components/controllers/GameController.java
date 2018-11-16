package it.mat.unical.asde.project2_puzzle.components.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.mat.unical.asde.project2_puzzle.components.services.GameService;

@Controller
public class GameController {
	@Autowired
	GameService gameService;

	@GetMapping("/")
	public String goToGame(Model m) {
		m.addAttribute("randomGrid", gameService.getRandomGrid());
		return "Game";
	}

}
