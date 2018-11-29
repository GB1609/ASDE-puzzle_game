package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import it.mat.unical.asde.project2_puzzle.model.Grid;
import it.mat.unical.asde.project2_puzzle.model.services_utility.RunningGame;

@Service
public class GameService {
	HashMap<Integer, RunningGame> runningGames = new HashMap<>();

	synchronized public Grid initNewGame(Integer gameId) {
		if (!runningGames.containsKey(gameId)) {
			runningGames.put(gameId, new RunningGame(0));
			System.out.println("init");
		}
		RunningGame rgGame = runningGames.get(gameId);
		rgGame.addPlayer();
		return rgGame.getInitialGrid(gameId);

	}

	public boolean updateStateGame(Integer gameId, Integer player, String old_location, int old_position,
			String new_location, int new_position, String piece) {
		return runningGames.get(gameId).updateSateGame(player, old_location, old_position, new_location, new_position,
				piece);
	}

	public String getProgressFor(Integer gameId, Integer player) {
		return runningGames.get(gameId).getProgress(player).toString();
	}

	public Integer getCurrentPlayer(Integer gameId) {
		return runningGames.get(gameId).getCurrentPlayer();
	}
}