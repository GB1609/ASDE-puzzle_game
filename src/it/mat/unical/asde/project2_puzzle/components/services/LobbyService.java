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
		lobbies = new LinkedList<Lobby>();
		for(int i = 0; i <= 15; i++) {
			lobbies.add(new Lobby("Lobby numero "+(i+1),"user "+(int)(Math.random()*100),""));
		}
	}

	public List<Lobby> getLobbies() {
		return lobbies;
	}

}
