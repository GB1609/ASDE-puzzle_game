package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import it.mat.unical.asde.project2_puzzle.model.Lobby;

@Service
public class LobbyService {
	List<Lobby> lobbies;

	@PostConstruct
	public void init() {
		this.lobbies = new LinkedList<>();
		for (int i = 0; i <= 5; i++) {
			this.lobbies.add(new Lobby("Lobby" + (i + 1), "user" + (int) (Math.random() * 100), ""));
		}
	}

	public List<Lobby> getLobbies() {
		return this.lobbies;
	}

	public void joinToLobby(String lobby_name, String username) {
		this.leaveIfInOtherLobby(username);
		Lobby lobbyToJoin = this.getLobby(lobby_name, SearchBy.LOBBY_NAME);
		lobbyToJoin.setGuest(username);
		// System.out.println("join to lobby: " + lobbyToJoin);
	}

	// user with "username" leave the lobby where it is
	public void leaveIfInOtherLobby(String username) {
		// System.out.println("LEAVE if in other lobby: " + username);
		for (int i = 0; i < this.lobbies.size(); i++) {
			Lobby lobby = this.lobbies.get(i);
			String guest = lobby.getGuest();
			if (lobby.getOwner().equals(username)) {
				if (!guest.isEmpty()) {
					lobby.setOwner(guest);
					lobby.setGuest("");
					System.out.println("LEAVE LOBBY: " + lobby);
				} else {
					this.lobbies.remove(i);
					System.out.println("REMOVE LOBBY: " + lobby);
				}
			} else if (!guest.isEmpty()) {
				if (guest.equals(username)) {
					System.out.println("LEAVE LOBBY: " + lobby);
					lobby.setGuest("");
				}
			}
		}
	}

	public boolean removeLobby(Lobby lobby) {
		return this.lobbies.remove(lobby);
	}

	public boolean removeLobbyByName(String lobby_name) {
		for (Lobby lobby : this.lobbies) {
			if (lobby.getName().equals(lobby_name)) {
				return this.lobbies.remove(lobby);
			}
		}
		return false;
	}

	public boolean addLobby(Lobby newLobby) {
		// TODO can't add lobby with name with spaces
		boolean added = false;
		if (!this.lobbies.contains(newLobby)) {
			this.lobbies.add(0, newLobby);
			added = true;
		}
		return added;
	}

	// Return the lobby with the given name, null otherwise
	public Lobby getLobby(String name, SearchBy typeOfSearch) {
		for (int i = 0; i < this.lobbies.size(); i++) {
			Lobby lobby = this.lobbies.get(i);
			boolean condition = false;
			if (typeOfSearch.equals(SearchBy.LOBBY_NAME)) {
				condition = lobby.getName().equals(name);
			} else if (typeOfSearch.equals(SearchBy.USERNAME)) {
				condition = lobby.getOwner().equals(name);
			}
			if (condition) {
				// this.putLobbyOnTop(i);
				return lobby;
			}
		}
		return null;
	}
}
