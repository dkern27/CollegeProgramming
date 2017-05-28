package gameEngine;
import java.util.ArrayList;
import java.util.Scanner;

import levelPieces.LevelEngine;

/**
 * Framework for LevelGame. 
 * 
 * @author Cyndi Rader
 * 
 *
 */

public class GameEngine {
	/**
	 * Determines the size of the game board. 
	 * 
	 * Board is a 1D structure. It is an odd number so player can start 
	 * exactly in the middle
	 */
	public final static int BOARD_SIZE = 21;
	/**
	 * The number of points to complete a level. 
	 * Player can earn 1 point at a time by interacting with a game piece.
	 */
	public final static int POINTS_TO_ADVANCE = 2;
	/**
	 * The number of hits that can be taken before death. 
	 */
	public final static int POINTS_TO_DIE = 2;
	/**
	 * Number of levels defined for this game. 
	 * GameEngine will automatically play each level, starting with 1
	 * through the number specified here. 
	 */
	public final static int NUM_LEVELS = 2;
	private int currentLevel;
	private LevelEngine levels; 
	// Data structures that contain the game pieces for the level
	// Will be initialized by the LevelEngine
	private Drawable [] pieces;
	private ArrayList<Moveable> movingPieces;
	private ArrayList<InteractingPiece> interactingPieces;
	// Variables to track the player status
	private int playerLocation;
	private int levelPoints;
	private int damagePoints;
	/**
	 * Status set when interacting with game pieces. 
	 * Enables GameEngine to know when player advances to next level or has 
	 * been killed. 
	 *
	 */
	public enum PlayerStatus {DEAD, ADVANCING, OK; }
	private PlayerStatus playerStatus;

	public GameEngine() {
		levels = new LevelEngine(); 
	}
	/**
	 * Should be called at the beginning of every level (including beginning 
	 * of program) to set up the data for that level.
	 * 
	 * @param levelNum The number of the level to be loaded. Level number should
	 * be from 1 to GameEngine.NUM_LEVELS
	 */

	public void setupLevel(int levelNum) {
		// Player starts in the middle of the board
		playerLocation = BOARD_SIZE/2;
		// reset player variables
		levelPoints = 0;
		damagePoints = 0;
		playerStatus = PlayerStatus.OK;
		// request data structures from LevelEngine
		levels.createLevel(levelNum);
		pieces = levels.getPieces();
		movingPieces = levels.getMovingPieces();
		interactingPieces = levels.getInteractingPieces();
	}

	/**
	 * Prints a representation of the board. 
	 * The player is represented by 'P'. All other objects must be Drawable, so 
	 * that the draw method can be used.
	 */
	public void displayBoard() {
		for (int i=0; i<pieces.length; i++) {
			if (i == playerLocation) 
				System.out.print('P');
			else if (pieces[i] == null)
				System.out.print(' ');
			else
				pieces[i].draw();
			System.out.print('|');
		}
		System.out.println();
	}

	private void displayMenu() {
		System.out.println();
		System.out.println("1: Move right");
		System.out.println("2: Move left");
		System.out.println("3: Jump right");
		System.out.println("4: Jump left");
		System.out.print("Enter your choice: ");
	}

	/**
	 * Accepts and returns the player's movement choice. 
	 * Ensures the value is between 1 and 4.  
	 * 
	 * @return user's validated selection
	 */
	public int getPlayerChoice() {
		int playerChoice = 0;
		do {
			displayMenu();
			Scanner scan = new Scanner(System.in);
			String choiceStr = scan.next();
			try {
				playerChoice = Integer.parseInt(choiceStr);
				if (playerChoice < 1 || playerChoice > 4) {
					System.out.println("Invalid option, please retry");
					playerChoice = 0;
				}					
			} catch (NumberFormatException e) {
				System.out.println("Must enter a number, please retry");
			}
		} while (playerChoice == 0);
		return playerChoice;
	}

	/*
	 * updatePlayerLocation updates the player's location.
	 */
	/**
	 * Moves the player as determined by the menu option. 
	 * A "move" goes one space in the desired direction, "jump" goes two. 
	 * If move would go beyond ends of board, an error message is displayed. 
	 * 
	 * @param option a numeric option representing the direction selected by the user
	 * @return true if the option is valid (within bounds), false otherwise
	 */
	public boolean updatePlayerLocation(int option) {
		if (option == 1 && playerLocation < (pieces.length-1)) {
			playerLocation++;
			return true;
		}
		if (option == 2 && playerLocation > 0) {
			playerLocation--;
			return true;
		}
		if (option == 3 && playerLocation < (pieces.length - 2)) {
			playerLocation += 2;
			return true;
		}
		if (option == 4 && playerLocation > 1) {
			playerLocation -= 2;
			return true;
		}				
		System.out.println("Invalid option, please retry");
		return false;
	}

	/**
	 * Calls the move method for each Moveable piece. 
	 * All Moveable pieces should be placed in the movingPieces list. 
	 * Pieces will move at the end of the level (after user has selected option, 
	 * to prepare for the next level). 
	 */
	public void movePieces() {
		for (Moveable piece : movingPieces) {
			piece.move(pieces, playerLocation);		
		}	
	}

	/**
	 * Calls the interact method for each InteractingPiece. 
	 * All interacting pieces should be placed in the interactingPieces
	 * list. GameEngine will take the appropriate action based on the 
	 * InteractionResult (e.g., sets player status to DEAD if the 
	 * result is InteractionResult.DIE). 
	 */
	public void interaction() {
		for (InteractingPiece piece : interactingPieces) {
			InteractionResult result = piece.interact(pieces, playerLocation);		
			if (result == InteractionResult.GET_POINT) {
				levelPoints++;
				System.out.println("\nYou just won a prize!\n");
			}
			if (result == InteractionResult.HIT) {
				damagePoints++;
				System.out.println("\nYou just took a hit!\n");
			}
			if (result == InteractionResult.DIE) {
				playerStatus = PlayerStatus.DEAD;
				System.out.println("\nSomething just killed you!\n");
			}
			if (result == InteractionResult.ADVANCE) {
				playerStatus = PlayerStatus.ADVANCING;
				System.out.println("\nGood news, you have won an advance!\n");
			}
			if (result == InteractionResult.HEAL) {
				damagePoints--;
				System.out.println("\nYou have gained a health point!\n");
			}
		}			
	}

	/**
	 * Determines if this level is complete. 
	 * Will be complete if player is advancing (either by interaction 
	 * with a game piece or accumulating points) or dead (either by 
	 * interaction with a game piece or accumulating too many hits).
	 * 
	 * @return true if player advances or was killed, false otherwise
	 */
	public boolean levelFinished() {
		if (levelPoints >= POINTS_TO_ADVANCE || playerStatus == PlayerStatus.ADVANCING) {
			System.out.println("Advancing to next level...\n");
			return true;
		}
		if (damagePoints >= POINTS_TO_DIE) {
			System.out.println("Too many hits, you are dead");
			playerStatus = PlayerStatus.DEAD;
			return true;
		}
		if (playerStatus == PlayerStatus.DEAD) {
			System.out.println("You are dead, level is over");
			return true;
		}
		return false;	
	}

	/**
	 * Completes the game play for one level.
	 * During each round, the board is displayed, the player enters a movement
	 * option, the player's location is updated, then the player interacts with
	 * any pieces in the interactingPieces list. Moveable piece locations are then 
	 * updated for the next round. This continues until the level is finished. 
	 */
	public void doOneLevel() {
		while (!levelFinished()) {
			displayBoard();
			int choice = getPlayerChoice();
			boolean moved = updatePlayerLocation(choice);
			if (moved) {
				interaction();
				movePieces();
			}
		}
	}

	/**
	 * Plays the game. 
	 * Continues until either all levels have been completed or the player 
	 * has been killed. Program exits when game is over. 
	 */
	public void playGame() {
		while (currentLevel < NUM_LEVELS && playerStatus != PlayerStatus.DEAD) {
			currentLevel++;
			setupLevel(currentLevel);
			doOneLevel();
		}
		// If reach this point, either all levels were completed OR
		// player is dead
		if (playerStatus == PlayerStatus.DEAD)
			System.out.println("Too bad, you lose...");
		else
			System.out.println("Congratulations, you won!");
		System.exit(0);
	}
	
	public static void main(String[] args) {
		GameEngine game = new GameEngine();
		game.playGame();
	}

}
