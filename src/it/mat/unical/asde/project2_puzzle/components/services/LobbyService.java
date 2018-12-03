package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import it.mat.unical.asde.project2_puzzle.components.services.utility.MessageMaker;
import it.mat.unical.asde.project2_puzzle.model.Lobby;
import it.mat.unical.asde.project2_puzzle.model.services_utility.PlayerType;
import it.mat.unical.asde.project2_puzzle.model.services_utility.SearchBy;

@Service
public class LobbyService {
	private List<Lobby> lobbies;
	// mapping to keep mapping from user to lobby
	private HashMap<String, Lobby> lobbyMapping;
	HashMap<String, String> previousLobby;

	@PostConstruct
	public void init() {
		this.lobbies = new LinkedList<>();
		this.previousLobby = new HashMap<>();
		this.lobbyMapping = new HashMap<>();
	}

	public List<Lobby> getLobbies() {
		return this.lobbies;
	}

	public Integer joinToLobby(String lobby_name, String username) {
		lobby_name = lobby_name.toLowerCase();
		this.leaveIfInOtherLobby(username);
		Lobby lobbyToJoin = this.lobbyMapping.get(lobby_name);
		if (lobbyToJoin.getGuest().isEmpty()) {
			lobbyToJoin.setGuest(username);
			System.out.println("join to lobby: " + this.lobbyMapping.get(lobby_name).getGuest());
			return lobbyToJoin.getLobbyID();
		}
		return -1;
	}

	public void leaveIfInOtherLobby(String username) {
		for (Lobby lobby : this.lobbies) {
			if (this.leaveIfInLobby(lobby, username)) {
				return;
			}
		}
	}

	public boolean leaveLobby(String username, String lobbyname) {
		return this.leaveIfInLobby(this.lobbyMapping.get(lobbyname), username);
	}

	// User with name "username" leave the lobby (if he is in a lobby)
	public boolean leaveIfInLobby(Lobby lobby, String username) {
		if (lobby != null) {
			String owner = lobby.getOwner();
			String guest = lobby.getGuest();
			if (owner.equals(username)) { // if username is the owner and leave the lobby
				if (!guest.isEmpty()) { // guest become owner
					lobby.setOwner(guest);
					lobby.setGuest("");
					System.out.println("LEAVE LOBBY: " + lobby);
					this.previousLobby.put(username, lobby.getName() + "player2");
					return true;
				} else { // if owner is the only in the lobby, he leave it and lobby will be removed
					this.previousLobby.put(username, lobby.getName() + "player2");
					System.out.println("REMOVE LOBBYA: " + lobby);
					this.lobbies.remove(lobby);
					return true;
				}
			} else if (!guest.isEmpty()) { // if username is the guest, he simply leave the lobby
				if (guest.equals(username)) {
					System.out.println("LEAVE LOBBY: " + lobby);
					lobby.setGuest("");
					this.previousLobby.put(username, lobby.getName());
					return true;
				}
			}
		}
		return false;
	}

	public void cleanIfOffline(String event, String lobby_name) {
		JSONObject j = new JSONObject(event);
		System.out.println("IN CLEAR IF OFFLINE" + event + " lobby  " + lobby_name + " Leave by owner "
				+ j.has(MessageMaker.FOR_CLEANING));
		if (!j.has("leave")) {
			return;
		}
		Lobby lobby = this.lobbyMapping.get(lobby_name);
		if (j.has(MessageMaker.FOR_CLEANING) && Boolean.parseBoolean(j.getString(MessageMaker.FOR_CLEANING))) {
			String guest = lobby.getGuest();
			System.out.println("GUEST: ->" + guest);
			if (!guest.isEmpty()) {
				lobby.setOwner(guest);
				lobby.setGuest("");
			} else {
				System.out.println("DELETE LOBY " + lobby_name + "lobby owner" + lobby.getOwner());
				this.lobbies.remove(this.lobbyMapping.remove(lobby_name));
			}
		} else {
			lobby.setGuest("");
		}

	}

	public String checkPreviousLobby(String username) {
		return this.previousLobby.remove(username);
	}

	public boolean removeLobbyByName(String lobby_name) {
		lobby_name = lobby_name.toLowerCase();
		return this.lobbies.remove(this.lobbyMapping.get(lobby_name));
	}

	public Integer destructLobby(String lobby_name) {
		lobby_name = lobby_name.toLowerCase();
		Lobby l = this.lobbyMapping.remove(lobby_name);
		Integer lobbyId = -1;
		if (l != null) {
			lobbyId = l.getLobbyID();
			this.lobbies.remove(l);
		}
		return lobbyId;
	}

	// add lobby "newLobby" created by "username". If "username" is in another
	// lobby, he leave it
	public boolean addLobby(Lobby newLobby, String username) {
		this.leaveIfInOtherLobby(username);
		boolean added = false;

		if (!this.lobbyMapping.containsKey(newLobby.getName())) {
			this.lobbies.add(newLobby);
			this.lobbyMapping.put(newLobby.getName(), newLobby);
			added = true;
		}

		return added;
	}

	// Return the lobby with the given name, null otherwise
	public Lobby getLobby(String name, SearchBy typeOfSearch) {
		if (typeOfSearch.equals(SearchBy.LOBBY_NAME)) {
			return this.lobbyMapping.get(name);
		}
		for (int i = 0; i < this.lobbies.size(); i++) {
			Lobby lobby = this.lobbies.get(i);
			boolean condition = false;
			if (typeOfSearch.equals(SearchBy.USERNAME)) {
				condition = lobby.getOwner().equals(name) || lobby.getGuest().equals(name);
			}
			if (condition) {
				return lobby;
			}
		}
		return null;
	}

	// this function is called to apply some kind of lazy load of lobbies.
	// It returns the "m" next lobbies starting from index "currentlyShowed"
	public List<Lobby> getNextMLobbies(int currentlyShowed, int m) {
		List<Lobby> fromTo = new LinkedList<>();
		int toIndex = currentlyShowed + m;
		try {
			fromTo = this.lobbies.subList(currentlyShowed, toIndex);
			currentlyShowed += m;
		} catch (IndexOutOfBoundsException e) {
			if (currentlyShowed < 0) {
				currentlyShowed = 0;
			} else if (currentlyShowed > this.lobbies.size()) {
				currentlyShowed = this.lobbies.size();
			}
			if (toIndex < currentlyShowed) {
				toIndex = currentlyShowed;
			} else if (toIndex > this.lobbies.size()) {
				toIndex = this.lobbies.size();
			}
			fromTo = this.lobbies.subList(currentlyShowed, toIndex);
			currentlyShowed += toIndex - currentlyShowed;
		}
		return fromTo;
	}

	public List<Lobby> getLobbiesBy(String username, PlayerType p) {
		List<Lobby> tmp = new LinkedList<>();
		for (Lobby lobby : this.lobbies) {
			if (p.equals(PlayerType.OWNER) && lobby.getOwner().equals(username)) {
				tmp.add(lobby);
			} else if (p.equals(PlayerType.GUEST) && lobby.getGuest().equals(username)) {
				tmp.add(lobby);
			}
		}
		return tmp;
	}

	// this function is called when client ask for getting lobbies.
	// return "true" if lobbies on server are different from those of the client,
	// "false" otherwise
	public boolean hasTheListChanges(String lobbies) {
		JSONArray actual = new JSONArray(this.lobbies);
		boolean changed = actual.toString().equals(lobbies);
		System.out.println("CHANGED:" + !changed + "_old:" + lobbies + "___actual:" + actual);
		return !changed;
	}
}
