package it.mat.unical.asde.project2_puzzle.components.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

	@PostMapping("move_piece")
	@ResponseBody
	public void movePiece(@RequestParam String old_location, @RequestParam int old_position,
			@RequestParam String new_location, @RequestParam int new_position, @RequestParam String piece) {
		System.out.println(old_position);
		gameService.updateStateGame(old_location, old_position, new_location, new_position, piece);
	}
}
