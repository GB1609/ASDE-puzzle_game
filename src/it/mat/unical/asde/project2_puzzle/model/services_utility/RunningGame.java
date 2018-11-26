package it.mat.unical.asde.project2_puzzle.model.services_utility;

public class RunningGame {

	private GridToComplete gamePlayer1;
	private GridToComplete gamePlayer2;
	private String user1;
	private String user2;

	public RunningGame(int dim) {
		gamePlayer1 = new GridToComplete(dim);
		gamePlayer2 = new GridToComplete(dim);
	}

	public boolean updateSateGame(String player, String old_location, int old_position, String new_location,
			int new_position, String piece) {
		return updateGameForPlayer(player.equals("player1") ? gamePlayer1 : gamePlayer2, old_location, old_position,
				new_location, new_position, piece);
	}

	private boolean updateGameForPlayer(GridToComplete gamePlayer12, String old_location, int old_position,
			String new_location, int new_position, String piece) {
		if (old_location.equals("to_complete"))
			gamePlayer12.removePiece(piece, old_position);
		if (new_location.equals("to_complete"))
			gamePlayer12.insertPiece(piece, new_position);
		return gamePlayer12.checkPuzzleTermination();
	}

	public Integer getProgress(String player) {
		return getStatusOf(player.equals("player1") ? gamePlayer1 : gamePlayer2);
	}

	private Integer getStatusOf(GridToComplete gridToComplete) {
		return gridToComplete.getStatus();
	}

}
