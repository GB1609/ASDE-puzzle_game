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
		System.out.println("IN GAME" + session.getAttribute("player"));
		m.addAttribute("randomGrid", gameService.initNewGame((Integer) session.getAttribute("gameId")));
		System.out.println("IN Game dopo" + session.getAttribute("player"));
		return "Game";
	}

	@PostMapping("move_piece")
	@ResponseBody
	public void movePiece(@RequestParam String old_location, @RequestParam int old_position,
			@RequestParam String new_location, @RequestParam int new_position, @RequestParam String piece,
			HttpSession session) {

		Integer gameId = (Integer) session.getAttribute("gameId");
		String player = (String) session.getAttribute("player");

		try {
			if (gameService.updateStateGame(gameId, player, old_location, old_position, new_location, new_position,
					piece))
				eventsService.addEventEndGame(gameId);
			else {
				String progress = gameService.getProgressFor(gameId, player);
				eventsService.addEventFor(gameId, player, progress);
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
		String player = (String) session.getAttribute("player");
		DeferredResult<String> outputProgress = new DeferredResult<>();
		ForkJoinPool.commonPool().submit(() -> {
			try {
				outputProgress.setResult(eventsService.nextEventProgress(gameId, player));
			} catch (InterruptedException e) {
				outputProgress.setResult(-1000 + "");
			}
		});

		return outputProgress;
	}

	@GetMapping("end_game")
	public String goToEndGame() {
		return "EndGame";
	}

	@GetMapping("leave_game")
	@ResponseBody
	public void leaveGame(HttpSession session) {
		Integer gameId = (Integer) session.getAttribute("gameId");
		String player = (String) session.getAttribute("player");
		try {
			eventsService.addEventLeaveGameBy(gameId, player);
		} catch (InterruptedException e) {
			System.out.println("non riesco ad aggiungere");
			e.printStackTrace();
		}
	}

}
