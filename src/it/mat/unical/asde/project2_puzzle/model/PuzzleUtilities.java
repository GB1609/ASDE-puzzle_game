package it.mat.unical.asde.project2_puzzle.model;

import java.awt.Point;
import java.util.ArrayList;

public class PuzzleUtilities{
    private int[][] matrix;
    private int height;
    private int width;

    public PuzzleUtilities(int dim){
        super();
        this.height = dim;
        this.width = dim;
        this.matrix = new int[this.width][this.height];
        for (int i = 0; i < this.width; i++)
            for (int k = 0; k < this.height; k++)
                this.matrix[i][k] = -1;
    }

    private int convertInputToNumber(String s){
        int x = Integer.parseInt(s.substring(0, 1));
        int y = Integer.parseInt(s.substring(s.length() - 1));
        return pairToCell(x, y);
    }

    public void updateMatrix(String moved,String movedOn){
        int pieceMoved = convertInputToNumber(moved);
        int pieceMovedOn = convertInputToNumber(movedOn);
    }

    public boolean checkPuzzleTermination(){
        int a = 0;
        for (int i = 0; i < this.height; i++)
            for (int k = 0; k < this.width; k++) {
                if (this.matrix[i][k] != a)
                    return false;
                a++;
            }
        return true;
    }

    public void insertPiece(int x){
        int i = (int) (Math.random() * this.width);
        int k = (int) (Math.random() * this.height);
        if (this.matrix[i][k] == -1)
            this.matrix[i][k] = x;
        else
            this.insertPiece(x);
    }

    public Point cellToPair(int c){
        int riga = c / width;
        int colonna = c - (riga * width);
        return new Point(riga, colonna);
    }

    int pairToCell(int colonna,int riga){
        return riga * width + colonna;
    }

    public void createGrid(ArrayList<String> nameImages){
        for (int a = 0; a < width * height; a++)
            insertPiece(a);
        for (int a = 0; a < width; a++)
            for (int b = 0; b < width; b++)
                nameImages.add(cellToPair(matrix[a][b]).x + "-" + cellToPair(matrix[a][b]).y);
    }
}
