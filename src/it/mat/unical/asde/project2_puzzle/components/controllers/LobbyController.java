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
import it.mat.unical.asde.project2_puzzle.components.services.PlayerType;
import it.mat.unical.asde.project2_puzzle.components.services.SearchBy;
import it.mat.unical.asde.project2_puzzle.model.Lobby;

@Controller
public class LobbyController {

	@Autowired
	LobbyService lobbyService;

	@GetMapping("lobby")
	public String showLobbies(Model model) {
		return "lobby";
	}

	private JSONObject initJSOONResponse(String username) {
		return new JSONObject().put("error", false).put("lobbies", new JSONArray(this.lobbyService.getNextMLobbies(20)))
				.put("lobbies_owner", new JSONArray(this.lobbyService.getLobbiesBy(username, PlayerType.OWNER)))
				.put("lobbies_guest", new JSONArray(this.lobbyService.getLobbiesBy(username, PlayerType.GUEST)));
	}

	@PostMapping("get_lobbies")
	@ResponseBody
	public String getLobbies(HttpSession session, @RequestParam boolean reset_counter) {
		if (reset_counter) {
			this.lobbyService.resetCurrentlyShowed();
		}
		String username = (String) session.getAttribute("username");
		return this.initJSOONResponse(username).put("username", username).toString();
	}

	@PostMapping("join_lobby")
	@ResponseBody
	public String joinLobby(HttpSession session, @RequestParam String lobby_name) {
		String username = (String) session.getAttribute("username");
		return this.initJSOONResponse(username).put("error", !this.lobbyService.joinToLobby(lobby_name, username))
				.toString();
	}

	@PostMapping("create_lobby")
	@ResponseBody
	public String createLobby(HttpSession session, @RequestParam String lobby_name) {
		String username = (String) session.getAttribute("username");
		return this.initJSOONResponse(username)
				.put("error", !this.lobbyService.addLobby(new Lobby(lobby_name, username))).toString();
	}

	@PostMapping("search_lobby")
	@ResponseBody
	public String searchLobby(HttpSession session, @RequestParam String search_txt, @RequestParam String search_by) {
		Lobby newLobby = this.lobbyService.getLobby(search_txt, SearchBy.valueOf(search_by));
		return this.initJSOONResponse((String) session.getAttribute("username")).put("error", newLobby == null)
				.put("lobby_searched", new JSONObject(newLobby)).toString();
	}

	@PostMapping("delete_lobby_by_name")
	@ResponseBody
	public String deleteLobbyByName(HttpSession session, @RequestParam String lobby_name) {
		return this.initJSOONResponse((String) session.getAttribute("username"))
				.put("error", !this.lobbyService.removeLobbyByName(lobby_name)).toString();
	}

}
