package it.mat.unical.asde.project2_puzzle.components.controllers;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.mat.unical.asde.project2_puzzle.components.services.LobbyService;
import it.mat.unical.asde.project2_puzzle.components.services.SearchBy;
import it.mat.unical.asde.project2_puzzle.model.Lobby;

@Controller
public class LobbyController {
	@Autowired
	LobbyService lobbyService;

	@PostMapping("create_lobby")
	@ResponseBody
	public String createLobby(HttpSession session, @RequestParam String lobby_name) {
		String username = (String) session.getAttribute("username");
		JSONObject result = new JSONObject().put("error", false).put("usr", username);
		Lobby newLobby = new Lobby(lobby_name, username);
		if (this.lobbyService.addLobby(newLobby)) {
			System.out.println("ADD LOBBY: true || info: " + newLobby);
			// Can we avoid to use this line? redundant with that in the "showLobbies"
			// method.
			return result.put("new_lobby", newLobby.toString()).toString();
		}
		System.out.println("ADD LOBBY: false || info: " + newLobby);
		return result.put("error", true).put("err_msg", "Lobby with name " + lobby_name + " already exists").toString();
	}

//	@PostMapping("delete_lobby_by_name")
//	@ResponseBody
//	public String deleteLobbyByName(Model model, @RequestParam String lobby_name) {
//		JSONObject result = new JSONObject().put("error", false);
//		boolean deleted = this.lobbyService.removeLobbyByName(lobby_name);
//		System.out.println("DELETED lobby with name: " + lobby_name + ".");
//		if (deleted) {
//			System.out.println("Lobby Successfully deleted: " + lobby_name);
//			return result.toString();
//		}
//		System.out.println("Lobby NOT deleted !!!");
//		return result.put("error", true)
//				.put("err_msg", "Lobby with name " + lobby_name + " not deleted! An error occured on the server")
//				.toString();
//	}

	@PostMapping("join_lobby")
	@ResponseBody
	public String joinLobby(HttpSession session, @RequestParam String lobby_name) {
		String username = (String) session.getAttribute("username");
		JSONObject result = new JSONObject().put("error", false).put("usr", username);
		this.lobbyService.joinToLobby(lobby_name, username);
		result.put("lobbies", new JSONArray(this.lobbyService.getLobbies()));
		System.out.println(
				"User: " + username + " join to Lobby: " + this.lobbyService.getLobby(lobby_name, SearchBy.LOBBY_NAME));
		return result.toString();
	}

	@PostMapping("refresh_lobbies")
	@ResponseBody
	public String joinLobby(HttpSession session) {
		String username = (String) session.getAttribute("username");
		JSONObject result = new JSONObject().put("error", false).put("usr", username);
		result.put("lobbies", new JSONArray(this.lobbyService.getLobbies()));
		System.out.println("User: " + username + " Refresh Lobby");
		return result.toString();
	}

	@PostMapping("search_lobby")
	@ResponseBody
	public String searchLobby(HttpSession session, @RequestParam String search_txt, @RequestParam String search_by) {
		String username = (String) session.getAttribute("username");
		JSONObject result = new JSONObject().put("error", false).put("usr", username);
		Lobby newLobby = this.lobbyService.getLobby(search_txt, SearchBy.valueOf(search_by));
		System.out.println("Search lobby by " + search_by + ": " + search_txt + ".");
		if (newLobby != null) {
			System.out.println("Lobby found: " + newLobby);
			return result.put("lobby_searched", newLobby.toString()).toString();
		}
		System.out.println("Lobby NOT found !!!");
		return result.put("error", true).put("err_msg", "Lobby with " + search_by + ": " + search_txt + " not found")
				.toString();
	}

	@GetMapping("lobby")
	public String showLobbies(Model model) {
		model.addAttribute("lobbies", this.lobbyService.getLobbies());
		return "lobby";
	}
}
/*
 * TODO:
 *
 * -fix modal create lobby
 *
 */
