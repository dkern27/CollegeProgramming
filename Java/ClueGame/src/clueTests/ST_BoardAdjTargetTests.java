package clueTests;

import java.util.LinkedList;
import java.util.Set;

//Doing a static import allows me to write assertEquals rather than
//assertEquals
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class ST_BoardAdjTargetTests {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	@BeforeClass
	public static void setUp() {
		board = new Board("Configs/Layouts/ClueLayout_ST_DH.csv", "Configs/Legends/ClueLegend_ST_DH.txt");
		board.initialize();
	}

	// Adjacency test for room cells, no room cell that is not a door cell should have any adjacency list
	// because as of now no rooms should allow movement inside
	// These cells are ORANGE on the planning spreadsheet
		//up to date for our board
	@Test
	public void testAdjacenciesForNonDoorRoomCells()
	{
		// Test a corner
		LinkedList<BoardCell> testList = board.getAdjList(0, 0);
		assertEquals(0, testList.size());
		// Test one that has walkway underneath
		testList = board.getAdjList(4, 11);
		assertEquals(0, testList.size());
		// Test one that has walkway above
		testList = board.getAdjList(7, 18);
		assertEquals(0, testList.size());
		// Test one that is in middle of room
		testList = board.getAdjList(21, 21);
		assertEquals(0, testList.size());
		// Test one beside a door
		testList = board.getAdjList(17, 4);
		assertEquals(0, testList.size());
		// Test one in room cornered by walkways
		testList = board.getAdjList(16, 11);
		assertEquals(0, testList.size());
	}

	//Testing locations that are doorways (one for each direction of door)
	//only adjacency should be walkway entered from
	// These tests are PURPLE on the planning spreadsheet
		//up to date for our board
	@Test
	public void testAdjacencyAtDoorLocations()
	{
		// TEST DOORWAY RIGHT 
		LinkedList<BoardCell> testList = board.getAdjList(12, 3);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(12, 4)));
		// TEST DOORWAY LEFT 
		testList = board.getAdjList(22, 9);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(22, 8)));
		//TEST DOORWAY DOWN
		testList = board.getAdjList(5, 3);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(6, 3)));
		//TEST DOORWAY UP
		testList = board.getAdjList(15, 22);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(14, 22)));
		
	}
	
	// Test adjacency for cells directly next to door entrances and correctly oriented for entering
	// The cells should list the door as adjacent in addition to other walkways
	// These tests are GREEN in planning spreadsheet
		//up to date for our board
	@Test
	public void testAdjacencyOfCellsNextToDoorways()
	{
		// Test beside a door direction RIGHT
		LinkedList<BoardCell> testList = board.getAdjList(0, 12);
		assertTrue(testList.contains(board.getCellAt(0, 11)));
		assertTrue(testList.contains(board.getCellAt(0, 13)));
		assertTrue(testList.contains(board.getCellAt(1, 12)));
		assertEquals(3, testList.size());
		// Test beside a door direction DOWN
		testList = board.getAdjList(4, 22);
		assertTrue(testList.contains(board.getCellAt(5, 22)));
		assertTrue(testList.contains(board.getCellAt(3, 22)));
		assertTrue(testList.contains(board.getCellAt(4, 21)));
		assertEquals(3, testList.size());
		// Test beside a door direction LEFT
		testList = board.getAdjList(10, 16);
		assertTrue(testList.contains(board.getCellAt(10, 17)));
		assertTrue(testList.contains(board.getCellAt(10, 15)));
		assertTrue(testList.contains(board.getCellAt(9, 16)));
		assertTrue(testList.contains(board.getCellAt(11, 16)));
		assertEquals(4, testList.size());
		// Test beside a door direction UP
		testList = board.getAdjList(16, 3);
		assertTrue(testList.contains(board.getCellAt(17, 3)));
		assertTrue(testList.contains(board.getCellAt(15, 3)));
		assertTrue(testList.contains(board.getCellAt(16, 4)));
		assertTrue(testList.contains(board.getCellAt(16, 2)));
		assertEquals(4, testList.size());
	}

	// Test adjacency for a variety of walkway scenarios
	// These tests are LIGHT PURPLE on the planning spreadsheet
		//up to date for our board
	@Test
	public void testAdjacencyOfWalkways()
	{
		// Test on top edge of board, two walkway pieces
		LinkedList<BoardCell> testList = board.getAdjList(0, 7);
		assertTrue(testList.contains(board.getCellAt(1, 7)));
		assertTrue(testList.contains(board.getCellAt(0, 6)));
		assertEquals(2, testList.size());
		
		// Test on left edge of board, one walkway piece
		testList = board.getAdjList(18, 0);
		assertTrue(testList.contains(board.getCellAt(17, 0)));
		assertEquals(1, testList.size());

		// Test between two rooms, walkways right and left
		testList = board.getAdjList(6, 18);
		assertTrue(testList.contains(board.getCellAt(6, 19)));
		assertTrue(testList.contains(board.getCellAt(6, 17)));
		assertEquals(2, testList.size());

		// Test surrounded by 4 walkways
		testList = board.getAdjList(17, 8);
		assertTrue(testList.contains(board.getCellAt(16, 8)));
		assertTrue(testList.contains(board.getCellAt(18, 8)));
		assertTrue(testList.contains(board.getCellAt(17, 9)));
		assertTrue(testList.contains(board.getCellAt(17, 7)));
		assertEquals(4, testList.size());
		
		// Test on bottom edge of board, next to 1 room piece
		testList = board.getAdjList(22, 14);
		assertTrue(testList.contains(board.getCellAt(22, 15)));
		assertTrue(testList.contains(board.getCellAt(21, 14)));
		assertEquals(2, testList.size());
		
		// Test on right edge of board, next to 1 room piece
		testList = board.getAdjList(13, 22);
		assertTrue(testList.contains(board.getCellAt(14, 22)));
		assertTrue(testList.contains(board.getCellAt(13, 21)));
		assertEquals(2, testList.size());

		// Test on walkway next to  door that is not in the needed
		// direction to enter
		testList = board.getAdjList(18, 15);
		assertTrue(testList.contains(board.getCellAt(18, 16)));
		assertTrue(testList.contains(board.getCellAt(17, 15)));
		assertTrue(testList.contains(board.getCellAt(19, 15)));
		assertEquals(3, testList.size());
	}
	
	
	// Tests walkway targets for 1 step (essentially testing adjacency, also no doors near)
	// so only two are tested
	// These are LIGHT BLUE on the planning spreadsheet
		//up to date
	@Test
	public void testTargetsOneStep() {
		//walkway cell next to room cells
		board.calcTargets(2, 20, 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCellAt(3, 20)));
		assertTrue(targets.contains(board.getCellAt(2, 19)));	
		
		//walkway cell on edge
		board.calcTargets(8, 0, 1);
		targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCellAt(8, 1)));
		assertTrue(targets.contains(board.getCellAt(7, 0)));				
	}
	
	// Tests of walkway targets for 2 steps (no doors near)
	// These are LIGHT BLUE on the planning spreadsheet
		//up to date
	@Test
	public void testTargetsTwoSteps() {
		//targets for no doors/rooms near
		board.calcTargets(15, 9, 2);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCellAt(13, 9)));
		assertTrue(targets.contains(board.getCellAt(17, 9)));
		assertTrue(targets.contains(board.getCellAt(15 ,11)));
		assertTrue(targets.contains(board.getCellAt(15, 7)));
		assertTrue(targets.contains(board.getCellAt(14, 8)));
		assertTrue(targets.contains(board.getCellAt(14, 10)));
		assertTrue(targets.contains(board.getCellAt(16, 10)));
		assertTrue(targets.contains(board.getCellAt(16, 8)));
		
		//targets when room near but no door
		board.calcTargets(6, 21, 2);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(4, 21)));
		assertTrue(targets.contains(board.getCellAt(6, 19)));	
		assertTrue(targets.contains(board.getCellAt(5 ,22)));		
		assertTrue(targets.contains(board.getCellAt(5 ,20)));
	}
	
	// Tests of walkway targets for 4 steps (no doors near)
	// These are LIGHT BLUE on the planning spreadsheet
		//up to date
	@Test
	public void testTargetsFourSteps() {
		// Includes a path that doesn't have enough length
		board.calcTargets(17, 0, 4);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(15, 0)));
		assertTrue(targets.contains(board.getCellAt(16, 1)));
		assertTrue(targets.contains(board.getCellAt(15, 2)));
		assertTrue(targets.contains(board.getCellAt(16, 3)));
		
		board.calcTargets(18, 10, 4);
		targets= board.getTargets();
		assertEquals(11, targets.size());
		assertTrue(targets.contains(board.getCellAt(18, 6)));	
		assertTrue(targets.contains(board.getCellAt(17, 7)));	
		assertTrue(targets.contains(board.getCellAt(16, 8)));	
		assertTrue(targets.contains(board.getCellAt(15, 9)));	
		assertTrue(targets.contains(board.getCellAt(14, 10)));	
		assertTrue(targets.contains(board.getCellAt(19, 7)));	
		assertTrue(targets.contains(board.getCellAt(18, 8)));
		assertTrue(targets.contains(board.getCellAt(17, 9)));	
		assertTrue(targets.contains(board.getCellAt(16, 10)));	
		assertTrue(targets.contains(board.getCellAt(15, 11)));	
		assertTrue(targets.contains(board.getCellAt(20, 8)));	
	}	
	
	// Tests of walkway targets that includes a doorway exactly 6 steps away
	// These are LIGHT BLUE on the planning spreadsheet
		//up to date
	@Test
	public void testTargetsSixStepsWithDoorInRange() {
		board.calcTargets(3, 18, 6);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(13, targets.size());
		assertTrue(targets.contains(board.getCellAt(0, 17)));	
		assertTrue(targets.contains(board.getCellAt(2, 17)));
		assertTrue(targets.contains(board.getCellAt(6, 17)));	
		assertTrue(targets.contains(board.getCellAt(1, 18)));
		assertTrue(targets.contains(board.getCellAt(2, 19)));	
		assertTrue(targets.contains(board.getCellAt(4, 19)));
		assertTrue(targets.contains(board.getCellAt(6, 19)));	
		assertTrue(targets.contains(board.getCellAt(3, 20)));
		assertTrue(targets.contains(board.getCellAt(5, 20)));	
		assertTrue(targets.contains(board.getCellAt(4, 21)));
		assertTrue(targets.contains(board.getCellAt(6, 21)));
		assertTrue(targets.contains(board.getCellAt(3, 22)));//doorway that is 6 steps
		assertTrue(targets.contains(board.getCellAt(5, 22)));
	}	
	
	// Test getting into a room 2 steps away after rolling a 2
	// These are LIGHT BLUE on the planning spreadsheet
		//up to date
	@Test 
	public void testTargetsIntoRoomTwoStepsAwayRollTwo()
	{
		// One room is exactly 2 away
		board.calcTargets(12, 5, 2);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(12, 3))); //door
		assertTrue(targets.contains(board.getCellAt(12, 7)));
		assertTrue(targets.contains(board.getCellAt(11, 4)));
		assertTrue(targets.contains(board.getCellAt(11, 6)));
	}
	
	// Test rolling a 3 when a door is 2 steps away (should be able to enter)
	// These are LIGHT BLUE on the planning spreadsheet
		//up to date
	@Test
	public void testTargetsIntoRoomShortcut() 
	{
		board.calcTargets(6, 8, 3);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(13, targets.size());
		assertTrue(targets.contains(board.getCellAt(5, 6)));
		assertTrue(targets.contains(board.getCellAt(7, 6)));
		assertTrue(targets.contains(board.getCellAt(4, 7)));
		assertTrue(targets.contains(board.getCellAt(6, 7)));
		assertTrue(targets.contains(board.getCellAt(8, 7)));
		assertTrue(targets.contains(board.getCellAt(4, 8)));
		assertTrue(targets.contains(board.getCellAt(5, 8)));
		assertTrue(targets.contains(board.getCellAt(7, 8)));
		assertTrue(targets.contains(board.getCellAt(9, 8)));
		assertTrue(targets.contains(board.getCellAt(6, 9)));
		assertTrue(targets.contains(board.getCellAt(8, 9)));
		assertTrue(targets.contains(board.getCellAt(5, 10)));
		assertTrue(targets.contains(board.getCellAt(6, 11)));
		
	}

	// Test getting out of a room
	// These are LIGHT BLUE on the planning spreadsheet
		//up to date
	@Test
	public void testRoomExitTargets()
	{
		// Take one step out from a door, essentially just the adj list
		// but useful to make sure not exiting through walls
		// or through wrong door direction
		board.calcTargets(18, 14, 1);
		Set<BoardCell> targets= board.getTargets();
		System.out.println(targets.toString());
		System.out.println(board.getAdjList(18, 14).toString());
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCellAt(19, 14)));//should only be 1 below
		
		
		// Take two steps out from a door
		board.calcTargets(9, 17, 2);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(8, 16)));
		assertTrue(targets.contains(board.getCellAt(9, 15)));
		assertTrue(targets.contains(board.getCellAt(10, 16)));
	}

}