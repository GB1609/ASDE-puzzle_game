package it.mat.unical.asde.project2_puzzle.components.controllers;

import java.util.concurrent.ForkJoinPool;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import it.mat.unical.asde.project2_puzzle.components.services.AccountService;
import it.mat.unical.asde.project2_puzzle.components.services.EventsServiceForLobby;
import it.mat.unical.asde.project2_puzzle.components.services.GameService;
import it.mat.unical.asde.project2_puzzle.components.services.LobbyService;
import it.mat.unical.asde.project2_puzzle.components.services.utility.MessageMaker;
import it.mat.unical.asde.project2_puzzle.model.Lobby;
import it.mat.unical.asde.project2_puzzle.model.services_utility.SearchBy;

@Controller
public class LobbyController {
	@Autowired
	LobbyService lobbyService;
	@Autowired
	EventsServiceForLobby eventService;
	@Autowired
	GameService gameService;
	@Autowired
	MessageMaker messageMaker;

	@Autowired
	AccountService accountService;

	@GetMapping("lobby")
	public String showLobbies() {
		return "lobby";
	}

	@GetMapping("get_lobbies")
	@ResponseBody
	public String getLobbies(HttpSession session, @RequestParam String lobbies, @RequestParam int currently_showed) {
		return this.lobbyService.getLobbiesOrRefresh(session.getAttribute("username").toString(), lobbies,
				currently_showed);
	}

	@PostMapping("join_lobby")
	@ResponseBody
	public String joinLobby(HttpSession session, @RequestParam String lobby_name) {
		String username = (String) session.getAttribute("username");
		// try to add the player to the lobby
		Integer lobbyID = this.lobbyService.joinToLobby(lobby_name, username);
		if (lobbyID == -1) {
			// if no lobby "lobby_name" is present then return an error
			return messageMaker.makeMessage(MessageMaker.ERROR);
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
			return messageMaker.makeMessage(MessageMaker.ERROR);
		}
		this.eventService.attachListenerToStart(lobby_name, username);
		return messageMaker.makeMessage(MessageMaker.ERROR, false);
	}

	/*
	 * This method is called when a player wants to join a lobby
	 */
	@PostMapping("create_lobby")
	@ResponseBody
	public String createLobby(HttpSession session, @RequestParam String lobby_name) {
		String username = (String) session.getAttribute("username");
		boolean added = false;
		if (added = this.lobbyService.addLobby(new Lobby(lobby_name, username), username)) {
			this.eventService.attachListenerToJoin(lobby_name, username);
			// if the player was joined to a lobby then notify the event to other
			// components.
			String previousJoined = this.lobbyService.checkPreviousLobby(username);
			if (previousJoined != null) {
				try {
					this.eventService.addEventLeaveJoin(previousJoined, username, false);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return messageMaker.makeMessage(MessageMaker.ERROR);
				}
			}
		}
		return messageMaker.makeMessage(MessageMaker.ERROR, !added);
	}

	@PostMapping("search_lobby")
	@ResponseBody
	public String searchLobby(HttpSession session, @RequestParam String search_txt, @RequestParam String search_by) {
		Lobby newLobby = this.lobbyService.getLobby(search_txt, SearchBy.valueOf(search_by));
		return new JSONObject().put("error", newLobby == null).put("lobby_searched", newLobby).toString();
	}

	/*
	 * This method is called when a player creates a lobby to check when a player
	 * join to it
	 */
	@PostMapping("check_join")
	@ResponseBody
	public DeferredResult<String> checkJoin(@RequestParam String lobby_name, HttpSession session) {
		String username = (String) session.getAttribute("username");
		DeferredResult<String> joins = new DeferredResult<>();
		ForkJoinPool.commonPool().submit(() -> {
			try {
				String result;
				joins.setResult((result = this.eventService.getEventJoin(lobby_name, username)));
				lobbyService.cleanIfOffline(result, lobby_name);
			} catch (InterruptedException e) {
				joins.setResult(null);
			}
		});
		return joins;
	}

	/*
	 * This method is called when a player joins to a lobby to check when the owner
	 * start the game
	 */
	@PostMapping("check_start")
	@ResponseBody
	public DeferredResult<String> checkStart(@RequestParam String lobby_name, HttpSession session) {
		String username = (String) session.getAttribute("username");
		DeferredResult<String> joins = new DeferredResult<>();
		ForkJoinPool.commonPool().submit(() -> {
			try {
				String result;
				joins.setResult((result = this.eventService.getEventStartGame(lobby_name, username)));
				lobbyService.cleanIfOffline(result, lobby_name);
			} catch (InterruptedException e) {
				joins.setResult(null);
			}
		});
		return joins;

	}

	/*
	 * This method is called when a player starts the game in order to initialize
	 * all the needful structures
	 */
	@PostMapping("forward_to_game")
	public String forwardToGame(@RequestParam String lobby_name, HttpSession session) {
		Integer lobbyID = this.lobbyService.destructLobby(lobby_name);
		if (lobbyID.equals(-1)) {
			throw new RuntimeException("no lobby found");
		}
		session.setAttribute("gameId", lobbyID);
		// init the new game
		gameService.initNewGame(lobbyID, lobby_name);
		this.eventService.detachListenerForJoin(lobby_name, (String) session.getAttribute("username"));
		try {
			// add the event of start game for the other components in lobby
			this.eventService.addEventStartGame(lobby_name);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// then redirect it to the game
		return "redirect:game";
	}

	/*
	 * This method is called when a player receives the event of start. It detaches
	 * the correspondent listener and redirect the player to the game.
	 */
	@GetMapping("joiner_to_game")
	public String forwardJoinerToGame(HttpSession session) {
		eventService.detachListenerForStart((String) session.getAttribute("username"), 2);
		return "redirect:game";
	}

	/*
	 * This method is called to check if a player is listening for an event. (useful
	 * in page reload, avoid the lose of listener)
	 */
	@PostMapping("check_is_listening_for")
	@ResponseBody
	public String checkIsListeningFor(HttpSession session) {
		return eventService.getListenerOfUser((String) session.getAttribute("username"));
	}

	/*
	 * This method is called when a player leave the lobby.
	 * 
	 * Delete the user in the correspondent lobby and notify the event to other
	 * components.
	 */
	@PostMapping("leave_lobby")
	@ResponseBody
	public String leaveLobby(HttpSession session, @RequestParam String lobby_name) {
		String username = (String) session.getAttribute("username");
		System.out.println("User: " + username + " leave Lobby: " + lobby_name);
		boolean leaved = this.lobbyService.leaveLobby(username, lobby_name);
		if (leaved) {
			String previousJoined = this.lobbyService.checkPreviousLobby(username);
			if (previousJoined != null) {
				try {
					this.eventService.addEventLeaveJoin(previousJoined, username, false);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return messageMaker.makeMessage(MessageMaker.ERROR);

				}
			}
		}
		return messageMaker.makeMessage(MessageMaker.ERROR, !leaved);
	}
}
