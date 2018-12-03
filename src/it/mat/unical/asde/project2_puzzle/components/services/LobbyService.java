package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.mat.unical.asde.project2_puzzle.components.persistence.UserDAO;
import it.mat.unical.asde.project2_puzzle.components.services.utility.MessageMaker;
import it.mat.unical.asde.project2_puzzle.model.Lobby;
import it.mat.unical.asde.project2_puzzle.model.services_utility.PlayerType;
import it.mat.unical.asde.project2_puzzle.model.services_utility.SearchBy;

@Service
public class LobbyService {
	private List<Lobby> lobbies;
	private HashMap<String, Lobby> lobbyMapping;
	HashMap<String, String> previousLobby;
	HashMap<String, String> avatars;
	@Autowired
	UserDAO userDao;

	@PostConstruct
	public void init() {
		this.lobbies = new LinkedList<>();
		this.previousLobby = new HashMap<>();
		avatars = new HashMap<>();
		lobbyMapping = new HashMap<>();
	}

	public List<Lobby> getLobbies() {
		return this.lobbies;
	}
	
	
	private void updateAvatarsMap(Lobby lobby)
	{
		System.out.println("UPDATE AVATARS :"+ lobby.toString());
		String owner = lobby.getOwner();
		String guest = lobby.getGuest();
		if (!avatars.containsKey(owner) && !owner.equals(""))
			avatars.put(owner, userDao.getUser(owner).getAvatar());
		if (!avatars.containsKey(guest) && !guest.equals(""))
			avatars.put(guest, userDao.getUser(guest).getAvatar());
	}
	
	public Integer joinToLobby(String lobby_name, String username) {
		lobby_name = lobby_name.toLowerCase();
		this.leaveIfInOtherLobby(username);
		Lobby lobbyToJoin = lobbyMapping.get(lobby_name);
		if (lobbyToJoin.getGuest().isEmpty()) {
			lobbyToJoin.setGuest(username);
			System.out.println("join to lobby: " + lobbyMapping.get(lobby_name).getGuest());
			updateAvatarsMap(lobbyToJoin);
			return lobbyToJoin.getLobbyID();
		}
		return -1;
	}

	// user with "username" leave the lobby where it is
	public void leaveIfInOtherLobby(String username) {
		for (Lobby lobby : this.lobbies) {
			if (this.leaveIfInLobby(lobby, username)) {
				return;
			}
		}
	}

	public boolean leaveLobby(String username, String lobbyname) {
		return this.leaveIfInLobby(lobbyMapping.get(lobbyname), username);
	}

	public boolean leaveIfInLobby(Lobby lobby, String username) {
		if (lobby != null) {
			
			String owner = lobby.getOwner();
			String guest = lobby.getGuest();
			if (owner.equals(username)) {
				avatars.remove(username);
				if (!guest.isEmpty()) {
					lobby.setOwner(guest);
					lobby.setGuest("");
					System.out.println("LEAVE LOBBY: " + lobby);
					this.previousLobby.put(username, lobby.getName() + "player2");
					return true;
				} else {
					this.previousLobby.put(username, lobby.getName() + "player2");
					System.out.println("REMOVE LOBBYA: " + lobby);
					this.lobbies.remove(lobby);
					lobbyMapping.remove(lobby.getName());
					return true;
				}
			} else if (!guest.isEmpty()) {
				if (guest.equals(username)) {
					avatars.remove(username);
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
		if (!j.has("leave"))
			return;
		Lobby lobby = lobbyMapping.get(lobby_name);
		if (j.has(MessageMaker.FOR_CLEANING) && Boolean.parseBoolean(j.getString(MessageMaker.FOR_CLEANING))) {
			String guest = lobby.getGuest();
			System.out.println("GUEST: ->" + guest);
			if (!guest.isEmpty()) {
				lobby.setOwner(guest);
				lobby.setGuest("");
			} else {
				System.out.println("DELETE LOBY " + lobby_name + "lobby owner" + lobby.getOwner());
				this.lobbies.remove(lobbyMapping.remove(lobby_name));
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
		return this.lobbies.remove(lobbyMapping.get(lobby_name));
	}

	public Integer destructLobby(String lobby_name) {
		lobby_name = lobby_name.toLowerCase();
		Lobby l = lobbyMapping.remove(lobby_name);
		Integer lobbyId = -1;
		if (l != null) {
			lobbyId = l.getLobbyID();
			this.lobbies.remove(l);
		}
		return lobbyId;
	}

	public boolean addLobby(Lobby newLobby, String username) {
		this.leaveIfInOtherLobby(username);
		// TODO can't add lobby with name with spaces
		boolean added = false;
		if (!this.lobbyMapping.containsKey(newLobby.getName())) {
			this.lobbies.add(newLobby);

			this.lobbyMapping.put(newLobby.getName(), newLobby);
			added = true;
			updateAvatarsMap(newLobby);
		}
		return added;
	}

	// Return the lobby with the given name, null otherwise
	public Lobby getLobby(String name, SearchBy typeOfSearch) {
		if (typeOfSearch.equals(SearchBy.LOBBY_NAME))
			return lobbyMapping.get(name);
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

	public boolean hasTheListChanges(String lobbies) {
		JSONArray actual = new JSONArray(this.lobbies);
		return !actual.toString().equals(lobbies);
	}

	public String getLobbiesOrRefresh(String username, String lobbies, int currently_showed) {
		JSONObject response = new JSONObject().put("error", false);
		List<Lobby> l = getNextMLobbies(currently_showed, 20);
		response.put("lobbies_to_add", l);

		JSONArray avatarsJA = new JSONArray();
		for (String key : avatars.keySet()) {
			avatarsJA.put(new JSONObject().put("user", key).put("avatar", avatars.get(key)));
		}
		System.out.println("AVATARS: " + avatarsJA);
		response.put("avatars", avatarsJA);
		if (hasTheListChanges(lobbies)) {
			response.put("lobbies_changed", true);
			return response.put("lobbies", getLobbies()).put("lobbies_owner", getLobbiesBy(username, PlayerType.OWNER))
					.put("lobbies_guest", getLobbiesBy(username, PlayerType.GUEST)).put("username", username)
					.toString();
		}
		response.put("lobbies_changed", false);
		return response.put("username", username).toString();
	}
}
