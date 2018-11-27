package it.mat.unical.asde.project2_puzzle.model;

import java.awt.Point;
import java.util.ArrayList;

public class PuzzleUtilities{
    private int[][] matrix;
    private int height;
    private int width;

    public PuzzleUtilities(int dim){
        super();
        height = dim;
        width = dim;
        matrix = new int[width][height];
        for (int i = 0; i < width; i++)
            for (int k = 0; k < height; k++)
                matrix[i][k] = -1;
    }

    public boolean checkPuzzleTermination(){
        int a = 0;
        for (int i = 0; i < height; i++)
            for (int k = 0; k < width; k++) {
                if (matrix[i][k] != a)
                    return false;
                a++;
            }
        return true;
    }

    // TODO move in Grid class
    public void create_random_grid(ArrayList<String> nameImages){
        for (int a = 0; a < width * height; a++)
            insert_random_number(a);
        for (int a = 0; a < width; a++)
            for (int b = 0; b < width; b++)
                nameImages.add(cellToPair(matrix[a][b]).x + "-" + cellToPair(matrix[a][b]).y);
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public void insert_random_number(int x){
        int i = (int) (Math.random() * width);
        int k = (int) (Math.random() * height);
        if (matrix[i][k] == -1)
            matrix[i][k] = x;
        else
            insert_random_number(x);
    }

    public void insertPiece(String piece,int new_position){
//        System.out.println("INSERT: " + piece + "     in         " + new_position);
        int p = convertInputToNumber(piece);
        Point coordinates = cellToPair(new_position);
//        System.out.println("PEZZO DA INSERIRE=" + p);
        matrix[coordinates.x][coordinates.y] = p;
    }

    public void printMatrix(){
        for (int a = 0; a < width; a++) {
            for (int b = 0; b < width; b++)
                System.out.print(" | " + matrix[a][b] + " | ");
            System.out.println();
        }
    }

    public void removePiece(String piece,int old_position){
        Point p = cellToPair(old_position);
        matrix[p.x][p.y] = -1;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void setWidth(int width){
        this.width = width;
    }

    private Point cellToPair(int c){
        int riga = c / width;
        int colonna = c - (riga * width);
        return new Point(riga, colonna);
    }

    private int convertInputToNumber(String s){
        int x = Integer.parseInt(s.substring(0, 1));
        int y = Integer.parseInt(s.substring(s.length() - 1));
//        System.out.println("X===" + x + " e Y====" + y);
        return pairToCell(x, y);
        // CHECK, IT WORKS
    }

    private int pairToCell(int a,int b){
        return (b * width) + a;
    }
}