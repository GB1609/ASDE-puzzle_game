package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import it.mat.unical.asde.project2_puzzle.model.Lobby;

@Service
public class LobbyService{
    private List<Lobby> lobbies;
    private int currentlyShowed;
    HashMap<String, String> previousLobby;

    @PostConstruct
    public void init(){
        this.currentlyShowed = 0;
        this.lobbies = new LinkedList<>();
        this.previousLobby = new HashMap<>();
        for (int i = 0; i <= 100; i++) {
            this.lobbies.add(new Lobby("Lobby" + (i + 1), "user" + (int) (Math.random() * 100), ""));
        }
    }

    public List<Lobby> getLobbies(){
        return this.lobbies;
    }

    public Integer joinToLobby(String lobby_name,String username){
        this.leaveIfInOtherLobby(username);
        Lobby lobbyToJoin = this.getLobby(lobby_name, SearchBy.LOBBY_NAME);
        if (lobbyToJoin.getGuest().isEmpty()) {
            lobbyToJoin.setGuest(username);
            // System.out.println("join to lobby: " + lobbyToJoin);
            return lobbyToJoin.getLobbyID();
        }
        return -1;
    }

    // user with "username" leave the lobby where it is
    public void leaveIfInOtherLobby(String username){
        for (int i = 0; i < this.lobbies.size(); i++) {
            Lobby lobby = this.lobbies.get(i);
            String guest = lobby.getGuest();
            if (lobby.getOwner().equals(username)) {
                if (!guest.isEmpty()) {
                    lobby.setOwner(guest);
                    lobby.setGuest("");
                    System.out.println("LEAVE LOBBY: " + lobby);
                    previousLobby.put(username, lobby.getName());
                } else {
                    this.lobbies.remove(i);
                    System.out.println("REMOVE LOBBY: " + lobby);
                    previousLobby.put(username, lobby.getName() + "player2");
                }
            } else if (!guest.isEmpty()) {
                if (guest.equals(username)) {
                    System.out.println("LEAVE LOBBY: " + lobby);
                    lobby.setGuest("");
                    previousLobby.put(username, lobby.getName());
                }
            }
        }
    }

    public String checkPreviousLobby(String username){
        return previousLobby.remove(username);
    }

    public boolean removeLobby(Lobby lobby){
        return this.lobbies.remove(lobby);
    }

    public boolean removeLobbyByName(String lobby_name){
        for (Lobby lobby : this.lobbies) {
            if (lobby.getName().equals(lobby_name)) {
                return this.lobbies.remove(lobby);
            }
        }
        return false;
    }

    public Integer destrucLobby(String lobby_name){
        for (Lobby lobby : this.lobbies) {
            if (lobby.getName().equals(lobby_name)) {
                Integer lobbyId = lobby.getLobbyID();
                this.lobbies.remove(lobby);
                return lobbyId;
            }
        }
        return -1;
    }

    public boolean addLobby(Lobby newLobby){
        // TODO can't add lobby with name with spaces
        boolean added = false;
        if (!this.lobbies.contains(newLobby)) {
            this.lobbies.add(newLobby);
            added = true;
        }
        return added;
    }

    // Return the lobby with the given name, null otherwise
    public Lobby getLobby(String name,SearchBy typeOfSearch){
        for (int i = 0; i < this.lobbies.size(); i++) {
            Lobby lobby = this.lobbies.get(i);
            boolean condition = false;
            if (typeOfSearch.equals(SearchBy.LOBBY_NAME)) {
                condition = lobby.getName().equals(name);
            } else if (typeOfSearch.equals(SearchBy.USERNAME)) {
                condition = lobby.getOwner().equals(name) || lobby.getGuest().equals(name);
            }
            if (condition) {
                return lobby;
            }
        }
        return null;
    }

    public List<Lobby> getNextMLobbies(int m){
        List<Lobby> fromTo = new LinkedList<>();
        int toIndex = this.currentlyShowed + m;
        try {
            // System.out.println("TRY get from:" + this.currentlyShowed + " to:" +
            // toIndex);
            fromTo = this.lobbies.subList(this.currentlyShowed, toIndex);
            this.currentlyShowed += m;
        } catch (IndexOutOfBoundsException e) {
//			if (this.currentlyShowed < 0) {
//				this.currentlyShowed = 0;
//			} else if (this.currentlyShowed > this.lobbies.size()) {
//				this.currentlyShowed = this.lobbies.size();
//			}
//			if (toIndex < this.currentlyShowed) {
//				toIndex = this.currentlyShowed;
//			} else if (toIndex > this.lobbies.size()) {
//				toIndex = this.lobbies.size();
//			}
//			fromTo = this.lobbies.subList(this.currentlyShowed, toIndex);
            // System.out.println("CATCH get from:" + this.currentlyShowed + " to:" +
            // toIndex);
        }
        return fromTo;
    }

    public void resetCurrentlyShowed(){
        this.currentlyShowed = 0;
    }

    public List<Lobby> getLobbiesBy(String username,PlayerType p){
        // System.out.println("LOBBIES by:" + p);
        List<Lobby> tmp = new LinkedList<>();
        for (Lobby lobby : this.lobbies) {
            // System.out.println("LOBBY " + lobby);
            if (p.equals(PlayerType.OWNER) && lobby.getOwner().equals(username)) {
                tmp.add(lobby);
            } else if (p.equals(PlayerType.GUEST) && lobby.getGuest().equals(username)) {
                tmp.add(lobby);
            }
        }
        return tmp;
    }
}
