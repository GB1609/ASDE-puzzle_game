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

	private Map<String, BlockingQueue<String>> eventsOfLobbies = new HashMap<>();
	private Map<String, String> userListeningFor = new HashMap<>();

	/////////////////////// CLIENT MONITORING
	/////////////////////// STRUCTURES//////////////////////////////////////
	private HashMap<String, HashMap<String, Object>> lastRequestOwners = new HashMap<>();
	private HashMap<String, HashMap<String, Object>> lastRequestJoiner = new HashMap<>();

	/////////////////////////////////// ADD
	/////////////////////////////////// EVENTS//////////////////////////////////////////////
	private void updateLastRequest(HashMap<String, HashMap<String, Object>> lastRequests, String requestKey,
			String username) {
		if (!lastRequests.containsKey(requestKey))
			lastRequests.put(requestKey, new HashMap<>());
		HashMap<String, Object> map = lastRequests.get(requestKey);
		map.put("requestIn", new Date());
		map.put("user", username);
	}

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

	private void addGeneralEventLobby(String key, String message_type, String message, String message_content,
			boolean forClear) throws InterruptedException {
		if (!eventsOfLobbies.containsKey(key)) {
			if (forClear)
				return;
			throw new RuntimeException("No join found for this lobby");
		}
		eventsOfLobbies.get(key).put(messageMaker.makeMessage(message_type, message, message_content));
	}

	////////////////////////// EVENT BEFORE GAME/////////////////////////
	public void addEventStartGame(String lobby_name) throws InterruptedException {
		String key = lobby_name.toLowerCase() + "player2";
		addGeneralEventLobby(key, MessageMaker.START_MESSAGE, false);
		addGeneralEventLobby(key, MessageMaker.START_MESSAGE, false);
	}

	public void addEventJoin(String lobbyName, String username) throws InterruptedException {
		addGeneralEventLobby(lobbyName.toLowerCase(), MessageMaker.JOIN_MESSAGE, MessageMaker.WHO_JOIN, username,
				false);
		addGeneralEventLobby(lobbyName.toLowerCase(), MessageMaker.JOIN_MESSAGE, MessageMaker.WHO_JOIN, username,
				false);
	}

	public void addEventLeaveJoin(String previousJoined, String username, boolean fromClear)
			throws InterruptedException {
		Pattern p = Pattern.compile("(.+)player2$");// TODO transorm to N
		Matcher meMatcher = p.matcher(previousJoined);
		if (meMatcher.matches()) {
			String s = meMatcher.group(1);
			System.out.println("GROUP1: " + s);
			System.out.println("add event leave to owner");
			addGeneralEventLobby(s, MessageMaker.LEAVE_MESSAGE, MessageMaker.OWNER, username, fromClear);
			if (eventsOfLobbies.containsKey(previousJoined)) {
				addGeneralEventLobby(previousJoined, MessageMaker.LEAVE_MESSAGE, fromClear);
				System.out.println("add event leave by owner for " + previousJoined);

			}
		} else {
			System.out.println("A JOINER has leave");
//			if (!fromClear)
//				detachListenerForStart(previousJoined, username, 2);// TODO REMOVE STATIC 2
			addGeneralEventLobby(previousJoined, MessageMaker.LEAVE_MESSAGE, MessageMaker.JOINER, username, fromClear);
			addGeneralEventLobby(previousJoined, MessageMaker.LEAVE_MESSAGE, MessageMaker.JOINER, username, fromClear);
			addGeneralEventLobby(previousJoined + "player2", MessageMaker.LEAVE_MESSAGE, MessageMaker.JOINER, username,
					fromClear);
			userListeningFor.remove(username);

		}
		System.out.println("remove listen for" + username + "to li " + userListeningFor.remove(username));

	}

	public void attachListenerToJoin(String lobby_name, String username) {

		if (eventsOfLobbies.containsKey(lobby_name))
			/* throw new RuntimeException */System.out
					.println("This lobby already has a listener attached" + lobby_name);
		else {
			System.out.println("listener attached for lobby" + lobby_name);
			eventsOfLobbies.put(lobby_name, new LinkedBlockingQueue<>());
			userListeningFor.put(username,
					messageMaker.makeMessage(MessageMaker.JOIN_MESSAGE, MessageMaker.LOBBY_NAME, lobby_name));
			System.out.println(eventsOfLobbies.containsKey(lobby_name));
		}
	}

	public void attachListenerToStart(String lobby_name, String username) {
		String key = lobby_name.toLowerCase() + "player2";

		if (eventsOfLobbies.containsKey(key))
			/* throw new RuntimeException */System.out
					.println("This lobby already has a listener attached" + lobby_name);
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

	/////////////////////////////////// GET
	/////////////////////////////////// EVENTS//////////////////////////////////////////////

	public String getEventJoin(String lobbyName, String username) throws InterruptedException {
		lobbyName = lobbyName.toLowerCase();
		if (!eventsOfLobbies.containsKey(lobbyName)) {
			// TODO THROW EXCEPTION
			eventsOfLobbies.put(lobbyName, new LinkedBlockingQueue<>());

			System.out.println("No listener attached for this lobby" + lobbyName);
//			return null;
		}
		System.out.println("update for OWNER lobby name " + lobbyName);
		userListeningFor.put(username,
				messageMaker.makeMessage(MessageMaker.JOIN_MESSAGE, MessageMaker.LOBBY_NAME, lobbyName));
		updateLastRequest(lastRequestOwners, lobbyName, username);
		return eventsOfLobbies.get(lobbyName).poll(29, TimeUnit.SECONDS);
	}

	public String getEventStartGame(String lobbyName, String username) throws InterruptedException {
		String key = lobbyName.toLowerCase() + "player2";
		if (!eventsOfLobbies.containsKey(key)) {
			eventsOfLobbies.put(key, new LinkedBlockingQueue<>());
			System.out.println("No listener attached for this lobby" + lobbyName);
		}
		System.out.println("update for Joiner lobby name " + lobbyName);
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
				System.out.println("update for OWNER lobby name " + jo.getString(MessageMaker.LOBBY_NAME));

				updateLastRequest(lastRequestOwners, jo.getString(MessageMaker.LOBBY_NAME), username);
			} else {
				System.out.println("update for Joiner lobby name " + jo.getString(MessageMaker.LOBBY_NAME));

				updateLastRequest(lastRequestJoiner, jo.getString(MessageMaker.LOBBY_NAME) + "player2", username);
			}
		}
		return listenFor;
	}

	@Scheduled(fixedDelay = 29000)
	public void clearJoinerAndNotifyOwner() {

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
		// something that should execute periodically
	}

}
