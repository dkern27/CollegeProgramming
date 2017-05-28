package experiment;

import java.util.*;

//Class for setting up the game board. Currently hard coded to make a 4x4 board for this experiment
//Most methods are skeletons, when filled out correctly these should pass tests

public class IntBoard {
	private Map<BoardCell, LinkedList<BoardCell>> adjacenciesMap;
	private Set<BoardCell> visitedCells;
	private Set<BoardCell> targetList;
	private BoardCell[][] gameBoard;

	public IntBoard(int rows, int columns) {
		gameBoard = new BoardCell[rows][columns];//must fill with BoardCells that know their location
		
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				gameBoard[i][j] = new BoardCell(i,j);
			}
		}
		
		adjacenciesMap = new HashMap<BoardCell, LinkedList<BoardCell>>();
		visitedCells = new HashSet<BoardCell>();
		targetList = new HashSet<BoardCell>();	
	}
	
	//create map of adjacencies that correspond to each cell on the game board
	public void calcAdjacencies(){
		LinkedList<BoardCell> tempLinkList = new LinkedList<BoardCell>(); 
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				tempLinkList.clear(); 
				if (i - 1 >= 0){
					tempLinkList.add(gameBoard[i-1][j]);
				}
				if (i + 1 < 4){
					tempLinkList.add(gameBoard[i+1][j]);
				}
				if (j - 1 >= 0){
					tempLinkList.add(gameBoard[i][j - 1]);
				}
				if (j + 1 < 4){
					tempLinkList.add(gameBoard[i][j + 1]);
				}
				adjacenciesMap.put(gameBoard[i][j], new LinkedList<BoardCell>(tempLinkList));
			}
		}	
	}
	
	//calculates the valid cells to move to given a starting cell and a length to go
	//note that this will eventually have a recursive helper function that it calls when
	//fully implemented
	public void calcTargets(BoardCell startCell, int pathLength){
		visitedCells.clear();
		targetList.clear();
		visitedCells.add(startCell);
		
		findAllTargets(startCell, pathLength);
	}
	
	private void findAllTargets(BoardCell startCell, int pathLength) {
		LinkedList<BoardCell> adjacentCells = new LinkedList<BoardCell>();
		
		for (BoardCell cell: adjacenciesMap.get(startCell)){
			if (!(visitedCells.contains(cell))){
				adjacentCells.add(cell);
			}
		}

		for (BoardCell cell: adjacentCells){
			visitedCells.add(cell);
			if (pathLength == 1){
				targetList.add(cell);
			}
			else {
				findAllTargets(cell, pathLength - 1);
			}
			visitedCells.remove(cell);
		}
	}

	//getter for the Set of targets that is created after calcTargets is called
	public Set<BoardCell> getTargets(){
		return targetList;
	}
	
	//getter for the linked list of adjacent cells to a specific cell
	public LinkedList<BoardCell> getAdjList(BoardCell currentCell){
		return adjacenciesMap.get(currentCell);
	}
	
	//getter for a specific BoardCell object within the game board
	public BoardCell getCell(int row, int column){
		return gameBoard[row][column];
	}
}
