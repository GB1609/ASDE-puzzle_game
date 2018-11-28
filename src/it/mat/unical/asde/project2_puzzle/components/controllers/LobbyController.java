package it.mat.unical.asde.project2_puzzle.components.controllers;

import java.util.concurrent.ForkJoinPool;

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
import org.springframework.web.context.request.async.DeferredResult;

import it.mat.unical.asde.project2_puzzle.components.services.EventsService;
import it.mat.unical.asde.project2_puzzle.components.services.LobbyService;
import it.mat.unical.asde.project2_puzzle.components.services.PlayerType;
import it.mat.unical.asde.project2_puzzle.components.services.SearchBy;
import it.mat.unical.asde.project2_puzzle.model.Lobby;

@Controller
public class LobbyController {
	@Autowired
	LobbyService lobbyService;
	@Autowired
	EventsService eventService;

	@GetMapping("lobby")
	public String showLobbies(Model model) {
		return "lobby";
	}

	private JSONObject initJSOONResponse(String username) {
		return new JSONObject().put("error", false)
				.put("lobbies_owner", new JSONArray(this.lobbyService.getLobbiesBy(username, PlayerType.OWNER)))
				.put("lobbies_guest", new JSONArray(this.lobbyService.getLobbiesBy(username, PlayerType.GUEST)));
	}

	@PostMapping("get_lobbies")
	@ResponseBody
	public String getLobbies(HttpSession session, @RequestParam int currently_showed) {
		String username = (String) session.getAttribute("username");
		return this.initJSOONResponse(username)
				.put("lobbies", new JSONArray(this.lobbyService.getNextMLobbies(currently_showed, 20)))
				.put("username", username).toString();
	}

	@PostMapping("join_lobby")
	@ResponseBody
	public String joinLobby(HttpSession session, @RequestParam String lobby_name) {
		String username = (String) session.getAttribute("username");
		Integer lobbyID = this.lobbyService.joinToLobby(lobby_name, username);
		if (lobbyID == -1) {
			return this.initJSOONResponse(username).put("error", true).toString();
		}
		session.setAttribute("gameId", lobbyID);
		session.setAttribute("player", "player2");
		try {
			this.eventService.addEventJoin(lobby_name);
			String previousJoined = this.lobbyService.checkPreviousLobby(username);
			if (previousJoined != null) {
				this.eventService.addEventLeaveJoin(previousJoined);
			}
		} catch (Exception e) {
			System.out.println("I can't join to lobby" + lobby_name);
		}
		System.out.println("User: " + username + " join to Lobby: " + lobby_name);
		this.eventService.attachListenerToStart(lobby_name);
		return this.initJSOONResponse(username).put("error", false).toString();
	}

	@PostMapping("create_lobby")
	@ResponseBody
	public String createLobby(HttpSession session, @RequestParam String lobby_name) {
		String username = (String) session.getAttribute("username");
		boolean added = false;
		if ((added = this.lobbyService.addLobby(new Lobby(lobby_name, username), username)))
			eventService.attachListenerToStart(lobby_name);
		return this.initJSOONResponse(username).put("error", !added).toString();
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
		boolean deleted = this.lobbyService.removeLobbyByName(lobby_name);
		if (deleted) {
			eventService.detachListenerForJoin(lobby_name);
		}

		return this.initJSOONResponse((String) session.getAttribute("username")).put("error", !deleted).toString();
	}

	@PostMapping("check_join")
	@ResponseBody
	public DeferredResult<String> checkJoin(@RequestParam String lobby_name) {
		DeferredResult<String> joins = new DeferredResult<>();
		ForkJoinPool.commonPool().submit(() -> {
			try {
				joins.setResult(this.eventService.getEventJoin(lobby_name));
			} catch (InterruptedException e) {
				joins.setResult(null);
			}
		});
		return joins;
	}

	@PostMapping("check_start")
	@ResponseBody
	public DeferredResult<String> checkStart(@RequestParam String lobby_name) {
		DeferredResult<String> joins = new DeferredResult<>();
		ForkJoinPool.commonPool().submit(() -> {
			try {
				joins.setResult(this.eventService.getEventStartGame(lobby_name));
			} catch (InterruptedException e) {
				joins.setResult(null);
			}
		});
		return joins;
	}

	@PostMapping("forward_to_game")
	public String forwardToGame(@RequestParam String lobby_name, HttpSession session) {
		Integer lobbyID = this.lobbyService.destrucLobby(lobby_name);
		if (lobbyID.equals(-1)) {
			throw new RuntimeException("no lobby found");
		}
		session.setAttribute("gameId", lobbyID);
		session.setAttribute("player", "player1");
		this.eventService.detachListenerForJoin(lobby_name);
		try {
			this.eventService.addEventStartGame(lobby_name);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:game";
	}
}
