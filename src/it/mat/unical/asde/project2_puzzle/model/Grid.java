package it.mat.unical.asde.project2_puzzle.model;

import java.util.ArrayList;

public class Grid{
    private ArrayList<String> nameImages;
    private String subjectName;
    private String difficulty;
    private int dim;

    public int getDim(){
        return dim;
    }

    public void setDim(int dim){
        this.dim = dim;
    }

    public Grid(int difficulty,String subjectName){
        // TODO generate randomly indexes
        nameImages = new ArrayList<>();
        this.subjectName = subjectName;
        int dim = 5;
        if (difficulty > 0) {
            dim = 10;
        }
        PuzzleUtilities pu = new PuzzleUtilities(dim);
        pu.create_random_grid(nameImages);
        this.difficulty = (difficulty > 0) ? "dieci" : "cinque";
    }

    public ArrayList<String> getNameImages(){
        return nameImages;
    }

    public void setNameImages(ArrayList<String> nameImages){
        this.nameImages = nameImages;
    }

    public String getSubjectName(){
        return subjectName;
    }

    public void setSubjectName(String subjectName){
        this.subjectName = subjectName;
    }

    public String getDifficulty(){
        return difficulty;
    }

    public void setDifficulty(String difficulty){
        this.difficulty = difficulty;
    }
}
