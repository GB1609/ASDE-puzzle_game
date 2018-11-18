package it.mat.unical.asde.project2_puzzle.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Grid{
    private ArrayList<String> nameImages;
    private String subjectName;
    private String difficulty;
    private int dim;

    public Grid(int difficulty,String subjectName){
        nameImages = new ArrayList<>();
        this.subjectName = subjectName;
        dim = 5;
        if (difficulty > 0) {
            dim = 10;
        }
        generateRandomGrid(nameImages);
        this.difficulty = (difficulty > 0) ? "dieci" : "cinque";
    }

    public String getDifficulty(){
        return difficulty;
    }

    public int getDim(){
        return dim;
    }

    public ArrayList<String> getNameImages(){
        return nameImages;
    }

    public String getSubjectName(){
        return subjectName;
    }

    public void setDifficulty(String difficulty){
        this.difficulty = difficulty;
    }

    public void setDim(int dim){
        this.dim = dim;
    }

    public void setNameImages(ArrayList<String> nameImages){
        this.nameImages = nameImages;
    }

    public void setSubjectName(String subjectName){
        this.subjectName = subjectName;
    }

    private Point cellToPair(int number){
        int riga = number / dim;
        int colonna = number - (riga * dim);
        return new Point(riga, colonna);
    }

    private void generateRandomGrid(ArrayList<String> nameImages){
        List<Integer> toFill = IntStream.rangeClosed(0, (dim * dim) - 1).boxed().collect(Collectors.toList());
        Random random = new Random();
        while (!toFill.isEmpty()) {
            int index = random.nextInt(toFill.size());
            int toInsert = toFill.get(index);
            toFill.remove(index);
            Point point = cellToPair(toInsert);
            nameImages.add(point.x + "-" + point.y);
        }
    }
}
