package it.mat.unical.asde.project2_puzzle.model;

import java.awt.Point;
import java.util.ArrayList;

public class PuzzleUtilities{
    private int[][] matrix;
    private int height;
    private int width;

    public int getHeight(){
        return height;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public int getWidth(){
        return width;
    }

    public void setWidth(int width){
        this.width = width;
    }

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
//        System.out.println("X===" + x + " e Y====" + y);
        return pairToCell(x, y);
        // CHECK, IT WORKS
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

    public void insert_random_number(int x){
        int i = (int) (Math.random() * this.width);
        int k = (int) (Math.random() * this.height);
        if (this.matrix[i][k] == -1)
            this.matrix[i][k] = x;
        else
            this.insert_random_number(x);
    }

    public Point cellToPair(int c){
        int riga = c / width;
        int colonna = c - (riga * width);
        return new Point(riga, colonna);
    }

    int pairToCell(int a,int b){
        return (b * width) + a;
    }

    public void create_random_grid(ArrayList<String> nameImages){
        for (int a = 0; a < width * height; a++)
            insert_random_number(a);
        for (int a = 0; a < width; a++)
            for (int b = 0; b < width; b++)
                nameImages.add(cellToPair(matrix[a][b]).x + "-" + cellToPair(matrix[a][b]).y);
    }

    public void print_matrix(){
        for (int a = 0; a < width; a++) {
            for (int b = 0; b < width; b++)
                System.out.print(" | " + matrix[a][b] + " | ");
            System.out.println();
        }
    }

    public void shuffle(String piece,int new_position){
        int p = convertInputToNumber(piece);
        resetCell(p);
        int pos = 0;
        for (int a = 0; a < width; a++)
            for (int b = 0; b < width; b++)
                if (pos == new_position) {
                    matrix[a][b] = p;
                    return;
                } else
                    pos++;
    }

    public void insert_in_matrix(String piece,int new_position){
//        System.out.println("INSERT: " + piece + "     in         " + new_position);
        int p = convertInputToNumber(piece);
//        System.out.println("PEZZO DA INSERIRE=" + p);
        int pos = 0;
        for (int a = 0; a < width; a++)
            for (int b = 0; b < width; b++)
                if (pos == new_position) {
                    matrix[a][b] = p;
                    return;
                } else
                    pos++;
    }

    private void resetCell(int p){
        for (int a = 0; a < width; a++)
            for (int b = 0; b < width; b++)
                if (matrix[a][b] == p)
                    matrix[a][b] = -1;
    }
}
