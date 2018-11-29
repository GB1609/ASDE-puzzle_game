package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.HashMap;
import java.util.Map;
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
	private MessageMaker maker = new MessageMaker();

	/////////////////////////////////// ADD
	/////////////////////////////////// EVENTS//////////////////////////////////////////////
	private void addEvent(String progress, String key) throws InterruptedException {
		if (!events.containsKey(key))
			events.put(key, new LinkedBlockingQueue<>());
		events.get(key).put(progress);
	}

	private void addGeneralEventLobby(String key, String message_type) throws InterruptedException {
		if (!join.containsKey(key))
			throw new RuntimeException("No join found for this lobby");
		join.get(key).put(maker.makeMessage(message_type));
	}

	private void addGeneralEventLobby(String key, String message_type, String message, String message_content)
			throws InterruptedException {
		if (!join.containsKey(key))
			throw new RuntimeException("No join found for this lobby");
		join.get(key).put(maker.makeMessage(message_type, message, message_content));
	}

	////////////////////////// EVENT IN GAME/////////////////////////
	public void addEventFor(Integer gameID, String player, String progress) throws InterruptedException {
		String key = gameID + (player.equals("player1") ? "player2" : "player1");
		addEvent(maker.makeMessage(MessageMaker.UPDATE_MESSAGE, MessageMaker.PROGRESS_MESSAGE, progress), key);
	}

	public void addMessageFor(Integer gameId, String player, String message) throws InterruptedException {
		String key = gameId + (player.equals("player1") ? "player2" : "player1");
		addEvent(maker.makeMessage(MessageMaker.CHAT_MESSAGE, MessageMaker.TEXT_MESSAGE, message), key);
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

	////////////////////////// EVENT BEFORE GAME/////////////////////////
	public void addEventStartGame(String lobby_name) throws InterruptedException {
		String key = lobby_name.toLowerCase() + "player2";
		addGeneralEventLobby(key, MessageMaker.START_MESSAGE);
	}

	public void addEventJoin(String lobbyName, String username) throws InterruptedException {
		addGeneralEventLobby(lobbyName.toLowerCase(), MessageMaker.JOIN_MESSAGE, MessageMaker.WHO_JOIN, username);
	}

	public void addEventLeaveJoin(String previousJoined) throws InterruptedException {
		Pattern p = Pattern.compile("(.+)player2$");
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
			join.remove(previousJoined + "player2");
			addGeneralEventLobby(previousJoined, MessageMaker.LEAVE_MESSAGE);

		}
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

	public void attachListenerToStart(String lobby_name) {
		String key = lobby_name.toLowerCase() + "player2";

		if (join.containsKey(key))
			/* throw new RuntimeException */System.out
					.println("This lobby already has a listener attached" + lobby_name);
		else {
			System.out.println("listener attached for start" + lobby_name);
			join.put(key, new LinkedBlockingQueue<>());
			System.out.println(join.containsKey(key));
		}
	}

	public void detachListenerForJoin(String lobby_name) {
		join.remove(lobby_name.toLowerCase());
	}

	public void detachListenerInGame(Integer gameId, String player) {
		String key = gameId + player;
		events.remove(key);
	}

	/////////////////////////////////// GET
	/////////////////////////////////// EVENTS//////////////////////////////////////////////
	public String nextGameEventFor(Integer gameId, String player) throws InterruptedException {
		String key = gameId + player;
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
		String b = join.get(lobbyName).poll(29, TimeUnit.SECONDS);
		if (b != null) {// TODO problem, now this listener is used also for the leave join, so find
						// another solution for this. Now when a player leave the lobby already joined
						// is put
			if (b.equals("already-joined"))
				join.remove(lobbyName);
			else
				join.get(lobbyName).put("already-joined");
		}
		return b;
	}

	public String getEventStartGame(String lobbyName) throws InterruptedException {
		String key = lobbyName.toLowerCase() + "player2";
		if (!join.containsKey(key)) {
			join.put(key, new LinkedBlockingQueue<>());

			System.out.println("No listener attached for this lobby" + lobbyName);
//			return null;
		}
		String b = join.get(key).poll(29, TimeUnit.SECONDS);
		if (b != null) {
			// only for reload problem
			if (b.equals("already-started"))
				join.remove(key);
			else
				join.get(key).put("already-started");
		}
		return b;
	}

	private class MessageMaker {
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
