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

	/*
	 * map to store the event in game
	 */
	private Map<String, BlockingQueue<String>> eventsInGame = new HashMap<>();
	/*
	 * Structures to monitoring the requests time. Used for identify the offline
	 * players
	 */
	/**
	 * structure used to store the players become offline in a game
	 * <ul>
	 * <li>Key: an Integer that represents the identifier of a game</li>
	 * <li>Value: a String that represents a player offline in the game</li>
	 * </ul>
	 */
	private Map<Integer, Set<String>> playerOffline = new HashMap<>();
	/**
	 * The Key is a string that represents a key in eventsInGame map. The Value is a
	 * Map used to store:
	 * <ul>
	 * <li>a Date object that represent the last time in which a request for the Key
	 * is made;</li>
	 * <li>an Integer object that represent the player that made the request;</li>
	 * <li>an Integer object that represent the number of players involved in the
	 * game;</li>
	 * </ul>
	 * When a request for a key are made then the Date is update.
	 */
	private HashMap<String, HashMap<String, Object>> lastRequestPlayerInGame = new HashMap<>();

	/**
	 * Add an event in the map of events
	 * 
	 * @param progress the event to add in the map
	 * @param key      key for which add the event
	 * @param gameId   the identifier of the game in which the event was generated
	 * @param forClear if the event is generated form the cleaning procedure
	 * @throws InterruptedException
	 */
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

	/**
	 * Add an event for all players in game different from the player that has
	 * generated the event.
	 * 
	 * @param gameID         the identifier of the game in which the event was
	 *                       generated
	 * @param player         the player that has generated the event
	 * @param currentPlayers the number of players in game
	 * @param progress       the event to send
	 * @throws InterruptedException
	 */
	public void addEventFor(Integer gameID, Integer player, Integer currentPlayers, String progress)
			throws InterruptedException {
		for (int i = 1; i <= currentPlayers; i++) {
			String key = gameID + "player" + i;
			if (i != player)
				addEvent(messageMaker.makeMessage(MessageMaker.UPDATE_MESSAGE, MessageMaker.PROGRESS_MESSAGE, progress),
						key, gameID, false);
		}
	}

	/**
	 * Add a chat message for all players in game different from the player that has
	 * sent the message.
	 * 
	 * @param gameId         the identifier of the game in which the message was
	 *                       generated
	 * @param player         the player that has sent the event
	 * @param currentPlayers the number of player in game
	 * @param message        the message to send
	 * @throws InterruptedException
	 */
	public void addMessageFor(Integer gameId, Integer player, Integer currentPlayers, String message)
			throws InterruptedException {
		for (int i = 1; i <= currentPlayers; i++) {
			String key = gameId + "player" + i;
			if (i != player)
				addEvent(messageMaker.makeMessage(MessageMaker.CHAT_MESSAGE, MessageMaker.TEXT_MESSAGE, message), key,
						gameId, false);

		}
	}

	/**
	 * Add the event "end game" for all players in game
	 * 
	 * @param gameId         the identifier of the game finished
	 * @param currentPlayers the number of players in game
	 * @throws InterruptedException
	 */
	public void addEventEndGame(Integer gameId, Integer currentPlayers) throws InterruptedException {
		for (int i = 1; i <= currentPlayers; i++) {
			String key = gameId + "player" + i;
			addEvent(messageMaker.makeMessage(MessageMaker.END_GAME), key, gameId, false);
		}

	}

	/**
	 * Add the event "leave game" for all players in game different from the player
	 * that left the game.
	 * 
	 * @param gameID         the identifier of the game left
	 * @param player         the player that left the game
	 * @param currentPlayers the number of players in game
	 * @param forClear       if the event is generated from the cleaning procedure
	 * @throws InterruptedException
	 */
	public void addEventLeaveGameBy(Integer gameID, Integer player, Integer currentPlayers, boolean forClear)
			throws InterruptedException {
		Set<String> alreadyLeaved = playerOffline.get(gameID);
		// if there are only two players left in game, send the event "end game"
		// otherwise add the player left in the structure of offline player.
		if (currentPlayers == 2 || (alreadyLeaved != null && (alreadyLeaved.size() - currentPlayers) == 2))
			for (int i = 1; i <= currentPlayers; i++) {
				String key = gameID + "player" + i;
				if (i != player) {
					if (forClear)
						addEvent(messageMaker.makeMessage(MessageMaker.END_GAME, MessageMaker.CAUSE_OFFLINE,
								player + ""), key, gameID, forClear);
					else
						addEvent(messageMaker.makeMessage(MessageMaker.END_GAME), key, gameID, forClear);
				}
			}
		else
			addLeaved(gameID, player);// TODO ADD LEAVED ALSO IN CLEANING
	}

	/**
	 * Add the player in the offline players of the game passed as parameter
	 * 
	 * @param gameID the identifier of the game
	 * @param player the identifier of the player
	 */
	private void addLeaved(Integer gameID, Integer player) {

		if (!playerOffline.containsKey(gameID))
			playerOffline.put(gameID, new HashSet<>());
		playerOffline.get(gameID).add(gameID + "player" + player);
	}

	/**
	 * Detach the listener of player in the game
	 * 
	 * @param gameId the identifier of the game
	 * @param player the identifier of the player
	 */
	public void detachListenerInGame(Integer gameId, Integer player) {
		String key = gameId + "player" + player;
		eventsInGame.remove(key);
	}

	/**
	 * Get the next event for the player passed as parameter in game
	 * 
	 * @param gameId        the identifier of the game
	 * @param player        the identifier of the player
	 * @param playersInGame the number of players in game
	 * @return
	 * @throws InterruptedException
	 */
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
		// update the time of the last request for the key
		lastRequestPlayerInGame.get(key).put("requestIn", new Date());
		return eventsInGame.get(key).poll(29, TimeUnit.SECONDS);
	}

	/*
	 * This scheduled method check if there is any player who does not make a
	 * request for 30 second. If this happens it means that the player closed the
	 * game so it is considered offline for the game.
	 */
	@Scheduled(fixedDelay = 30000)
	public void clearPlayerInGame() {
		Date now = new Date();
		System.out.println("In schedule for clear players" + now);

		Iterator<Map.Entry<String, HashMap<String, Object>>> iter = lastRequestPlayerInGame.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, HashMap<String, Object>> entry = iter.next();
			HashMap<String, Object> values = entry.getValue();
			long diff = now.getTime() - ((Date) values.get("requestIn")).getTime();
			diff = TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS);
			if (diff >= 30) {
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
	}
}
