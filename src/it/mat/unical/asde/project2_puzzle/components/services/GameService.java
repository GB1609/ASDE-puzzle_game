package it.mat.unical.asde.project2_puzzle.components.services;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import it.mat.unical.asde.project2_puzzle.model.Grid;
import it.mat.unical.asde.project2_puzzle.model.PuzzleUtilities;

@Service
public class GameService{
    Grid grid;
    PuzzleUtilities matrixToComplete;

    @PostConstruct
    public void init(){
        grid = new Grid(0, "Gatto");
        matrixToComplete = new PuzzleUtilities(/* grid.getDim() */5);
    }

    public Grid getRandomGrid(){
        return grid;
    }

    public void updateInitialMatrix(){
    }

    public void shuffle_matrix_toComplete(String piece,int new_position){
        matrixToComplete.shuffle(piece, new_position);
    }

    public boolean check_completation(){
        return matrixToComplete.checkPuzzleTermination();
    }

    public void printToComplete(){
        System.out.println("MATRIX TO COMPLETE:");
        matrixToComplete.print_matrix();
        System.out.println("END MATRIX TO COMPLETE");
    }

    public void insert_in_matrix_toComplete(String piece,int new_position){
        matrixToComplete.insert_in_matrix(piece, new_position);
    }

    public void remove_from_matrix(String piece,int new_position){
        matrixToComplete.remove_from_matrix(piece, new_position);
    }
}