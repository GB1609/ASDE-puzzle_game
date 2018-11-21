package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class EventsService {
	private Map<Integer, BlockingQueue<String>> events = new HashMap<>();

	public void addEvent(Integer gameID) throws InterruptedException {
		if (!events.containsKey(gameID)) {
			events.put(gameID, new LinkedBlockingQueue<>());
		}
		events.get(gameID).put("Aggiungo un nuovo evento" + new Date().toString());
	}

	public String nextEvent(Integer gameId) throws InterruptedException {
		if (!events.containsKey(gameId)) {
			events.put(gameId, new LinkedBlockingQueue<>());
		}
		return events.get(gameId).poll(29, TimeUnit.SECONDS);
	}

}
