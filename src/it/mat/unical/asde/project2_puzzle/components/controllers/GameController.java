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

import it.mat.unical.asde.project2_puzzle.components.services.EventsServiceForGame;
import it.mat.unical.asde.project2_puzzle.components.services.GameService;
import it.mat.unical.asde.project2_puzzle.components.services.utility.MessageMaker;

@Controller
public class GameController {
	@Autowired
	GameService gameService;
	@Autowired
	EventsServiceForGame eventsServiceForGame;

	/**
	 * this method allow to the players to start to play.
	 */
	@GetMapping("/game")
	public String goToGame(Model model, HttpSession session) {
		String username = (String) session.getAttribute("username");
		Integer gameId = (Integer) session.getAttribute("gameId");

		// if no game id is present in the session then the player can't play and it is
		// redirect to home
		if (gameId == null)
			return "redirect:lobby";
		// set in the model the initial disposition of the puzzle pieces
		model.addAttribute("randomGrid", gameService.getGameForPlayer(gameId, username));
		// in order to identify the player in game updates an integer is assigned in its
		// session
		Integer currentPlayer = gameService.getCurrentPlayer(gameId);
		session.setAttribute("player", currentPlayer);
		return "Game";
	}

	/**
	 * Make the updates in game logic when a player moves a piece
	 * 
	 * @param old_location the grid in which the piece was located
	 * @param old_position the position in which the piece was located
	 * @param new_location the grid in which the piece will be located
	 * @param new_position the position in which the piece will be located
	 * @param piece        the identifier of the piece
	 * @param timer        the time since the start of the game
	 * @param session
	 */
	@PostMapping("move_piece")
	@ResponseBody
	public String movePiece(@RequestParam String old_location, @RequestParam int old_position,
			@RequestParam String new_location, @RequestParam int new_position, @RequestParam String piece,
			@RequestParam String timer, HttpSession session) {
		Integer gameId = (Integer) session.getAttribute("gameId");
		Integer player = (Integer) session.getAttribute("player");
		try {
			// if the player has completed the puzzle, the match is stored. Otherwise, his
			// progress will be notified to the opponents
			if (gameService.updateStateGame(gameId, player, old_location, old_position, new_location, new_position,
					piece, timer)) {
				gameService.storeMatch(gameId);
				eventsServiceForGame.addEventEndGame(gameId, gameService.getPlayerInGame(gameId));
			} else {
				String progress = gameService.getProgressFor(gameId, player);
				eventsServiceForGame.addEventFor(gameId, player, gameService.getPlayerInGame(gameId), progress);
			}
		} catch (InterruptedException e) {
			System.out.println("non riesco ad aggiungere");
			e.printStackTrace();
			return MessageMaker.ERROR;
		}
		return MessageMaker.SUCCESS;
	}

	@PostMapping("get_progress")
	@ResponseBody
	public DeferredResult<String> getEvents(HttpSession session) {
		Integer gameId = (Integer) session.getAttribute("gameId");
		Integer player = (Integer) session.getAttribute("player");
		DeferredResult<String> outputProgress = new DeferredResult<>();
		ForkJoinPool.commonPool().submit(() -> {
			try {
				outputProgress.setResult(
						eventsServiceForGame.nextGameEventFor(gameId, player, gameService.getPlayerInGame(gameId)));
			} catch (InterruptedException e) {
				outputProgress.setResult(-1000 + "");
			}
		});
		return outputProgress;
	}

	/**
	 * This method is called when the game ends
	 */
	@GetMapping("end_game")
	public String goToEndGame(Model m, HttpSession session, Integer offline) {
		Integer gameId = (Integer) session.getAttribute("gameId");
		Integer player = (Integer) session.getAttribute("player");
		session.removeAttribute("gameId");
		session.removeAttribute("player");
		// if the game end because the opponent closed it then the match is stored and
		// the remaining player wins
		if (offline != null) {
			gameService.leaveGameBy(gameId, offline);
			gameService.storeMatch(gameId);
		}
		eventsServiceForGame.detachListenerInGame(gameId, player);
		m.addAttribute("match", gameService.getMatch(gameId));
		return "EndGame";
	}

	/**
	 * This method is called when the player leaves the game
	 */
	@GetMapping("leave_game")
	@ResponseBody
	public void leaveGame(HttpSession session) {
		Integer gameId = (Integer) session.getAttribute("gameId");
		Integer player = (Integer) session.getAttribute("player");
		// remove the game attribute from the session
		session.removeAttribute("gameId");
		session.removeAttribute("player");

		// if the session has not been invalidated, store the match and notify the event
		// to the opponents
		if (gameId != null && player != null) {
			System.out.println("LEAVE BY" + player);
			gameService.leaveGameBy(gameId, player);
			gameService.storeMatch(gameId);
			try {
				eventsServiceForGame.addEventLeaveGameBy(gameId, player, gameService.getPlayerInGame(gameId), false);
				eventsServiceForGame.detachListenerInGame(gameId, player);
			} catch (InterruptedException e) {
				System.out.println("non riesco ad aggiungere");
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method is called when the player send a message in chat
	 */
	@PostMapping("send_message")
	@ResponseBody
	public String sendMessage(@RequestParam String message, HttpSession session) {
		Integer gameId = (Integer) session.getAttribute("gameId");
		Integer player = (Integer) session.getAttribute("player");
		try {
			// send the message to the other players
			eventsServiceForGame.addMessageFor(gameId, player, gameService.getPlayerInGame(gameId), message);
		} catch (InterruptedException e) {
			System.out.println("non riesco ad aggiungere");
			return MessageMaker.ERROR;
		}
		return MessageMaker.SUCCESS;
	}
}
