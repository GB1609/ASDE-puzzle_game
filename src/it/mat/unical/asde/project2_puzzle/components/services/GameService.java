package it.mat.unical.asde.project2_puzzle.components.services;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import it.mat.unical.asde.project2_puzzle.model.Grid;
import it.mat.unical.asde.project2_puzzle.model.PuzzleUtilities;

@Service
public class GameService {
	Grid grid;
	PuzzleUtilities matrixToComplete;

	@PostConstruct
	public void init() {
		grid = new Grid(0, "Gatto");
		matrixToComplete = new PuzzleUtilities(/* grid.getDim() */5);
	}

	public Grid getRandomGrid() {
		return grid;
	}

	public boolean check_completation() {
		return matrixToComplete.checkPuzzleTermination();
	}

	// TODO delete after testing
	public void printToComplete() {
		System.out.println("MATRIX TO COMPLETE:");
		matrixToComplete.printMatrix();
		System.out.println("END MATRIX TO COMPLETE");
	}

	public void updateStateGame(String old_location, int old_position, String new_location, int new_position,
			String piece) {

		if (old_location.equals("to_complete"))
			matrixToComplete.removePiece(piece, old_position);
		if (new_location.equals("to_complete"))
			matrixToComplete.insertPiece(piece, new_position);
		printToComplete(); // TODO delete after testing
		if (check_completation())
			System.out.println("WIN");

	}
}