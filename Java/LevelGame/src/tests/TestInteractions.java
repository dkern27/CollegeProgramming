package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import gameEngine.*;

public class TestInteractions {

	@Test
	/*
	 * Tests Fairy piece
	 * HEAL if shares space with player, NONE otherwise
	 * Loops for all possible spaces
	 */
	public void testFairy(){
		Drawable[] pieces = new Drawable[GameEngine.BOARD_SIZE];
		Fairy fairy = new Fairy(0);
		pieces[fairy.getLocation()]=fairy;
		//HEAL if on the same space
		assertEquals(InteractionResult.HEAL, fairy.interact(pieces, 0));
		//No interaction for any other location
		for(int i=1; i<GameEngine.BOARD_SIZE;i++){
			assertEquals(InteractionResult.NONE, fairy.interact(pieces, i));
		}
	}
	
	/*
	 * Tests PointPiece
	 * GET_POINT if shares space with player, NONE otherwise
	 * Loops for all possible spaces
	 */
	@Test
	public void testPointPiece(){
		Drawable[] pieces = new Drawable[GameEngine.BOARD_SIZE];
		PointPiece point = new PointPiece(0);
		pieces[point.getLocation()]=point;
		//GET_POINT if on the same space
		assertEquals(InteractionResult.GET_POINT, point.interact(pieces, 0));
		//No interaction for any other location
		for(int i=1; i<GameEngine.BOARD_SIZE;i++){
			assertEquals(InteractionResult.NONE, point.interact(pieces, i));
		}
	}
	
	/*
	 * Tests Monster
	 * HIT if shares space with player when board is printed, NONE otherwise
	 * Loops for all possible spaces
	 */	
	@Test
	public void testMonster(){
		Drawable[] pieces = new Drawable[GameEngine.BOARD_SIZE];
		Monster monster = new Monster(0);
		pieces[monster.getLocation()]=monster;
		//HIT if on the same space
		//monster at 0, player at 1 results in hit, as monster will move onto 1, where player is
		//before board is printed when move is used
		assertEquals(InteractionResult.HIT, monster.interact(pieces, 1));
		
		//NONE because monster would move to position 1 if playing the game
		assertEquals(InteractionResult.NONE, monster.interact(pieces,0));
		//No interaction for any other location
		for(int i=2; i<GameEngine.BOARD_SIZE;i++){
			assertEquals(InteractionResult.NONE, monster.interact(pieces, i));
		}
	}
	
	
	/*
	 * Tests Sniper
	 * DIE if two spaces to left or right, NONE otherwise
	 * Loops for all possible spaces
	 */
	@Test
	public void testSniper(){
		Drawable[] pieces = new Drawable[GameEngine.BOARD_SIZE];
		Sniper sniper = new Sniper(2);
		pieces[sniper.getLocation()]=sniper;
		//DIE if two spaces to the left or right
		assertEquals(InteractionResult.DIE, sniper.interact(pieces, 4));
		assertEquals(InteractionResult.DIE, sniper.interact(pieces, 0));
		
		//No interaction for any other location, including on and directly around the sniper
		assertEquals(InteractionResult.NONE, sniper.interact(pieces, 1));
		assertEquals(InteractionResult.NONE, sniper.interact(pieces, 2));
		assertEquals(InteractionResult.NONE, sniper.interact(pieces, 3));
		for(int i=5; i<GameEngine.BOARD_SIZE;i++){
			assertEquals(InteractionResult.NONE, sniper.interact(pieces, i));
		}
	}
	
	/*
	 * Tests Treasure
	 * ADVANCE if shares space with player, NONE otherwise
	 * Loops for all possible spaces
	 */
	@Test
	public void testTreasure(){
		Drawable[] pieces = new Drawable[GameEngine.BOARD_SIZE];
		Treasure treasure = new Treasure(0);
		pieces[treasure.getLocation()]=treasure;
		//ADVANCE if on the same space
		assertEquals(InteractionResult.ADVANCE, treasure.interact(pieces, 0));
		//No interaction for any other location
		for(int i=1; i<GameEngine.BOARD_SIZE;i++){
			assertEquals(InteractionResult.NONE, treasure.interact(pieces, i));
		}
	}
	
	/*
	 * Tests Spikes
	 * DIE if player lands on it, NONE otherwise
	 * Loops for all possible spaces
	 */
	@Test
	public void testSpikes(){
		Drawable[] pieces = new Drawable[GameEngine.BOARD_SIZE];
		Spikes spike = new Spikes(0);
		pieces[spike.getLocation()]=spike;
		//DIE if on the same space
		assertEquals(InteractionResult.DIE, spike.interact(pieces, 0));
		//No interaction for any other location
		for(int i=1; i<GameEngine.BOARD_SIZE;i++){
			assertEquals(InteractionResult.NONE, spike.interact(pieces, i));
		}
	}
	
}
