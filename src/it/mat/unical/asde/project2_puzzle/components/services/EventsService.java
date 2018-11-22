package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class EventsService {
	private Map<String, BlockingQueue<Integer>> events = new HashMap<>();

	public Integer nextEvent(Integer gameId, String player) throws InterruptedException {
		String key = gameId + player;
		if (!events.containsKey(key))
			events.put(key, new LinkedBlockingQueue<>());
		return events.get(key).poll(29, TimeUnit.SECONDS);
	}

	public void addEventFor(Integer gameID, String player, Integer progress) throws InterruptedException {
		String key = gameID + (player.equals("player1") ? "player2" : "player1");
		if (!events.containsKey(key))
			events.put(key, new LinkedBlockingQueue<>());
		events.get(key).put(progress);
	}

}
