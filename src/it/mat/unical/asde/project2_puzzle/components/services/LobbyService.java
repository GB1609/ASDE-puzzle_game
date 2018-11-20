package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import it.mat.unical.asde.project2_puzzle.model.Lobby;

@Service
public class LobbyService{
    List<Lobby> lobbies;

    public boolean addLobby(Lobby newLobby){
        // TODO can't add lobby with name with spaces
        boolean added = false;
        if (!lobbies.contains(newLobby)) {
            lobbies.add(0, newLobby);
            added = true;
        }
        return added;
    }

    public List<Lobby> getLobbies(){
        return lobbies;
    }

    // Return the lobby with the given name, null otherwise
    public Lobby getLobbyByName(String name){
        for (int i = 0; i < lobbies.size(); i++) {
            Lobby lobby = lobbies.get(i);
            if (lobby.getName().equals(name)) {
                putLobbyOnTop(i);
                return lobby;
            }
        }
        return null;
    }

    public Lobby getLobbyByUsername(String username){
        for (int i = 0; i < lobbies.size(); i++) {
            Lobby lobby = lobbies.get(i);
            if (lobby.getOwner().equals(username)) {
                putLobbyOnTop(i);
                return lobby;
            }
        }
        return null;
    }

    @PostConstruct
    public void init(){
        lobbies = new LinkedList<>();
        for (int i = 0; i <= 5; i++) {
            lobbies.add(new Lobby("Lobby" + (i + 1), "user" + (int) (Math.random() * 100), ""));
        }
    }

    public void joinToLobby(String lobby_name,String username){
        leaveIfInOtherLobby(username);
        Lobby lobbyToJoin = getLobbyByName(lobby_name);
        lobbyToJoin.setGuest(username);
    }

    // user with "username" leave the lobby where it is
    public void leaveIfInOtherLobby(String username){
        System.out.println("LEAVE if in other lobby");
        for (int i = 0; i < lobbies.size(); i++) {
            Lobby lobby = lobbies.get(i);
            String guest = lobby.getGuest();
            if (lobby.getOwner().equals(username)) {
                if (guest != null) {
                    lobby.setOwner(guest);
                    lobby.setGuest(null);
                    System.out.println("LEAVE LOBBY: " + lobby);
                } else {
                    lobbies.remove(i);
                    System.out.println("REMOVE LOBBY: " + lobby);
                }
            } else if (guest != null) {
                if (guest.equals(username)) {
                    System.out.println("LEAVE LOBBY: " + lobby);
                    lobby.setGuest(null);
                }
            }
        }
    }

    public void putLobbyOnTop(int index){
        Collections.rotate(lobbies.subList(0, index + 1), 1);
    }

    public boolean removeLobby(Lobby lobby){
        return lobbies.remove(lobby);
    }

    public boolean removeLobbyByName(String lobby_name){
        for (Lobby lobby : lobbies) {
            if (lobby.getName().equals(lobby_name)) {
                return lobbies.remove(lobby);
            }
        }
        return false;
    }
}
