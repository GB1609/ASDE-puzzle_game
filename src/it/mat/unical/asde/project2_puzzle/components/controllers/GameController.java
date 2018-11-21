package it.mat.unical.asde.project2_puzzle.components.controllers;

import java.util.concurrent.ForkJoinPool;

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

	@GetMapping("game")
	public String goToGame(Model m) {
		m.addAttribute("randomGrid", gameService.getRandomGrid());
		return "Game";
	}

	@PostMapping("move_piece")
	@ResponseBody
	public void movePiece(@RequestParam String old_location, @RequestParam int old_position,
			@RequestParam String new_location, @RequestParam int new_position, @RequestParam String piece) {
		gameService.updateStateGame(old_location, old_position, new_location, new_position, piece);
		try {
			eventsService.addEvent(1);
		} catch (InterruptedException e) {
			System.out.println("non riesco ad aggiungere");
			e.printStackTrace();
		}
	}

	@PostMapping("get_progress")
	@ResponseBody
	public DeferredResult<String> getEvents(@RequestParam int gameId) {
//		try {
//			return eventsService.nextEvent(gameId);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;

		DeferredResult<String> output = new DeferredResult<>();
		ForkJoinPool.commonPool().submit(() -> {
			try {
				output.setResult(eventsService.nextEvent(gameId));
			} catch (InterruptedException e) {
				output.setResult("An error occurred during event retrieval");
			}
		});

		return output;
	}

}
