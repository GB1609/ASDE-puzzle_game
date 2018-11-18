package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import it.mat.unical.asde.project2_puzzle.model.Lobby;

@Service
public class LobbyService {

	Set<Lobby> lobbies;
	
	@PostConstruct
	public void init() {
		lobbies = new HashSet<Lobby>();
		/*for(int i = 0; i <= 15; i++) {
			lobbies.add(new Lobby("Lobby numero "+(i+1),"user "+(int)(Math.random()*100),""));
		}*/
	}

	public Set<Lobby> getLobbies() {
		return lobbies;
	}
	
	public boolean addLobby(Lobby newLobby) {
		return lobbies.add(newLobby);
	}

	//Return the lobby with the given name, null otherwise
	public Lobby findLobbyByName(String name) {
		for (Lobby lobby : lobbies) {
			if(lobby.getName().equals(name)){
				return lobby;
			}
		}
		return null;
	}
}
