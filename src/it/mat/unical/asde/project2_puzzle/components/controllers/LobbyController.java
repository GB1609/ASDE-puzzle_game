package it.mat.unical.asde.project2_puzzle.components.controllers;

import java.util.concurrent.ForkJoinPool;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import it.mat.unical.asde.project2_puzzle.components.services.AccountService;
import it.mat.unical.asde.project2_puzzle.components.services.EventsService;
import it.mat.unical.asde.project2_puzzle.components.services.GameService;
import it.mat.unical.asde.project2_puzzle.components.services.LobbyService;
import it.mat.unical.asde.project2_puzzle.model.Lobby;
import it.mat.unical.asde.project2_puzzle.model.User;
import it.mat.unical.asde.project2_puzzle.model.services_utility.PlayerType;
import it.mat.unical.asde.project2_puzzle.model.services_utility.SearchBy;

@Controller
public class LobbyController {
	@Autowired
	LobbyService lobbyService;
	@Autowired
	EventsService eventService;
	@Autowired
	GameService gameService;

	@Autowired
	AccountService accountService;

	@GetMapping("lobby")
	public String showLobbies() {
		return "lobby";
	}

	@PostMapping("get_lobbies")
	@ResponseBody
	public String getLobbies(HttpSession session, @RequestParam int currently_showed) {
		String username = (String) session.getAttribute("username");
		JSONArray avatars = new JSONArray();
		for (Lobby lobby : this.lobbyService.getNextMLobbies(currently_showed, 20)) {
			String owner = lobby.getOwner();
			String guest = lobby.getGuest();
			if (owner != null) {
				if (owner != "") {
					User user = this.accountService.getUser(owner);
					System.out.println("OWNER:" + user);
					JSONObject userAvatar = new JSONObject();
					userAvatar.put("user", owner);
					userAvatar.put("avatar", user.getAvatar());
					avatars.put(userAvatar);
				}
			}
			if (guest != null) {
				if (guest != "") {
					User user = this.accountService.getUser(guest);
					System.out.println("GUEST:" + user);
					JSONObject userAvatar = new JSONObject();
					userAvatar.put("user", guest);
					userAvatar.put("avatar", user.getAvatar());
					avatars.put(userAvatar);
				}
			}
		}
		return new JSONObject().put("error", false)
				.put("lobbies_owner", this.lobbyService.getLobbiesBy(username, PlayerType.OWNER))
				.put("lobbies_guest", this.lobbyService.getLobbiesBy(username, PlayerType.GUEST))
				.put("lobbies", this.lobbyService.getNextMLobbies(currently_showed, 20)).put("username", username)
				.put("avatars", avatars).toString();
	}

	@PostMapping("join_lobby")
	@ResponseBody
	public String joinLobby(HttpSession session, @RequestParam String lobby_name) {
		String username = (String) session.getAttribute("username");
		// try to add the player to the lobby
		Integer lobbyID = this.lobbyService.joinToLobby(lobby_name, username);
		if (lobbyID == -1) {
			// if no lobby "lobby_name" is present then return an error
			return new JSONObject().put("error", true).toString();
		}
		session.setAttribute("gameId", lobbyID);
		try {
			// add the event of join that will be notified to lobby's owner
			this.eventService.addEventJoin(lobby_name, username);
			// if the joiner was connected to another lobby then add this event that will be
			// notified to the other player in lobby
			String previousJoined = this.lobbyService.checkPreviousLobby(username);
			if (previousJoined != null) {
				this.eventService.addEventLeaveJoin(previousJoined, username, false);
			}
		} catch (Exception e) {
			System.out.println("I can't join to lobby" + lobby_name);
		}
		System.out.println("User: " + username + " join to Lobby: " + lobby_name);
		this.eventService.attachListenerToStart(lobby_name, username);
		return new JSONObject().put("error", false).toString();
	}

	@PostMapping("create_lobby")
	@ResponseBody
	public String createLobby(HttpSession session, @RequestParam String lobby_name) {
		String username = (String) session.getAttribute("username");
		boolean added = false;
		if (added = this.lobbyService.addLobby(new Lobby(lobby_name, username), username)) {
			this.eventService.attachListenerToJoin(lobby_name, username);
			String previousJoined = this.lobbyService.checkPreviousLobby(username);
			if (previousJoined != null) {
				try {
					this.eventService.addEventLeaveJoin(previousJoined, username, false);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return new JSONObject().put("error", !added).toString();
	}

	@PostMapping("search_lobby")
	@ResponseBody
	public String searchLobby(HttpSession session, @RequestParam String search_txt, @RequestParam String search_by) {
		Lobby newLobby = this.lobbyService.getLobby(search_txt, SearchBy.valueOf(search_by));
		return new JSONObject().put("error", newLobby == null).put("lobby_searched", newLobby).toString();
	}

	@PostMapping("delete_lobby_by_name")
	@ResponseBody
	public String deleteLobbyByName(HttpSession session, @RequestParam String lobby_name) {
		boolean deleted = this.lobbyService.removeLobbyByName(lobby_name);
		if (deleted) {
			// TODO NOTIFY TO GUEST THAT THE LOBBY WAS DELETED
			// TODO create function for leave lobby (request by button)
//			this.eventService.detachListenerForStart(lobby_name, (String) session.getAttribute("username"));
		}

		return new JSONObject().put("error", !deleted).toString();
	}

	@PostMapping("check_join")
	@ResponseBody
	public DeferredResult<String> checkJoin(@RequestParam String lobby_name, HttpSession session) {
		String username = (String) session.getAttribute("username");
		DeferredResult<String> joins = new DeferredResult<>();
		ForkJoinPool.commonPool().submit(() -> {
			try {
				String result;
				joins.setResult((result = this.eventService.getEventJoin(lobby_name, username)));
				lobbyService.cleanIfOffline(result, lobby_name, false);
			} catch (InterruptedException e) {
				joins.setResult(null);
			}
		});
		return joins;
	}

	@PostMapping("check_start")
	@ResponseBody
	public DeferredResult<String> checkStart(@RequestParam String lobby_name, HttpSession session) {
		String username = (String) session.getAttribute("username");
		DeferredResult<String> joins = new DeferredResult<>();
		ForkJoinPool.commonPool().submit(() -> {
			try {
				String result;
				joins.setResult((result = this.eventService.getEventStartGame(lobby_name, username)));
				lobbyService.cleanIfOffline(result, lobby_name, true);
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
		gameService.initNewGame(lobbyID, lobby_name);
		// session.setAttribute("player", "player1");
		this.eventService.detachListenerForJoin(lobby_name, (String) session.getAttribute("username"));
		try {
			this.eventService.addEventStartGame(lobby_name);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:game";
	}

	@PostMapping("check_is_listening_for")
	@ResponseBody
	public String checkIsListeningFor(HttpSession session) {
		return eventService.getListenerOfUser((String) session.getAttribute("username"));
	}
}
