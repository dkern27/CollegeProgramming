package clueTests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;

//Tests modeled directly after Cyndi's tests. Designed to work for OUR specific board configuration

public class ST_FileInitTests {
	// specific board setup for our board 23x23 with 11 rooms
	public static final int NUM_ROOMS = 11;
	public static final int NUM_ROWS = 23;
	public static final int NUM_COLUMNS = 23;

	//Only set up board one time (except for config testing)
	private static Board board;
	
	@BeforeClass
	public static void setUp() {
		//create board using our layout/legend files
		board = new Board("Configs/Layouts/ClueLayout_ST_DH.csv", "Configs/Legends/ClueLegend_ST_DH.txt");
		// Initialize will load BOTH config files 
		board.initialize();
	}
	
	@Test
	public void testRooms() {
		// rooms is static, see discussion in lab writeup
		Map<Character, String> rooms = Board.getRooms();

		//make sure room legend lines up with provided file
		//check that size matches as well as each char mapping to a room matching

		assertEquals(NUM_ROOMS, rooms.size());
		assertEquals("Laundry room", rooms.get('L'));
		assertEquals("Kitchen", rooms.get('K'));
		assertEquals("Pantry", rooms.get('P'));
		assertEquals("Bathroom", rooms.get('B'));
		assertEquals("Game room", rooms.get('G'));
		assertEquals("Office", rooms.get('O'));
		assertEquals("Movie theatre", rooms.get('M'));
		assertEquals("Swimming Pool", rooms.get('S'));
		assertEquals("Exercise room", rooms.get('E'));
		assertEquals("Closet", rooms.get('X'));
		assertEquals("Walkway", rooms.get('W'));
	}
	
	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());		
	}
	
	//Check if rooms on board know their intended orientation
	//also check that certain cells know their correct identity (room, doorway, walkway etc)
	//currently isDoorway(), isWalkWay(), isRoom() always returns true, 
	@Test
	public void FourDoorDirections() {
		
		// Test one each RIGHT/LEFT/UP/DOWN
		BoardCell room = board.getCellAt(0,11);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		
		room = board.getCellAt(4,8);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.DOWN, room.getDoorDirection());
		
		room = board.getCellAt(22,17);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());
		
		room = board.getCellAt(16,14);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.UP, room.getDoorDirection());
		
		// Test that room pieces know their correct identity
		room = board.getCellAt(20,3);
		assertFalse(room.isDoorway());	
		
		// Test that walkways are not doors or rooms
		BoardCell cell = board.getCellAt(11,9);
		assertFalse(cell.isDoorway());	

	}
	
	// Test that we have the correct number of doors (17 for our file)
	// and also test that the board size matches up
	@Test
	public void testNumberOfDoorways() 
	{
		int numDoors = 0;
		int totalCells = board.getNumColumns() * board.getNumRows();
		Assert.assertEquals(NUM_ROWS * NUM_COLUMNS, totalCells);
		for (int row=0; row<board.getNumRows(); row++)
			for (int col=0; col<board.getNumColumns(); col++) {
				BoardCell cell = board.getCellAt(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(17, numDoors);
	}

	// Test room cells to ensure the room initial is correct.
	@Test
	public void testRoomInitials() {
		assertEquals('L', board.getCellAt(0, 0).getInitial());
		assertEquals('K', board.getCellAt(1, 9).getInitial());
		assertEquals('P', board.getCellAt(3, 16).getInitial());
		assertEquals('B', board.getCellAt(0, 22).getInitial());
		assertEquals('G', board.getCellAt(11, 0).getInitial());
		assertEquals('O', board.getCellAt(10, 22).getInitial());
		assertEquals('M', board.getCellAt(20, 3).getInitial());
		assertEquals('S', board.getCellAt(16, 13).getInitial());
		assertEquals('E', board.getCellAt(20, 21).getInitial());
		assertEquals('X', board.getCellAt(11, 13).getInitial());
	}
	
	//the following tests come directly from the provided tests (not much wiggle room to change how to test if an exception is thrown
	//during board loading). Note that we do not use the @before board that was created, instead we create a new one without the 
	//default config files in order to test some bad config files. The exceptions are thrown by the load config methods so intialize() 
	//is not necessary to call
	
	// Test that an exception is thrown for a bad board config file (# columns for each row dont match)
	@Test (expected = BadConfigFormatException.class)
	public void testBadColumns() throws BadConfigFormatException, FileNotFoundException{
		Board board = new Board("Configs/Layouts/ClueLayoutBadColumns_ST_DH.csv", "Configs/Legends/ClueLegend_ST_DH.txt");
		board.loadRoomConfig();
		board.loadBoardConfig();
	}
	
	// Test that an exception is thrown for a bad board config file (# rows for each column dont match)
	//redundant because same as test above...derp
	@Test (expected = BadConfigFormatException.class)
	public void testBadRows() throws BadConfigFormatException, FileNotFoundException {
		Board board = new Board("Configs/Layouts/ClueLayoutBadRows_ST_DH.csv", "Configs/Legends/ClueLegend_ST_DH.txt");
		board.loadRoomConfig();
		board.loadBoardConfig();
	}
	
	// Test that an exception is thrown for a bad board config file (has a room not in legend)
	@Test (expected = BadConfigFormatException.class)
	public void testBadRoom() throws BadConfigFormatException, FileNotFoundException {
		Board board = new Board("Configs/Layouts/ClueLayoutBadRoom_ST_DH.csv", "Configs/Legends/ClueLegend_ST_DH.txt");
		board.loadRoomConfig();
		board.loadBoardConfig();
	}
	// Test that an exception is thrown if the format for the legend is incorrect (Initial, Room, Card)
	@Test (expected = BadConfigFormatException.class)
	public void testBadRoomFormat() throws BadConfigFormatException, FileNotFoundException {
		Board board = new Board("Configs/Layouts/ClueLayout_ST_DH.csv", "Configs/Legends/ClueLegendBadFormat_ST_DH.txt");
		board.loadRoomConfig();
	}
}