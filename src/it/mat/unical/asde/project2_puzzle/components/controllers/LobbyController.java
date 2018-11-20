package it.mat.unical.asde.project2_puzzle.components.controllers;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import it.mat.unical.asde.project2_puzzle.components.services.LobbyService;
import it.mat.unical.asde.project2_puzzle.model.Lobby;

@Controller
public class LobbyController{
    @Autowired
    LobbyService lobbyService;

    @GetMapping("lobby")
    public String showLobbies(Model model){
        model.addAttribute("lobbies", lobbyService.getLobbies());
        return "lobby";
    }

    @GetMapping("create_lobby")
    public String createLobby(Model model,HttpSession session,@RequestParam String lobby_name){
        Lobby newLobby = new Lobby(lobby_name, (String) session.getAttribute("username"));
        if (!lobbyService.addLobby(newLobby)) {
            System.out.println(("ADD LOBBY: false || info: " + newLobby));
            model.addAttribute("error", "Lobby with this name already exists");
            // Can we avoid to use this line? redundant with that in the "showLobbies" method.
            model.addAttribute("lobbies", lobbyService.getLobbies());
            return "lobby";
        }
        System.out.println(("ADD LOBBY: true || info: " + newLobby));
        return "redirect:/";
    }

    @GetMapping("search_lobby_by_name")
    public String searchLobby(Model model,HttpSession session,@RequestParam String lobby_name){
        Lobby newLobby = lobbyService.getLobbyByName(lobby_name);
        System.out.println("Search lobby with name: " + lobby_name + ".");
        if (newLobby != null) {
            // lobby with name "lobby_name" founded
            System.out.println("Lobby found: " + newLobby);
            return "redirect:/";
        }
        // No lobby found with given name
        System.out.println("Lobby NOT found !!!");
        model.addAttribute("error_lobby_search", "Lobby with name " + lobby_name + " not found");
        return "lobby";
    }

    @PostMapping("join_lobby")
    @ResponseBody
    public void joinLobby(Model model,HttpSession session,@RequestParam String lobby_name){
        Lobby lobbyToJoin = lobbyService.getLobbyByName(lobby_name);
        // if(lobbyToJoin != null) {
        // lobby with name "lobby_name" founded
        lobbyToJoin.setUser2((String) session.getAttribute("username"));
        lobbyService.removeLobby(lobbyToJoin);
        System.out.println("Lobby to join: " + lobbyToJoin);
        // return "redirect:/";
        // }
        // No lobby found with given name
        /*
         * System.out.println("Can't join to lobby: "+lobby_name+"!!!");
         * model.addAttribute("error_lobby_search", "Can't join to lobby: "+lobby_name+"!!!"); return
         * "lobby";
         */
    }
}
