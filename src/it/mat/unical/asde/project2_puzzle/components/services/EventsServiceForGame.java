package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import it.mat.unical.asde.project2_puzzle.components.services.utility.MessageMaker;

@Service
public class EventsServiceForGame {

	@Autowired
	private MessageMaker messageMaker;

	private Map<String, BlockingQueue<String>> eventsInGame = new HashMap<>();
	/////////////////////// CLIENT MONITORING
	/////////////////////// STRUCTURES//////////////////////////////////////
	private Map<Integer, Set<String>> playerOffline = new HashMap<>();
	private HashMap<String, HashMap<String, Object>> lastRequestPlayerInGame = new HashMap<>();

	/////////////////////////////////// ADD
	/////////////////////////////////// EVENTS//////////////////////////////////////////////
	private void addEvent(String progress, String key, Integer gameId, Boolean forClear) throws InterruptedException {
		if (!(playerOffline.containsKey(gameId) && playerOffline.get(gameId).contains(key))) {
			if (!eventsInGame.containsKey(key)) {
				if (forClear)
					return;
				eventsInGame.put(key, new LinkedBlockingQueue<>());
			}
			eventsInGame.get(key).put(progress);
		}
	}

//////////////////////////EVENT IN GAME/////////////////////////
	public void addEventFor(Integer gameID, Integer player, Integer currentPlayers, String progress)
			throws InterruptedException {
// add message for other players
		for (int i = 1; i <= currentPlayers; i++) {
			String key = gameID + "player" + i;
			if (i != player)
				addEvent(messageMaker.makeMessage(MessageMaker.UPDATE_MESSAGE, MessageMaker.PROGRESS_MESSAGE, progress),
						key, gameID, false);

		}

	}

	public void addMessageFor(Integer gameId, Integer player, Integer currentPlayers, String message)
			throws InterruptedException {

		for (int i = 1; i <= currentPlayers; i++) {
			String key = gameId + "player" + i;
			if (i != player)
				addEvent(messageMaker.makeMessage(MessageMaker.CHAT_MESSAGE, MessageMaker.TEXT_MESSAGE, message), key,
						gameId, false);

		}
	}

	public void addEventEndGame(Integer gameId, Integer currentPlayers) throws InterruptedException {
		String event = "END-GAME";
		for (int i = 1; i <= currentPlayers; i++) {
			String key = gameId + "player" + i;
			addEvent(event, key, gameId, false);
		}

	}

	public void addEventLeaveGameBy(Integer gameID, Integer player, Integer currentPlayers, boolean forClear)
			throws InterruptedException {
		Set<String> alreadyLeaved = playerOffline.get(gameID);

		if (currentPlayers == 2 || (alreadyLeaved != null && (alreadyLeaved.size() - currentPlayers) == 2))
			for (int i = 1; i <= currentPlayers; i++) {
				String key = gameID + "player" + i;
				if (i != player) {
					System.out.println("Send end game for" + gameID + "player" + i);

					addEvent("END-GAME", key, gameID, forClear);
				}
			}
		else
			addLeaved(gameID, player);// TODO ADD LEAVED ALSO IN CLEANING
	}

	private void addLeaved(Integer gameID, Integer player) {

		if (!playerOffline.containsKey(gameID))
			playerOffline.put(gameID, new HashSet<>());
		playerOffline.get(gameID).add(gameID + "player" + player);
	}

	public void detachListenerInGame(Integer gameId, Integer player) {
		String key = gameId + "player" + player;
		eventsInGame.remove(key);
	}

	/////////////////////////////////// GET
	/////////////////////////////////// EVENTS//////////////////////////////////////////////
	public String nextGameEventFor(Integer gameId, Integer player, Integer playersInGame) throws InterruptedException {
		String key = gameId + "player" + player;
		if (!eventsInGame.containsKey(key)) {
			eventsInGame.put(key, new LinkedBlockingQueue<>());
			HashMap<String, Object> toPut = new HashMap<>();
			toPut.put("gameId", gameId);
			toPut.put("player", player);
			toPut.put("playerInGame", playersInGame);
			lastRequestPlayerInGame.put(key, toPut);
		}
		System.out.println("update for player in game request " + gameId + " player" + player);
		lastRequestPlayerInGame.get(key).put("requestIn", new Date());
		return eventsInGame.get(key).poll(29, TimeUnit.SECONDS);
	}

	@Scheduled(fixedDelay = 35000)
	public void clearPlayerInGame() {
		Date now = new Date();
		System.out.println("In schedule for clear players" + now);

		Iterator<Map.Entry<String, HashMap<String, Object>>> iter = lastRequestPlayerInGame.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, HashMap<String, Object>> entry = iter.next();
			HashMap<String, Object> values = entry.getValue();
			long diffInMillies = now.getTime() - ((Date) values.get("requestIn")).getTime();
			diffInMillies = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
			if (diffInMillies > 30) {
				System.out.println("remove " + entry.getKey() + "from game listener, because offline since"
						+ diffInMillies + " seconds");
				try {
					addEventLeaveGameBy((Integer) values.get("gameId"), (Integer) values.get("player"),
							(Integer) values.get("playerInGame"), true);
				} catch (InterruptedException e) {
					// do nothings
				}

				iter.remove();
			}
		}
		System.out.println("End In schedule for clear players");
		// something that should execute periodically
	}
}
