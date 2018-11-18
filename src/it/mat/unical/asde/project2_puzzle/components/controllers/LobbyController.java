package it.mat.unical.asde.project2_puzzle.components.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.mat.unical.asde.project2_puzzle.components.services.LobbyService;
import it.mat.unical.asde.project2_puzzle.model.Lobby;

@Controller
public class LobbyController {
	
	@Autowired
	LobbyService lobbyService;
	
	@GetMapping("/")
	public String showLobbies(Model model) {
		model.addAttribute("lobbies", lobbyService.getLobbies());
		return "lobby";
	}

	@GetMapping("create_lobby")
	public String createLobby(Model model, @RequestParam String lobby_name) {
		Lobby newLobby = new Lobby(lobby_name, "Utente "+(int)(Math.random()*100), "");
		if(! lobbyService.addLobby(newLobby)){
			System.out.println(("ADD LOBBY: false || info: "+newLobby));
			model.addAttribute("error","Lobby with this name already exists");
			
			//Can we avoid to use this line? redundant with that in the "showLobbies" method.
			model.addAttribute("lobbies", lobbyService.getLobbies());

			return "lobby";
		}
		System.out.println(("ADD LOBBY: true || info: "+newLobby));
		return "redirect:/";
	}
	
}
