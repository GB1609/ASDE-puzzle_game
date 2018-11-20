package it.mat.unical.asde.project2_puzzle.components.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.mat.unical.asde.project2_puzzle.components.services.LobbyService;
import it.mat.unical.asde.project2_puzzle.model.Lobby;

@Controller
public class LobbyController {

	@Autowired
	LobbyService lobbyService;

	@GetMapping("lobby")
	public String showLobbies(Model model) {
		model.addAttribute("lobbies", this.lobbyService.getLobbies());
		return "lobby";
	}

	@GetMapping("create_lobby")
	public String createLobby(Model model, HttpSession session, @RequestParam String lobby_name) {
		Lobby newLobby = new Lobby(lobby_name, (String) session.getAttribute("username"));
		if (!this.lobbyService.addLobby(newLobby)) {
			System.out.println("ADD LOBBY: false || info: " + newLobby);
			model.addAttribute("error", "Lobby with this name already exists");

			// Can we avoid to use this line? redundant with that in the "showLobbies"
			// method.
			model.addAttribute("lobbies", this.lobbyService.getLobbies());

			return "lobby";
		}
		System.out.println("ADD LOBBY: true || info: " + newLobby);
		return "redirect:/";
	}

	@GetMapping("search_lobby_by_name")
	public String searchLobbyByName(Model model, @RequestParam String lobby_name) {
		Lobby newLobby = this.lobbyService.getLobbyByName(lobby_name);
		System.out.println("Search lobby with name: " + lobby_name + ".");
		if (newLobby != null) {
			// lobby with name "lobby_name" founded
			System.out.println("Lobby found: " + newLobby);

			return "redirect:/";
		}
		// No lobby found with given name
		System.out.println("Lobby NOT found !!!");
		model.addAttribute("error_lobby_search", "Lobby with name " + lobby_name + " not found");
		return "lobby";
	}

	@GetMapping("search_lobby_by_username")
	public String searchLobbyByUsername(Model model, @RequestParam String user_name) {
		Lobby newLobby = this.lobbyService.getLobbyByUsername(user_name);
		System.out.println("Search lobby with owner: " + user_name + ".");
		if (newLobby != null) {
			// lobby with name "lobby_name" founded
			System.out.println("Lobby found: " + newLobby);

			return "redirect:/";
		}
		// No lobby found with given name
		System.out.println("Lobby NOT found !!!");
		model.addAttribute("error_lobby_search", "Lobby with owner " + user_name + " not found");
		return "lobby";
	}

	@PostMapping("join_lobby")
	@ResponseBody
	public void joinLobby(Model model, HttpSession session, @RequestParam String lobby_name) {
		String username = (String) session.getAttribute("username");
		this.lobbyService.joinToLobby(lobby_name, username);
		System.out.println("User: " + username + " join to Lobby: " + lobby_name);
	}

	@PostMapping("delete_lobby_by_name")
	@ResponseBody
	public void deleteLobbyByName(Model model, @RequestParam String lobby_name) {
		boolean deleted = this.lobbyService.removeLobbyByName(lobby_name);
		System.out.println("DELETED lobby with name: " + lobby_name + ".");
		if (deleted) {
			System.out.println("Lobby Successfully deleted: " + lobby_name);
			return;
		}
		System.out.println("Lobby NOT deleted !!!");
		model.addAttribute("error_lobby_delete", "Lobby with name " + lobby_name + " not deleted");
	}
}
