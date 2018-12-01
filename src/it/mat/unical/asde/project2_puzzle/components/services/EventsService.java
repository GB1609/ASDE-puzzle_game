package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class EventsService {
	private Map<String, BlockingQueue<String>> events = new HashMap<>();
	private Map<String, BlockingQueue<String>> join = new HashMap<>();
	private Map<Integer, Set<String>> leaved = new HashMap<>();
	private Map<String, String> listeningFor = new HashMap<>();

	private MessageMaker maker = new MessageMaker();

	/////////////////////////////////// ADD
	/////////////////////////////////// EVENTS//////////////////////////////////////////////
	private void addEvent(String progress, String key, Integer gameId) throws InterruptedException {
		if (!(leaved.containsKey(gameId) && leaved.get(gameId).contains(key))) {
			if (!events.containsKey(key))
				events.put(key, new LinkedBlockingQueue<>());
			events.get(key).put(progress);
		}
	}

	private void addGeneralEventLobby(String key, String message_type) throws InterruptedException {
		if (!join.containsKey(key))
			throw new RuntimeException("No join found for this lobby" + key);
		join.get(key).put(maker.makeMessage(message_type));
	}

	private void addGeneralEventLobby(String key, String message_type, String message, String message_content)
			throws InterruptedException {
		if (!join.containsKey(key))
			throw new RuntimeException("No join found for this lobby");
		join.get(key).put(maker.makeMessage(message_type, message, message_content));
	}

	////////////////////////// EVENT IN GAME/////////////////////////
	public void addEventFor(Integer gameID, Integer player, Integer currentPlayers, String progress)
			throws InterruptedException {
		// add message for other players
		for (int i = 1; i <= currentPlayers; i++) {
			String key = gameID + "player" + i;
			if (i != player)
				addEvent(maker.makeMessage(MessageMaker.UPDATE_MESSAGE, MessageMaker.PROGRESS_MESSAGE, progress), key,
						gameID);

		}

	}

	public void addMessageFor(Integer gameId, Integer player, Integer currentPlayers, String message)
			throws InterruptedException {

		for (int i = 1; i <= currentPlayers; i++) {
			String key = gameId + "player" + i;
			if (i != player)
				addEvent(maker.makeMessage(MessageMaker.CHAT_MESSAGE, MessageMaker.TEXT_MESSAGE, message), key, gameId);

		}
	}

	public void addEventEndGame(Integer gameId, Integer currentPlayers) throws InterruptedException {
		String event = "END-GAME";
		for (int i = 1; i <= currentPlayers; i++) {
			String key = gameId + "player" + i;
			addEvent(event, key, gameId);
		}

	}

	public void addEventLeaveGameBy(Integer gameID, Integer player, Integer currentPlayers)
			throws InterruptedException {
		Set<String> alreadyLeaved = leaved.get(gameID);

		if (currentPlayers == 2 || (alreadyLeaved != null && (alreadyLeaved.size() - currentPlayers) == 2))
			for (int i = 1; i <= currentPlayers; i++) {
				String key = gameID + "player" + i;
				if (i != player) {
					System.out.println("Send end game for" + gameID + "player" + i);

					addEvent("END-GAME", key, gameID);
				}
			}
		else
			addLeaved(gameID, player);
	}

	private void addLeaved(Integer gameID, Integer player) {

		if (!leaved.containsKey(gameID))
			leaved.put(gameID, new HashSet<>());
		leaved.get(gameID).add(gameID + "player" + player);
	}

	////////////////////////// EVENT BEFORE GAME/////////////////////////
	public void addEventStartGame(String lobby_name) throws InterruptedException {
		String key = lobby_name.toLowerCase() + "player2";
		addGeneralEventLobby(key, MessageMaker.START_MESSAGE);
		addGeneralEventLobby(key, MessageMaker.START_MESSAGE);
	}

	public void addEventJoin(String lobbyName, String username) throws InterruptedException {
		addGeneralEventLobby(lobbyName.toLowerCase(), MessageMaker.JOIN_MESSAGE, MessageMaker.WHO_JOIN, username);
	}

	public void addEventLeaveJoin(String previousJoined, String username) throws InterruptedException {
		Pattern p = Pattern.compile("(.+)player2$");// TODO transorm to N
		Matcher meMatcher = p.matcher(previousJoined);
		if (meMatcher.matches()) {
			String s = meMatcher.group(1);
			System.out.println("GROUP1: " + s);
			addGeneralEventLobby(s, MessageMaker.LEAVE_MESSAGE, MessageMaker.WHO_LEAVE, MessageMaker.OWNER);
			if (join.containsKey(previousJoined))
				addGeneralEventLobby(previousJoined, MessageMaker.LEAVE_MESSAGE);// TODO FIND A WAY TO DELETE THIS KEY,
			// rimane sporca la chiave lobby+player2 perchè come riceve la risposta inizia
			// ad ascoltare sulla chiave lobby name.
//			join.remove(s);
		} else {
			System.out.println("A JOINER has leave");
			detachListenerForStart(previousJoined, username, 2);// TODO REMOVE STATIC 2
			addGeneralEventLobby(previousJoined, MessageMaker.LEAVE_MESSAGE);

		}
	}

	public void attachListenerToJoin(String lobby_name, String username) {

		if (join.containsKey(lobby_name))
			/* throw new RuntimeException */System.out
					.println("This lobby already has a listener attached" + lobby_name);
		else {
			System.out.println("listener attached for lobby" + lobby_name);
			join.put(lobby_name, new LinkedBlockingQueue<>());
			listeningFor.put(username,
					maker.makeMessage(MessageMaker.JOIN_MESSAGE, MessageMaker.LOBBY_NAME, lobby_name));
			System.out.println(join.containsKey(lobby_name));
		}
	}

	public void attachListenerToStart(String lobby_name, String username) {
		String key = lobby_name.toLowerCase() + "player2";

		if (join.containsKey(key))
			/* throw new RuntimeException */System.out
					.println("This lobby already has a listener attached" + lobby_name);
		else {
			System.out.println("listener attached for start" + lobby_name);
			join.put(key, new LinkedBlockingQueue<>());
			listeningFor.put(username,
					maker.makeMessage(MessageMaker.START_MESSAGE, MessageMaker.LOBBY_NAME, lobby_name));
			System.out.println(join.containsKey(key));
		}
	}

	public void detachListenerForJoin(String lobby_name, String username) {
		join.remove(lobby_name.toLowerCase());
		listeningFor.remove(username);
	}

	public void detachListenerForStart(String lobby_name, String username, Integer player) {
		System.out.println("detach listener for start" + lobby_name + "username" + username + player);
		join.remove(lobby_name.toLowerCase() + "player" + player);
		listeningFor.remove(username);
	}

	public void detachListenerInGame(Integer gameId, Integer player) {
		String key = gameId + "player" + player;
		events.remove(key);
	}

	public void detachListenerForStart(String username, Integer player) {
		String listenFor = listeningFor.get(username);
		if (listenFor != null) {
			JSONObject jo = new JSONObject(listenFor);
			detachListenerForStart(jo.getString(MessageMaker.LOBBY_NAME), username, player);
		}
	}

	/////////////////////////////////// GET
	/////////////////////////////////// EVENTS//////////////////////////////////////////////
	public String nextGameEventFor(Integer gameId, Integer player) throws InterruptedException {
		String key = gameId + "player" + player;
		if (!events.containsKey(key))
			events.put(key, new LinkedBlockingQueue<>());
		return events.get(key).poll(29, TimeUnit.SECONDS);
	}

	public String getEventJoin(String lobbyName) throws InterruptedException {
		lobbyName = lobbyName.toLowerCase();
		if (!join.containsKey(lobbyName)) {
			// TODO THROW EXCEPTION
			join.put(lobbyName, new LinkedBlockingQueue<>());

			System.out.println("No listener attached for this lobby" + lobbyName);
//			return null;
		}

		return join.get(lobbyName).poll(29, TimeUnit.SECONDS);
	}

	public String getEventStartGame(String lobbyName) throws InterruptedException {
		String key = lobbyName.toLowerCase() + "player2";
		if (!join.containsKey(key)) {
			join.put(key, new LinkedBlockingQueue<>());

			System.out.println("No listener attached for this lobby" + lobbyName);
//			return null;
		}
		return join.get(key).poll(29, TimeUnit.SECONDS);
	}

	public String getListenerOfUser(String username) {
		System.out.println("checkFor" + username + " listener " + listeningFor.get(username));
		return listeningFor.get(username);
	}

	private class MessageMaker {
		public static final String LOBBY_NAME = "lobby_name";
		public static final String OWNER = "owner";
		public static final String WHO_LEAVE = "by";
		public final static String JOIN_MESSAGE = "join";
		public final static String START_MESSAGE = "start";
		public final static String LEAVE_MESSAGE = "leave";
		public final static String CHAT_MESSAGE = "message";
		public final static String UPDATE_MESSAGE = "update";
		public final static String PROGRESS_MESSAGE = "progress";
		public final static String TEXT_MESSAGE = "message_text";
		public final static String WHO_JOIN = "joiner";

		public String makeMessage(String message_type) {
			return (new JSONObject().put(message_type, true)).toString();
		}

		public String makeMessage(String message_type, String message, String message_content) {
			return (new JSONObject().put(message_type, true).put(message, message_content)).toString();
		}

	}

}
