package it.mat.unical.asde.project2_puzzle.components.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import it.mat.unical.asde.project2_puzzle.components.services.GameService;

@Controller
public class GameController{
    @Autowired
    GameService gameService;

    @GetMapping("/")
    public String goToGame(Model m){
        m.addAttribute("randomGrid", gameService.getRandomGrid());
        return "Game";
    }

    @PostMapping("move_piece") // le richieste ajax solitamente sono in get
    @ResponseBody // per dire che ciò che restituisce non è il nome della view ma il risultato
                  // della richiesta ajax
    public void movePiece(@RequestParam String new_location,@RequestParam String old_location,@RequestParam String piece,
            @RequestParam int new_position){
        System.out.println("nl: " + new_location);
        System.out.println("ol: " + old_location);
        System.out.println("piece: " + piece);
        System.out.println("np:" + new_position);
    }
}
