package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class EventsService {
	private Map<String, BlockingQueue<String>> events = new HashMap<>();
	private Map<String, BlockingQueue<String>> join = new HashMap<>();

	public String nextEventProgress(Integer gameId, String player) throws InterruptedException {
		String key = gameId + player;
		if (!events.containsKey(key))
			events.put(key, new LinkedBlockingQueue<>());
		return events.get(key).poll(29, TimeUnit.SECONDS);
	}

	public String getEventJoin(String lobbyName) throws InterruptedException {
		if (!join.containsKey(lobbyName)) {
			join.put(lobbyName, new LinkedBlockingQueue<>());

			System.out.println("No listener attached for this lobby" + lobbyName);
//			return null;
		}
		String b = join.get(lobbyName).poll(29, TimeUnit.SECONDS);
		if (b != null) {

			if (b.equals("already-joined"))
				join.remove(lobbyName);
			else
				join.get(lobbyName).put("already-joined");

		}
		return b;
	}

	public void addEventJoin(String lobbyName) throws InterruptedException {
		if (!join.containsKey(lobbyName))
			throw new RuntimeException("This lobby isn't present in the list");
		join.get(lobbyName).put("join");
	}

	public void addEventFor(Integer gameID, String player, String progress) throws InterruptedException {
		String key = gameID + (player.equals("player1") ? "player2" : "player1");
		addEvent(progress, key);
	}

	private void addEvent(String progress, String key) throws InterruptedException {
		if (!events.containsKey(key))
			events.put(key, new LinkedBlockingQueue<>());
		events.get(key).put(progress);
	}

	public void attachListenerToJoin(String lobby_name) {

		if (join.containsKey(lobby_name))
			/* throw new RuntimeException */System.out
					.println("This lobby already has a listener attached" + lobby_name);
		else {
			System.out.println("listener attached for lobby" + lobby_name);
			join.put(lobby_name, new LinkedBlockingQueue<>());
			System.out.println(join.containsKey(lobby_name));
		}
	}

	public void detachListenerForJoin(String lobby_name) {
		join.remove(lobby_name);
	}

	public void addEventEndGame(Integer gameId) throws InterruptedException {
		String event = "END-GAME";
		addEvent(event, gameId + "player1");
		addEvent(event, gameId + "player2");
	}

	public void addEventLeaveGameBy(Integer gameID, String player) throws InterruptedException {
		String key = gameID + (player.equals("player1") ? "player2" : "player1");
		addEvent("END-GAME", key);

	}

}
