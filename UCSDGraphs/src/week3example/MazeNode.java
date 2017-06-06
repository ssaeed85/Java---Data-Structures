/**
 * 
 */
package week3example;

import java.util.LinkedList;
import java.util.List;

/**
 * MazeNode.java
 * 
 * @author UCSD Intermediate Programming MOOC team
 * 
 * A class to represent a Node in a graph which is a Maze that the robot navigates.
 */
public class MazeNode {
	public static final char EMPTY = '-';
	public static final char PATH = 'o';
	public static final char START = 'S';
	public static final char GOAL = 'G';
	
	private List<MazeNode> neighbors;
	// Since our maze is always a grid, nodes keep track of their row and column
	private int row;
	private int column;
	private char displayChar;

	public MazeNode(int row, int col)
	{
		this.row = row;
		this.column = col;
		neighbors = new LinkedList<MazeNode>();
		displayChar = EMPTY;
	}

	public void addNeighbor(MazeNode neighbor) 
	{
		neighbors.add(neighbor);
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @return the displayChar
	 */
	public char getDisplayChar() {
		return displayChar;
	}
	
	/**
	 * @return the neighbors
	 */
	public List<MazeNode> getNeighbors() {
		return neighbors;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @param displayChar the displayChar to set
	 */
	public void setDisplayChar(char displayChar) {
		this.displayChar = displayChar;
	}
	
	
}
