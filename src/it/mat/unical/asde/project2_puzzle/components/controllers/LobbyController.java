package it.mat.unical.asde.project2_puzzle.components.controllers;

import java.util.concurrent.ForkJoinPool;

import javax.servlet.http.HttpSession;

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
import it.mat.unical.asde.project2_puzzle.components.services.SearchBy;
import it.mat.unical.asde.project2_puzzle.model.Lobby;

@Controller
public class LobbyController {

	@Autowired
	LobbyService lobbyService;

	@Autowired
	EventsService eventService;

	@PostMapping("create_lobby")
	@ResponseBody
	public String createLobby(Model model, HttpSession session, @RequestParam String lobby_name) {
		JSONObject result = new JSONObject().put("error", false);
		Lobby newLobby = new Lobby(lobby_name, (String) session.getAttribute("username"));// TODO move this creation in
																							// lobby service
		if (this.lobbyService.addLobby(newLobby)) {
			eventService.attachListenerToJoin(lobby_name);
//			session.setAttribute("lobby_created", lobby_name);
			System.out.println("ADD LOBBY: true || info: " + newLobby);
			// Can we avoid to use this line? redundant with that in the "showLobbies"
			// method.
			return result.toString();
		}
		System.out.println("ADD LOBBY: false || info: " + newLobby);
		return result.put("error", true).put("err_msg", "Lobby with name " + lobby_name + " already exists").toString();
	}

	// TODO DETACH LISTENER FOR LOBBY DELETED

	@PostMapping("delete_lobby_by_name")
	@ResponseBody
	public String deleteLobbyByName(Model model, @RequestParam String lobby_name) {
		JSONObject result = new JSONObject().put("error", false);
		boolean deleted = this.lobbyService.removeLobbyByName(lobby_name);
		System.out.println("DELETED lobby with name: " + lobby_name + ".");
		if (deleted) {
			eventService.detachListenerForJoin(lobby_name);
			System.out.println("Lobby Successfully deleted: " + lobby_name);
			return result.toString();
		}
		System.out.println("Lobby NOT deleted !!!");
		return result.put("error", true)
				.put("err_msg", "Lobby with name " + lobby_name + " not deleted! An error occured on the server")
				.toString();
	}

	@PostMapping("join_lobby")
	@ResponseBody
	public String joinLobby(Model model, HttpSession session, @RequestParam String lobby_name) {
		JSONObject result = new JSONObject();
		String username = (String) session.getAttribute("username");
		Integer lobbyID = this.lobbyService.joinToLobby(lobby_name, username);
		if (lobbyID == -1) {
			result.put("error", true);
			return result.toString();
		}
		session.setAttribute("gameId", lobbyID);
		session.setAttribute("player", "player2");
		try {
			this.eventService.addEventJoin(lobby_name);
		} catch (Exception e) {

			System.out.println("I can't join to lobby" + lobby_name);
		}
		System.out.println("User: " + username + " join to Lobby: " + lobby_name);

		eventService.attachListenerToStart(lobby_name);
		result.put("error", false);
		return result.toString();
	}

	@PostMapping("search_lobby")
	@ResponseBody
	public String searchLobby(Model model, @RequestParam String search_txt, @RequestParam String search_by) {
		JSONObject result = new JSONObject().put("error", false);
		Lobby newLobby = this.lobbyService.getLobby(search_txt, SearchBy.valueOf(search_by));
		System.out.println("Search lobby by " + search_by + ": " + search_txt + ".");
		if (newLobby != null) {
			// lobby with name "lobby_name" founded
			System.out.println("Lobby found: " + newLobby);
			return result.toString();
		}
		System.out.println("Lobby NOT found !!!");
		return result.put("error", true).put("err_msg", "Lobby with owner " + search_txt + " not found").toString();
	}

	@GetMapping("lobby")
	public String showLobbies(Model model) {
		model.addAttribute("lobbies", this.lobbyService.getLobbies());
		return "lobby";
	}

	@PostMapping("check_join")
	@ResponseBody
	public DeferredResult<String> checkJoin(@RequestParam String lobby_name) {
		DeferredResult<String> joins = new DeferredResult<>();
		ForkJoinPool.commonPool().submit(() -> {
			try {
				joins.setResult(eventService.getEventJoin(lobby_name));
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
				joins.setResult(eventService.getEventStartGame(lobby_name));
			} catch (InterruptedException e) {
				joins.setResult(null);
			}
		});

		return joins;
	}

	@PostMapping("forward_to_game")
	public String forwardToGame(@RequestParam String lobby_name, HttpSession session) {

		Integer lobbyID = lobbyService.destrucLobby(lobby_name);
		if (lobbyID.equals(-1))
			throw new RuntimeException("no lobby found");

		session.setAttribute("gameId", lobbyID);
		session.setAttribute("player", "player1");
		eventService.detachListenerForJoin(lobby_name);
		try {
			eventService.addEventStartGame(lobby_name);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:game";
	}
}

/*
 * TODO:
 *
 * -fix search of lobby by user and name
 *
 * -fix create and search lobby methods. must update only lobbies_div with ajax
 *
 * TODO:
 *
 * -fix modal create lobby
 *
 */
