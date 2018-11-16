package it.mat.unical.asde.project2_puzzle.model;

import java.util.ArrayList;

public class Grid{
    ArrayList<String> nameImages;
    String subjectName;
    String difficulty;

    public Grid(int difficulty,String subjectName){
        // TODO generate randomly indexes
        nameImages = new ArrayList<>();
        this.subjectName = subjectName;
        int cols = 5;
        if (difficulty > 0) {
            cols = 10;
        }
        PuzzleUtilities pu=new PuzzleUtilities(cols);
        pu.createGrid(nameImages);
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
