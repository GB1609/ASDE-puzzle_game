package it.mat.unical.asde.project2_puzzle.components.controllers;

import java.util.concurrent.ForkJoinPool;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import it.mat.unical.asde.project2_puzzle.components.services.EventsService;
import it.mat.unical.asde.project2_puzzle.components.services.GameService;

@Controller
public class GameController {
	@Autowired
	GameService gameService;
	@Autowired
	EventsService eventsService;

	@GetMapping("/game")
	public String goToGame(Model m, HttpSession session) {
//		System.out.println("IN GAME" + session.getAttribute("player"));
		Integer gameId = (Integer) session.getAttribute("gameId");
		m.addAttribute("randomGrid", gameService.initNewGame(gameId, (String) session.getAttribute("username")));
		session.setAttribute("player", gameService.getCurrentPlayer(gameId));
		System.out.println("IN Game dopo" + session.getAttribute("player"));
		return "Game";
	}

	@GetMapping("endgame")
	public String getToEndGameTest() {
		return "EndGame";
	}

	@PostMapping("move_piece")
	@ResponseBody
	public void movePiece(@RequestParam String old_location, @RequestParam int old_position,
			@RequestParam String new_location, @RequestParam int new_position, @RequestParam String piece,
			@RequestParam String timer, HttpSession session) {
		// System.out.println(timer);
		Integer gameId = (Integer) session.getAttribute("gameId");
		Integer player = (Integer) session.getAttribute("player");
		try {
			if (gameService.updateStateGame(gameId, player, old_location, old_position, new_location, new_position,
					piece, timer)) {
				gameService.storeMatch(gameId);
				eventsService.addEventEndGame(gameId, gameService.getCurrentPlayer(gameId));
			} else {
				String progress = gameService.getProgressFor(gameId, player);
				eventsService.addEventFor(gameId, player, gameService.getCurrentPlayer(gameId), progress);
			}
		} catch (InterruptedException e) {
			System.out.println("non riesco ad aggiungere");
			e.printStackTrace();
		}
	}

	@PostMapping("get_progress")
	@ResponseBody
	public DeferredResult<String> getEvents(HttpSession session) {
		Integer gameId = (Integer) session.getAttribute("gameId");
		Integer player = (Integer) session.getAttribute("player");
		DeferredResult<String> outputProgress = new DeferredResult<>();
		ForkJoinPool.commonPool().submit(() -> {
			try {
				outputProgress.setResult(eventsService.nextGameEventFor(gameId, player));
			} catch (InterruptedException e) {
				outputProgress.setResult(-1000 + "");
			}
		});
		return outputProgress;
	}

	@GetMapping("end_game")
	public String goToEndGame(Model m, HttpSession session) {
		Integer gameId = (Integer) session.getAttribute("gameId");
		Integer player = (Integer) session.getAttribute("player");
		session.removeAttribute("gameId");
		session.removeAttribute("player");
		eventsService.detachListenerInGame(gameId, player);
		m.addAttribute("match", gameService.getMatch(gameId));
		return "EndGame";
	}

	@GetMapping("leave_game")
	@ResponseBody
	public void leaveGame(HttpSession session) {
		Integer gameId = (Integer) session.getAttribute("gameId");
		Integer player = (Integer) session.getAttribute("player");
		session.removeAttribute("gameId");
		session.removeAttribute("player");
		try {
			eventsService.addEventLeaveGameBy(gameId, player, gameService.getCurrentPlayer(gameId));
			eventsService.detachListenerInGame(gameId, player);
		} catch (InterruptedException e) {
			System.out.println("non riesco ad aggiungere");
			e.printStackTrace();
		}
	}

	@PostMapping("send_message")
	@ResponseBody
	public void sendMessage(@RequestParam String message, HttpSession session) {
		Integer gameId = (Integer) session.getAttribute("gameId");
		Integer player = (Integer) session.getAttribute("player");
		try {
			eventsService.addMessageFor(gameId, player, gameService.getCurrentPlayer(gameId), message);
		} catch (InterruptedException e) {
			System.out.println("non riesco ad aggiungere");
			e.printStackTrace();
		}
	}
}
