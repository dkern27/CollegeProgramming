package FarmGame;

import java.util.ArrayList;


public class GameEngine {

	private ArrayList<Level> levels;
	private int currentLevel;
	private final int maxLevel = 4;
	public static boolean easyMode = true;
	
	public GameEngine() {
		currentLevel = 1;
		
		//Initialize levels
		levels = new ArrayList<Level>();
		levels.add(0, null);
		levels.add(1, new Level( "Chicken", "/BackgroundImages/FindHens.jpg", 4, AnimalType.CHICKEN));
		levels.add(2, new Level( "Sheep", "/BackgroundImages/FindSheep.jpg", 5, AnimalType.SHEEP));
		levels.add(3, new Level( "Pig", "/BackgroundImages/FindPigs.jpg", 6, AnimalType.PIG));
		levels.add(4, new Level( "Cow", "/BackgroundImages/FindCows.jpg", 7, AnimalType.COW));	
	}
	
	public void nextLevel() {
		//Move to next level if the player has found all the animals
		if( levels.get(currentLevel).levelComplete() ) {
			currentLevel++;
		}
		else {
			//Do nothing
		}	
	}
	
	public boolean gameOver() {
		//If player has reached beyond the max level then the game is over
		if( currentLevel > maxLevel ) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		
		//Create the game, initializes in the constructor
		GameEngine theGame = new GameEngine();
		
		//Start the gui
		MainGUI theGUI = new MainGUI( theGame );
		theGUI.setVisible(true);
	}
	
	
////// Getters and Setters //////
	
	public ArrayList<Level> getLevels() {
		return levels;
	}

	public int getCurrentLevelNumber() {
		return currentLevel;
	}
	
	public void setCurrentLevel(int c){
		currentLevel=c;
	}
	
	public Level getCurrentLevel(){
		return levels.get(currentLevel);
	}
	
}
