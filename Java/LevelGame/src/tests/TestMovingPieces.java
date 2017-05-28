package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import gameEngine.*;

public class TestMovingPieces {

	/*
	 * Tests sequential movement used by Monster
	 * Tests regular movement, switching directions, sharing space with player (allowed), and jumping over other pieces
	 * 
	 * Runs loop to have monster go across entire board
	 * 
	 */
	@Test
	public void testSequentialMovement() {
		Drawable[] pieces = new Drawable[GameEngine.BOARD_SIZE];
		Monster monster = new Monster(18);
		pieces[18]=monster;
		
		Nothing nothing = new Nothing(10);
		pieces[10]=nothing;
		
		Nothing nothing2 = new Nothing(0);
		pieces[0]=nothing2;
		
		int expectedLocation=monster.getLocation();
		int actualLocation=-1; 
		int direction=1;//Monster is initially moving right
		
		for(int i=0; i<GameEngine.BOARD_SIZE+5;i++){
			//Checks for direction monster will be going on next move
			//Accounts for other pieces in that spot
			do{
				if(expectedLocation==0)
					direction=1;
				if(expectedLocation==GameEngine.BOARD_SIZE-1)
					direction=-1;
				expectedLocation+=direction;
				//For case of Nothing piece at board boundary
				if(pieces[expectedLocation]==monster)
					break;
			}while(pieces[expectedLocation]!=null);
			
			//Moves monster
			monster.move(pieces, 11);
			
			actualLocation=monster.getLocation();
			
			assertEquals(expectedLocation, actualLocation);	
		}
	}
	
	/*
	 * Tests random motion, used by the Fairy.
	 * Strategy: 
	 * - Place pieces in all spaces EXCEPT 0, 9, 13, 14, 17, 20.
	 * - Both end pieces, 0 and 20, are open
	 * - Same piece is used in all spaces, as piece identity doesn't matter.
	 * - Set player location to space 14.
	 * - Call move function many times, ensure each open space is chosen
	 *   (14 is "open" but has the player, so it can be chosen)
	 */
	
	@Test
	public void testRandomMovement() {
		// Each test will create its own list of pieces
		Drawable [] pieces = new Drawable[GameEngine.BOARD_SIZE];
		// Start with 1, leaves 0 open
		for (int i=1;i<=8; i++)
			pieces[i] = new Nothing(i);
		// Leave 9 open
		for (int i=10; i<=12; i++)
			pieces[i] = new Nothing(i);
		// Leave 13 open, player in 14
		for (int i=15; i<=16; i++)
			pieces[i] = new Nothing(i);
		// Leave 17 open
		for(int i=18;i<20;i++)
			pieces[i]=new Nothing(i);
		//Leave 20 open
		
		// Place Fairy in an open space - 9
		// Note that Fairy location will be updated via move method, 
		// so occasionally location 9 will be open and may be chosen
		Fairy fairy = new Fairy(9);
		pieces[9] = fairy;
		int count0 = 0;
		int count9 = 0;
		int count13 = 0;
		int count14 = 0;
		int count17 = 0;
		int count20 = 0;
		for (int i=0; i<750; i++) {
			fairy.move(pieces, 14);
			int loc = fairy.getLocation();
			// ensure no other space is chosen
			if (loc != 0 && loc != 9 && loc != 13 && loc != 14 && loc != 17 && loc != 20)
				fail("Invalid square selected");
			// counters to ensure every valid option is chosen
			if (loc == 0) count0++;
			if (loc == 9) count9++;
			if (loc == 13) count13++;
			if (loc == 14) count14++;
			if (loc == 17) count17++;
			if (loc == 20) count20++;
		}
		// 10 is an arbitrary value, goal is to ensure each option
		// is randomly chosen some number of times.
		
		assertTrue(count0 > 100);
		assertTrue(count9 > 100);
		assertTrue(count13 > 100);
		assertTrue(count14 > 100);
		assertTrue(count17 > 100);	
		assertTrue(count20 > 100);
	}

}
