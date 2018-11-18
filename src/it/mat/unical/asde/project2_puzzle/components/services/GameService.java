package it.mat.unical.asde.project2_puzzle.components.services;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import it.mat.unical.asde.project2_puzzle.model.Grid;
import it.mat.unical.asde.project2_puzzle.model.GridToComplete;

@Service
public class GameService{
    Grid grid;
    GridToComplete matrixToComplete;

    public boolean check_completation(){
        return matrixToComplete.checkPuzzleTermination();
    }

    public Grid getRandomGrid(){
        return grid;
    }

    @PostConstruct
    public void init(){
        grid = new Grid(0, "Gatto");
        matrixToComplete = new GridToComplete(grid.getDim());
    }

    public void updateStateGame(String old_location,int old_position,String new_location,int new_position,String piece){
        if (old_location.equals("to_complete"))
            matrixToComplete.removePiece(piece, old_position);
        if (new_location.equals("to_complete"))
            matrixToComplete.insertPiece(piece, new_position);
        if (check_completation())
            System.out.println("WIN");
    }
}