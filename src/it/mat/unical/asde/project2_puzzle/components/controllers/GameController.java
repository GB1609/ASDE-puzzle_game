package it.mat.unical.asde.project2_puzzle.components.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameController {
	@GetMapping("/")
	public String goToGame() {
		return "Game";
	}

}
