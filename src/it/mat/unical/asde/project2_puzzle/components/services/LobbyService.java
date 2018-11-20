package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import it.mat.unical.asde.project2_puzzle.model.Lobby;

@Service
public class LobbyService {

	List<Lobby> lobbies;
	
	@PostConstruct
	public void init() {
		lobbies = new LinkedList<Lobby>();
		for(int i = 0; i <= 5; i++) {
			lobbies.add(new Lobby("Lobby"+(i+1),"user "+(int)(Math.random()*100),""));
		}
	}

	public List<Lobby> getLobbies() {
		return lobbies;
	}
	
	public boolean addLobby(Lobby newLobby) {
		//TODO can't add lobby with name with spaces
		boolean added = false;
		if(!lobbies.contains(newLobby)) {
			lobbies.add(0, newLobby);
			added = true;
		}
		return added;
	}

	//Return the lobby with the given name, null otherwise
	public Lobby getLobbyByName(String name) {
		for (int i = 0; i < lobbies.size(); i++) {
			Lobby lobby = lobbies.get(i);
			if(lobby.getName().equals(name)){
				putLobbyOnTop(i);
				return lobby;
			}
		}
		return null;
	}
	
	public void putLobbyOnTop(int index) {
		Collections.rotate(lobbies.subList(0, index+1), 1);
	}
}
