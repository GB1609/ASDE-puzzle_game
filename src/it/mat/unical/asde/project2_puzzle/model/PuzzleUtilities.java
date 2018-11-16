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
                this.matrix[i][k] = 0;
    }

    public boolean check(){
        return true;
    }

    public void insertPiece(int x){
        int i = (int) (Math.random() * this.width);
        int k = (int) (Math.random() * this.height);
        if (this.matrix[i][k] == 0)
            this.matrix[i][k] = x;
        else
            this.insertPiece(x);
    }

    public Point cellToPair(int c){
        int colonna = c / width;
        int riga = c - (colonna * width);
        return new Point(colonna, riga);
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
