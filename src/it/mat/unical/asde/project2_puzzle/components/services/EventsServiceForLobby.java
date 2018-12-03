package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import it.mat.unical.asde.project2_puzzle.components.services.utility.MessageMaker;

@Service
public class EventsServiceForLobby {

	@Autowired
	private MessageMaker messageMaker;

	/*
	 * map to store the event in lobby management
	 */
	private Map<String, BlockingQueue<String>> eventsOfLobbies = new HashMap<>();
	/*
	 * Map to store the information about the event that the user is listening for.
	 * This structures is needful to avoid losing listeners when a player reload the
	 * page
	 */
	private Map<String, String> userListeningFor = new HashMap<>();

	/*
	 * Structures for monitoring the requests time. Used for identify the offline
	 * players
	 */
	/**
	 * Map to store the last request from the owner
	 * 
	 * The Key is a string that represents a key in eventsOfLobbies map. The Value
	 * is a Map used to store:
	 * <ul>
	 * <li>a Date object that represent the last time in which a request for the Key
	 * is made;</li>
	 * <li>an String object that represent the player that made the request;</li>
	 * </ul>
	 * When a request for a key are made then the Date is update.
	 */
	private HashMap<String, HashMap<String, Object>> lastRequestOwners = new HashMap<>();

	/**
	 * Map to store the last request from the joiners
	 * 
	 * The Key is a string that represents a key in eventsOfLobbies map. The Value
	 * is a Map used to store:
	 * <ul>
	 * <li>a Date object that represent the last time in which a request for the Key
	 * is made;</li>
	 * <li>an String object that represent the player that made the request;</li>
	 * </ul>
	 * When a request for a key are made then the Date is update.
	 */
	private HashMap<String, HashMap<String, Object>> lastRequestJoiner = new HashMap<>();

	/**
	 * Update the last request for the key specified
	 * 
	 * @param lastRequests the map to update
	 * @param requestKey   the key for which the request was made
	 * @param username     the user who made the request
	 */
	private void updateLastRequest(HashMap<String, HashMap<String, Object>> lastRequests, String requestKey,
			String username) {
		if (!lastRequests.containsKey(requestKey))
			lastRequests.put(requestKey, new HashMap<>());
		HashMap<String, Object> map = lastRequests.get(requestKey);
		map.put("requestIn", new Date());
		map.put("user", username);
	}

	/**
	 * Add a simple general event to map of events
	 * 
	 * @param key          key for which add the event
	 * @param message_type the type of message to add in the event
	 * @param forClear     if the event is generated form the cleaning procedure
	 * @throws InterruptedException
	 */
	private void addGeneralEventLobby(String key, String message_type, boolean forClear) throws InterruptedException {
		if (!eventsOfLobbies.containsKey(key)) {
			if (forClear)
				return;
			throw new RuntimeException("No join found for this lobby" + key);
		}
		if (forClear)
			System.out.println("leave to" + key);
		eventsOfLobbies.get(key).put(messageMaker.makeMessage(message_type, MessageMaker.FOR_CLEANING, forClear + ""));
	}

	/**
	 * Add to map of events a general event that contains a general message
	 * 
	 * @param key             key for which add the event
	 * @param message_type    the type of message to add in the events
	 * @param message         key of message to add in the event, it identify the
	 *                        message_content
	 * @param message_content message to add in the event
	 * @param forClear        if the event is generated form the cleaning procedure
	 * @throws InterruptedException
	 */
	private void addGeneralEventLobby(String key, String message_type, String message, String message_content,
			boolean forClear) throws InterruptedException {
		if (!eventsOfLobbies.containsKey(key)) {
			if (forClear)
				return;
			throw new RuntimeException("No join found for this lobby");
		}
		eventsOfLobbies.get(key).put(messageMaker.makeMessage(message_type, message, message_content));
	}

	/**
	 * Add the start game event to the lobby passed as parameter. This event will be
	 * sent from the owner of the lobby to the joiner
	 * 
	 * @param lobby_name the lobby for which add the event
	 * @throws InterruptedException
	 */
	public void addEventStartGame(String lobby_name) throws InterruptedException {
		String key = lobby_name.toLowerCase() + "player2";

		/*
		 * In order to deal with the page reloading, the event must be added twice. This
		 * because if a joiner is listening for the start game event and he reload the
		 * page, the first event will be received from the old request while the second
		 * one from the new request created after the reload.
		 */
		addGeneralEventLobby(key, MessageMaker.START_MESSAGE, false);
		addGeneralEventLobby(key, MessageMaker.START_MESSAGE, false);
	}

	/**
	 * Add the event of join for the lobby passed as parameter
	 * 
	 * @param lobbyName the lobby for which add the event
	 * @param username  user that joins to the lobby
	 * @throws InterruptedException
	 */
	public void addEventJoin(String lobbyName, String username) throws InterruptedException {

		/*
		 * In order to deal with the page reloading, the event must be added twice.
		 * (Look at the "addEventStartGame" method for more details)
		 */
		addGeneralEventLobby(lobbyName.toLowerCase(), MessageMaker.JOIN_MESSAGE, MessageMaker.WHO_JOIN, username,
				false);
		addGeneralEventLobby(lobbyName.toLowerCase(), MessageMaker.JOIN_MESSAGE, MessageMaker.WHO_JOIN, username,
				false);
	}

	/**
	 * Add the event of leave join for the lobby passed as parameter
	 * 
	 * @param previousJoined the lobby that the player leave
	 * @param username       the player that leave the lobby
	 * @param fromClear      if the event is generated form the cleaning procedure
	 * @throws InterruptedException
	 */
	public void addEventLeaveJoin(String previousJoined, String username, boolean fromClear)
			throws InterruptedException {
		Pattern p = Pattern.compile("(.+)player2$");// TODO transorm to N
		Matcher meMatcher = p.matcher(previousJoined);
		// if it is the owner of the lobby to leave the lobby
		if (meMatcher.matches()) {
			String s = meMatcher.group(1);
			// send the event to it
			addGeneralEventLobby(s, MessageMaker.LEAVE_MESSAGE, MessageMaker.OWNER, username, fromClear);
			// if present send the event to the joiner
			if (eventsOfLobbies.containsKey(previousJoined)) {
				addGeneralEventLobby(previousJoined, MessageMaker.LEAVE_MESSAGE, fromClear);
			}
		} else {
			// otherwise if it is the joiner to leave the lobby
			/*
			 * In order to deal with the page reloading, the event must be added twice.
			 * (Look at the "addEventStartGame" method for more details)
			 */
			addGeneralEventLobby(previousJoined, MessageMaker.LEAVE_MESSAGE, MessageMaker.JOINER, username, fromClear);
			addGeneralEventLobby(previousJoined, MessageMaker.LEAVE_MESSAGE, MessageMaker.JOINER, username, fromClear);
			addGeneralEventLobby(previousJoined + "player2", MessageMaker.LEAVE_MESSAGE, MessageMaker.JOINER, username,
					fromClear);
			userListeningFor.remove(username);
		}
	}

	/*
	 * Methods to attach the various listeners
	 */
	public void attachListenerToJoin(String lobby_name, String username) {

		if (eventsOfLobbies.containsKey(lobby_name))
			System.out.println("This lobby already has a listener attached" + lobby_name);
		else {
			eventsOfLobbies.put(lobby_name, new LinkedBlockingQueue<>());
			userListeningFor.put(username,
					messageMaker.makeMessage(MessageMaker.JOIN_MESSAGE, MessageMaker.LOBBY_NAME, lobby_name));
		}
	}

	public void attachListenerToStart(String lobby_name, String username) {
		String key = lobby_name.toLowerCase() + "player2";

		if (eventsOfLobbies.containsKey(key))
			System.out.println("This lobby already has a listener attached" + lobby_name);
		else {
			System.out.println("listener attached for start" + lobby_name);
			eventsOfLobbies.put(key, new LinkedBlockingQueue<>());
			userListeningFor.put(username,
					messageMaker.makeMessage(MessageMaker.START_MESSAGE, MessageMaker.LOBBY_NAME, lobby_name));
			System.out.println(eventsOfLobbies.containsKey(key));
		}
	}

	public void detachListenerForJoin(String lobby_name, String username) {
		eventsOfLobbies.remove(lobby_name.toLowerCase());
		userListeningFor.remove(username);
	}

	public void detachListenerForStart(String lobby_name, String username, Integer player) {
		System.out.println("detach listener for start" + lobby_name + "username" + username + player);
		eventsOfLobbies.remove(lobby_name.toLowerCase() + "player" + player);
		userListeningFor.remove(username);
	}

	public void detachListenerForStart(String username, Integer player) {
		String listenFor = userListeningFor.get(username);
		if (listenFor != null) {
			JSONObject jo = new JSONObject(listenFor);
			detachListenerForStart(jo.getString(MessageMaker.LOBBY_NAME), username, player);
		}
	}

	/*
	 * Methods to get the events
	 */
	public String getEventJoin(String lobbyName, String username) throws InterruptedException {
		lobbyName = lobbyName.toLowerCase();
		if (!eventsOfLobbies.containsKey(lobbyName)) {
			eventsOfLobbies.put(lobbyName, new LinkedBlockingQueue<>());
		}
		userListeningFor.put(username,
				messageMaker.makeMessage(MessageMaker.JOIN_MESSAGE, MessageMaker.LOBBY_NAME, lobbyName));
		updateLastRequest(lastRequestOwners, lobbyName, username);
		return eventsOfLobbies.get(lobbyName).poll(29, TimeUnit.SECONDS);
	}

	public String getEventStartGame(String lobbyName, String username) throws InterruptedException {
		String key = lobbyName.toLowerCase() + "player2";
		if (!eventsOfLobbies.containsKey(key)) {
			eventsOfLobbies.put(key, new LinkedBlockingQueue<>());
		}
		userListeningFor.put(username,
				messageMaker.makeMessage(MessageMaker.START_MESSAGE, MessageMaker.LOBBY_NAME, lobbyName));
		updateLastRequest(lastRequestJoiner, key, username);
		return eventsOfLobbies.get(key).poll(29, TimeUnit.SECONDS);
	}

	public String getListenerOfUser(String username) {
		System.out.println("checkFor" + username + " listener " + userListeningFor.get(username));

		String listenFor = userListeningFor.get(username);
		if (listenFor != null) {
			JSONObject jo = new JSONObject(listenFor);
			if (jo.has(MessageMaker.JOIN_MESSAGE)) {

				updateLastRequest(lastRequestOwners, jo.getString(MessageMaker.LOBBY_NAME), username);
			} else {

				updateLastRequest(lastRequestJoiner, jo.getString(MessageMaker.LOBBY_NAME) + "player2", username);
			}
		}
		return listenFor;
	}

	/*
	 * This scheduled method check if there is any player who does not make a
	 * request for 30 second. If this happens it means that the player closed the
	 * app so it is considered offline.
	 */

	@Scheduled(fixedDelay = 29000)
	public void clearAndNotify() {

		Date now = new Date();
		System.out.println("In schedule for clear joiner" + now);
		Iterator<Map.Entry<String, HashMap<String, Object>>> iter = lastRequestJoiner.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, HashMap<String, Object>> entry = iter.next();
			HashMap<String, Object> value = entry.getValue();
			long diffInMillies = now.getTime() - ((Date) value.get("requestIn")).getTime();
			diffInMillies = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
			System.out.println("joiner" + (String) value.get("user") + "noRequest from " + diffInMillies + "sec");
			if (diffInMillies >= 30) {
				System.out.println("remove " + entry.getKey() + "from join listener, becouse offline since"
						+ diffInMillies + " seconds");
				Pattern p = Pattern.compile("(.+)player2$");// TODO transorm to N
				Matcher meMatcher = p.matcher(entry.getKey());
				if (meMatcher.matches()) {
					String s = meMatcher.group(1);
					try {
						addEventLeaveJoin(s, (String) value.get("user"), true);
					} catch (InterruptedException e) {
						// do nothings
					}
				}
				iter.remove();
			}
		}
		System.out.println("In schedule for clear joiner" + now);
		now = new Date();
		System.out.println("In schedule for clear owner" + now);

		Iterator<Map.Entry<String, HashMap<String, Object>>> ownerIter = lastRequestOwners.entrySet().iterator();
		while (ownerIter.hasNext()) {
			Map.Entry<String, HashMap<String, Object>> entry = ownerIter.next();
			HashMap<String, Object> value = entry.getValue();
			long diffInMillies = now.getTime() - ((Date) value.get("requestIn")).getTime();
			diffInMillies = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
			System.out.println("owner" + (String) value.get("user") + "noRequest from " + diffInMillies + "sec");

			if (diffInMillies >= 30) {
				System.out.println("remove " + entry.getKey() + "from owners listener, because offline since"
						+ diffInMillies + " seconds");
				try {
					addEventLeaveJoin(entry.getKey() + "player2", (String) value.get("user"), true);
				} catch (InterruptedException e) {
					// do nothings
				}

				ownerIter.remove();
			}
		}
		System.out.println("End In schedule for clear owner");
	}

}
