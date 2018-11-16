package it.mat.unical.asde.project2_puzzle.components.services;

import org.springframework.stereotype.Service;
import it.mat.unical.asde.project2_puzzle.model.Grid;

@Service
public class GameService{
    public Grid getRandomGrid(){
        return new Grid(0, "Gatto");
    }
}
