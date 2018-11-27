package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import it.mat.unical.asde.project2_puzzle.model.Grid;
import it.mat.unical.asde.project2_puzzle.model.services_utility.RunningGame;

@Service
public class GameService {
	HashMap<Integer, Grid> initalGrids = new HashMap<>();
	HashMap<Integer, RunningGame> runningGames = new HashMap<>();

	public Grid initNewGame(Integer gameId) {
		if (!initalGrids.containsKey(gameId)) {
			initalGrids.put(gameId, new Grid(0, "0"));
			if (!runningGames.containsKey(gameId))
				runningGames.put(gameId, new RunningGame(initalGrids.get(gameId).getDim()));
			return initalGrids.get(gameId);
		}
		return initalGrids.remove(gameId);
	}

	public boolean updateStateGame(Integer gameId, String player, String old_location, int old_position,
			String new_location, int new_position, String piece) {
		return runningGames.get(gameId).updateSateGame(player, old_location, old_position, new_location, new_position,
				piece);
	}

	public String getProgressFor(Integer gameId, String player) {
		return runningGames.get(gameId).getProgress(player).toString();
	}
}