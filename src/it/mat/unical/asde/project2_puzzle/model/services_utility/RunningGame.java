package it.mat.unical.asde.project2_puzzle.model.services_utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.mat.unical.asde.project2_puzzle.model.Grid;

public class RunningGame {

	private List<GridToComplete> gamePlayer;
	private Grid initialGrid;
	private int dim;
	private String user1;
	private String user2;

	public RunningGame(int difficulty) {
		int randomPuzzle = new Random().nextInt(13);
		initialGrid = new Grid(difficulty, "" + randomPuzzle);
		this.dim = initialGrid.getDim();
		gamePlayer = new ArrayList<>();
	}

	public boolean updateSateGame(Integer player, String old_location, int old_position, String new_location,
			int new_position, String piece, String timer) {
		return updateGameForPlayer(gamePlayer.get(player - 1), old_location, old_position, new_location, new_position,
				piece, timer);
	}

	private boolean updateGameForPlayer(GridToComplete gamePlayer12, String old_location, int old_position,
			String new_location, int new_position, String piece, String timer) {
		if (old_location.equals("to_complete"))
			gamePlayer12.removePiece(piece, old_position);
		if (new_location.equals("to_complete"))
			gamePlayer12.insertPiece(piece, new_position);
		gamePlayer12.setTimer(timer);
		return gamePlayer12.checkPuzzleTermination();
	}

	public Integer getProgress(Integer player) {
		return getStatusOf(gamePlayer.get(player - 1));
	}

	private Integer getStatusOf(GridToComplete gridToComplete) {
		return gridToComplete.getStatus();
	}

	public Integer getCurrentPlayer() {
		return gamePlayer.size();
	}

	public void addPlayer() {
		gamePlayer.add(new GridToComplete(dim));

	}

	public Grid getInitialGrid(Integer gameId) {
		return initialGrid;
	}

}
