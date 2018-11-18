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

    @PostMapping("/move_piece") // le richieste ajax solitamente sono in get
    @ResponseBody // per dire che ciò che restituisce non è il nome della view ma il risultato
                  // della richiesta ajax
    public void movePiece(@RequestParam String old_location,@RequestParam int old_position,@RequestParam String new_location,
            @RequestParam int new_position,@RequestParam String piece){
        System.out.println(old_position);
        if (new_location.equals(old_location) && new_location.equals("to_complete")) {
            gameService.shuffle_matrix_toComplete(piece, new_position);
        } else if (new_location.equals("to_complete") && !new_location.equals(old_location))
            gameService.insert_in_matrix_toComplete(piece, new_position);
        else if (!new_location.equals("to_complete") && old_location.equals("to_complete"))
            gameService.remove_from_matrix(piece, new_position);
//        gameService.printToComplete();
        if (gameService.check_completation())
            System.out.println("WIN");
    }
}
