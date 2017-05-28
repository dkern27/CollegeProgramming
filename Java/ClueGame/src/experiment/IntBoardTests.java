package experiment;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Set;

import org.junit.*;

//tests to ensure adjacent cells are calculated properly and valid moves are calculated properly
//NOTE: Should fail all currently, actual methods not implemented yet, only skeletons
//Once the methods called on are implemented correctly, the tests should pass

public class IntBoardTests {
	
	private IntBoard board;
	
	@Before
	public void setUpIntBoard(){
		board = new IntBoard(4,4);//tests only good for this specific board size
		board.calcAdjacencies();
	}
	
	@Test
	public void testAdjacencyTopLeft() {
		BoardCell cell = board.getCell(0, 0);
		LinkedList<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertEquals(2, testList.size());	
	}
	
	@Test
	public void testAdjacencyBottomRight() {
		BoardCell cell = board.getCell(3, 3);
		LinkedList<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(3,2)));
		assertTrue(testList.contains(board.getCell(2,3)));
		assertEquals(2, testList.size());	
	}
	
	@Test
	public void testAdjacencyRightEdge() {
		BoardCell cell = board.getCell(1, 3);
		LinkedList<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(0,3)));
		assertTrue(testList.contains(board.getCell(1,2)));
		assertTrue(testList.contains(board.getCell(2,3)));
		assertEquals(3, testList.size());	
	}
	
	@Test
	public void testAdjacencyLeftEdge() {
		BoardCell cell = board.getCell(2,0);
		LinkedList<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1,0)));
		assertTrue(testList.contains(board.getCell(2,1)));
		assertTrue(testList.contains(board.getCell(3,0)));
		assertEquals(3, testList.size());	
	}
	
	@Test
	public void testAdjacencySecondColumnMiddleOfGrid() {
		BoardCell cell = board.getCell(1, 1);
		LinkedList<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertEquals(4, testList.size());	
	}
	
	@Test
	public void testAdjacencySecondFromLastColumnMiddleOfGrid() {
		BoardCell cell = board.getCell(2, 2);
		LinkedList<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertEquals(4, testList.size());	
	}

	//starting at cell 2,3 test the paths if rolled a 1
	@Test
	public void testTargets23_1() {
		BoardCell cell = board.getCell(2, 3);
		board.calcTargets(cell, 1);
		Set<BoardCell> targets = board.getTargets();

		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(1, 3)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(3, 3)));	
	}
	
	//starting at cell 1,0 test the paths if rolled a 2
	@Test
	public void testTargets10_2() {
		BoardCell cell = board.getCell(1, 0);
		board.calcTargets(cell, 2);
		Set<BoardCell> targets = board.getTargets();
		
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
	}
	
	//starting at cell 0,0 test the paths if rolled a 3
	@Test
	public void testTargets00_3() {
		BoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 3);
		Set<BoardCell> targets = board.getTargets();
		
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 0)));	
	}
	
	//starting at cell 1,2 test the paths if rolled a 4
	@Test
	public void testTargets12_4() {
		BoardCell cell = board.getCell(1, 2);
		board.calcTargets(cell, 4);
		Set<BoardCell> targets = board.getTargets();
		
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(2, 3)));
		assertTrue(targets.contains(board.getCell(3, 0)));	
		assertTrue(targets.contains(board.getCell(3, 2)));
	}
	
	//starting at cell 3,3 test the paths if rolled a 5
	@Test
	public void testTargets33_5() {
		BoardCell cell = board.getCell(3, 3);
		board.calcTargets(cell, 5);
		Set<BoardCell> targets = board.getTargets();
		
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(2, 3)));	
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(3, 2)));
	}
	
	//starting at cell 0,0 test the paths if rolled a 6
	@Test
	public void testTargets00_6() {
		BoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 6);
		Set<BoardCell> targets = board.getTargets();
		
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertTrue(targets.contains(board.getCell(3, 3)));
	}
}
