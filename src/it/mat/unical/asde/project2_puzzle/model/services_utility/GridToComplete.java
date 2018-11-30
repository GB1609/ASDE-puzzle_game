package it.mat.unical.asde.project2_puzzle.model.services_utility;

import java.awt.Point;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GridToComplete {
	private int[][] matrix;
	private int height;
	private int width;
	private SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
	private Date timer = new Date();
	private String player;
	private int access = 0;// TODO remove -> only for testing end game

	public GridToComplete(int dim, String player) {
		super();
		height = dim;
		width = dim;
		this.player = player;
		matrix = new int[width][height];
		for (int i = 0; i < width; i++)
			for (int k = 0; k < height; k++)
				matrix[i][k] = -1;
	}

	public boolean checkPuzzleTermination() {
		if (player.equals("a") && access == 5)
			return true;
		access++;
		int a = 0;
		for (int i = 0; i < height; i++)
			for (int k = 0; k < width; k++) {
				if (matrix[i][k] != a)
					return false;
				a++;
			}
		return true;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void insertPiece(String piece, int new_position) {
		int p = convertInputToNumber(piece);
		Point coordinates = cellToPair(new_position);
		matrix[coordinates.x][coordinates.y] = p;
	}

	public void printMatrix() {
		for (int a = 0; a < width; a++) {
			for (int b = 0; b < width; b++)
				System.out.print(" | " + matrix[a][b] + " | ");
			System.out.println();
		}
	}

	public void removePiece(String piece, int old_position) {
		Point p = cellToPair(old_position);
		matrix[p.x][p.y] = -1;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	private Point cellToPair(int c) {
		int riga = c / width;
		int colonna = c - (riga * width);
		return new Point(riga, colonna);
	}

	private int convertInputToNumber(String s) {
		int x = Integer.parseInt(s.substring(0, 1));
		int y = Integer.parseInt(s.substring(s.length() - 1));
		return pairToCell(x, y);
	}

	private int pairToCell(int a, int b) {
		return (b * width) + a;
	}

	public Integer getStatus() {
		int a = 0;
		int progress = 0;
		for (int i = 0; i < height; i++)
			for (int k = 0; k < width; k++) {
				if (matrix[i][k] == a)
					progress++;
				a++;
			}
		return (progress * 100) / (height * width);
	}

	public Date getDate() {
		System.out.println("INGET:" + timer);
		return this.timer;
	}

	public String getTimer() {
		return parser.format(this.timer);
	}

	public void setTimer(String timer) {
		try {
			this.timer = parser.parse(timer);
			System.out.println("INSET:" + timer);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getPlayer() {
		return this.player;
	}
}
