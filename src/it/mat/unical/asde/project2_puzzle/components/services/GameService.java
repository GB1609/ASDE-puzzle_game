package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.mat.unical.asde.project2_puzzle.components.persistence.MatchDAO;
import it.mat.unical.asde.project2_puzzle.components.persistence.UserDAO;
import it.mat.unical.asde.project2_puzzle.model.GameMatch;
import it.mat.unical.asde.project2_puzzle.model.Grid;
import it.mat.unical.asde.project2_puzzle.model.User;
import it.mat.unical.asde.project2_puzzle.model.services_utility.RunningGame;

@Service
public class GameService {
	HashMap<Integer, RunningGame> runningGames = new HashMap<>();
	HashMap<Integer, GameMatch> matches = new HashMap<>();

	@Autowired
	private UserDAO userDao;
	@Autowired
	private MatchDAO matchDao;

	public void initNewGame(Integer gameId, String lobbyName) {
		if (!runningGames.containsKey(gameId)) {
			runningGames.put(gameId, new RunningGame(0, lobbyName));
			System.out.println("init");
		}
	}

	public Grid getGameForPlayer(Integer gameId, String forPlayer) {
		RunningGame rgGame = runningGames.get(gameId);
		rgGame.addPlayer(forPlayer);
		return rgGame.getInitialGrid(gameId);
	}

	public boolean updateStateGame(Integer gameId, Integer player, String old_location, int old_position,
			String new_location, int new_position, String piece, String timer) {
		return runningGames.get(gameId).updateSateGame(player, old_location, old_position, new_location, new_position,
				piece, timer);
	}

	public String getProgressFor(Integer gameId, Integer player) {
		return runningGames.get(gameId).getProgress(player).toString();
	}

	public Integer getCurrentPlayer(Integer gameId) {
		return runningGames.get(gameId).getCurrentPlayer();
	}

	public void storeMatch(Integer gameId) {
		GameMatch m = new GameMatch();
		User winner = userDao.getUser(runningGames.get(gameId).getWinner());
		m.setWinner(winner);
		for (String s : runningGames.get(gameId).getUsersOrdered()) {
			User user = userDao.getFullUser(s);
			m.addUser(user);
			m.setTime(runningGames.get(gameId).getTime());
		}
		m.setLobbyName(runningGames.get(gameId).getLobbyName());
		matches.put(gameId, m);
	}

	public GameMatch getMatch(Integer gameId) {
		return matches.get(gameId);
	}
}