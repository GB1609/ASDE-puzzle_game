package it.mat.unical.asde.project2_puzzle.model.services_utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.mat.unical.asde.project2_puzzle.model.Grid;

public class RunningGame {

	private List<GridToComplete> gamePlayer;
	private List<GridToComplete> scoresOrder;
	private String lobbyName;
	private int currentPlayer = 0;
	private Grid initialGrid;
	private int dim;

	public RunningGame(int difficulty, String lobbyName) {
		int randomPuzzle = new Random().nextInt(13);
		initialGrid = new Grid(difficulty, "" + randomPuzzle);
		this.dim = initialGrid.getDim();
		this.lobbyName = lobbyName;
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

	synchronized public Integer getCurrentPlayer() {
		return ++currentPlayer;
	}

	synchronized public void addPlayer(String playerToAdd) {
		gamePlayer.add(new GridToComplete(dim, playerToAdd));

	}

	public Integer getPlayerInGame() {
		return gamePlayer.size();
	}

	public Grid getInitialGrid(Integer gameId) {
		return initialGrid;
	}

	public String getWinner() {
		scoresOrder = new ArrayList<>(gamePlayer);
		scoresOrder.sort((g1, g2) -> {
			if (g1.leaved() && !g2.leaved())
				return 1;
			if (g2.leaved() && !g1.leaved())
				return -1;
			Integer statusG1 = g1.getStatus();
			Integer statusG2 = g2.getStatus();
			System.out.println("Nessuno lascia e il tempo e uguale");
			if (statusG1 == statusG2)
				return 0;
			return statusG1 > statusG2 ? -1 : 1;
		});
		return scoresOrder.get(0).getPlayer();

	}

	public ArrayList<String> getUsersOrdered() {
		ArrayList<String> users = new ArrayList<>(scoresOrder.size());
		for (GridToComplete g : scoresOrder)
			users.add(g.getPlayer());
		return users;
	}

	public String getTime() {
		return (scoresOrder.get(0).getTimer());
	}

	public String getLobbyName() {
		return this.lobbyName;
	}

	public void userLeaveGame(Integer player) {
		System.out.println("set leave from" + (player - 1));
		gamePlayer.get(player - 1).setLeaved(true);
	}

}
