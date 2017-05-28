package clueTests;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Set;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.DoorDirection;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

public class GameActionTests {

	private static Board board;
	@BeforeClass
	public static void setUp() {
		board = new Board();
		board.initialize();
	}
	
	
	//test for checking accusations
	//Checks correct solution and incorrect solutions with one of each type
	@Test
	public void testAccusation(){
		board.getTheAnswer().person = "Laith Haddad";
		board.getTheAnswer().room = "Movie theatre";
		board.getTheAnswer().weapon = "Spork";
		
		//Check for correct solution
		Solution guess = new Solution("Laith Haddad", "Movie theatre", "Spork");
		assertTrue(board.checkAccusation(guess));
		//Check for incorrect solution based one of each being wrong
		guess.person="Pikachu";
		assertFalse(board.checkAccusation(guess));
		guess.person="Laith Haddad";
		guess.room = "Laundry Room";
		assertFalse(board.checkAccusation(guess));
		guess.room = "Movie theatre";
		guess.weapon = "Cardboard Box";
		assertFalse(board.checkAccusation(guess));
	}
	
	//Some tests for selecting a target location
	@Test
	public void testRandomTargetLocation(){
		ComputerPlayer player = new ComputerPlayer("player", 0, 0, Color.BLACK);
		
		//Tests random choice if no room is on list
		board.calcTargets(15, 0, 2);
		int loc_15_2 = 0, loc_17_0 = 0, loc_16_1 = 0;
		for (int i=0;i<100;i++){
			BoardCell selected = player.pickLocation(board.getTargets());
			if (selected == board.getCellAt(15, 2))
				loc_15_2++;
			else if (selected == board.getCellAt(17, 0))
				loc_17_0++;
			else if (selected == board.getCellAt(16, 1))
				loc_16_1++;
			else
				fail("Invalid target selected");
		}
		assertTrue(loc_15_2>10);
		assertTrue(loc_17_0>10);
		assertTrue(loc_16_1>10);
		
		//Test for room always chosen if it was not just visited
		board.calcTargets(6, 8, 3);
		int roomChosen=0;
		for(int i=0; i<25;i++){
			//Reset last room to be a walkway
			player.setPrevLocation('W');
			if(player.pickLocation(board.getTargets()) == board.getCellAt(4, 8))
				roomChosen++;
		}
		//room should be chosen all 25 times
		assertTrue(roomChosen==25);
		
		
		//Test for random choice when room is included and it was just visited
		board.calcTargets(0, 13, 2);
		int loc_0_11=0,loc_1_12=0,loc_1_14=0,loc_2_13=0;
		
		for(int i=0;i<200;i++){
			//Set last room to be same room on list
			player.setPrevLocation('K');
			BoardCell selected = player.pickLocation(board.getTargets());
			if(selected == board.getCellAt(0, 11))
				loc_0_11++; //room 'K'
			else if (selected == board.getCellAt(1, 12))
				loc_1_12++;
			else if (selected == board.getCellAt(1, 14))
				loc_1_14++;
			else if (selected == board.getCellAt(2, 13))
				loc_2_13++;
			else
				fail("Invalid target selected");
		}
		
		assertTrue(loc_0_11>25);
		assertTrue(loc_1_12>25);
		assertTrue(loc_1_14>25);
		assertTrue(loc_2_13>25);
		
		//Test random choice of rooms if two rooms are within range and neither visited recently
		board.calcTargets(20, 15, 4);
		int roomS = 0, roomE = 0;
		
		for(int i=0; i<100; i++){
			player.setPrevLocation('W');
			BoardCell selected = player.pickLocation(board.getTargets());
			if(selected == board.getCellAt(22, 17))
				roomE++;
			if(selected == board.getCellAt(18, 14))
				roomS++;
		}
		
		assertTrue(roomE>25);
		assertTrue(roomS>25);
		
		
	}
	
	
	//Some tests for disproving a suggestion
	@Test
	public void testDisprovingSuggestion(){
		//Set up players and cards
		ArrayList <Player> players = new ArrayList<Player>();
		
		players.add(new ComputerPlayer("1",0,0,Color.BLACK));
		players.add(new HumanPlayer("2",0,0,Color.BLACK));
		players.add(new ComputerPlayer("3",0,0,Color.BLACK));
		players.add(new ComputerPlayer("4",0,0,Color.BLACK));
		
		//Hand out specific cards
		players.get(0).giveCard(new Card("Rhinoceros",CardType.WEAPON));
		players.get(0).giveCard(new Card("Pikachu",CardType.PERSON));
		players.get(0).giveCard(new Card("Laundry Room",CardType.ROOM));
		
		board.setPlayers(players);
		
		//Disprove with person
		Card card = players.get(0).disproveSuggestion(new Solution("Pikachu", "Office", "Spork"));
		assertTrue(card.equals(new Card("Pikachu",CardType.PERSON)));
		
		//Disprove with room
		card = players.get(0).disproveSuggestion(new Solution("RFC", "Laundry Room", "Spork"));
		assertTrue(card.equals(new Card("Laundry Room", CardType.ROOM)));
		
		//Disprove with Weapon
		card = players.get(0).disproveSuggestion(new Solution("RFC", "Office", "Rhinoceros"));
		assertTrue(card.equals(new Card("Rhinoceros",CardType.WEAPON)));
		
		//No cards found
		card = players.get(0).disproveSuggestion(new Solution("RFC", "Office", "Spork"));
		assertEquals(card, null);
		
		//Checks for random choice if multiple cards in a hand match the suggestion
		int personCount=0,roomCount=0,weaponCount=0;
		for(int i=0; i<100; i++){
			card = players.get(0).disproveSuggestion(new Solution("Pikachu", "Laundry Room", "Rhinoceros"));
			if(card.getCardName().equals("Pikachu"))
				personCount++;
			if(card.getCardName().equals("Laundry Room"))
				roomCount++;
			if(card.getCardName().equals("Rhinoceros"))
				weaponCount++;
		}
		assertTrue(personCount > 20 && roomCount > 20 && weaponCount > 20);
		
		//Set the solution
		board.getTheAnswer().person = "Laith Haddad";
		board.getTheAnswer().room = "Exercise Room";
		board.getTheAnswer().weapon = "Spoon";
		
		//Give each player some cards
		players.get(1).giveCard(new Card("Enrique Iglesias",CardType.PERSON));
		players.get(1).giveCard(new Card("Swimming Pool",CardType.ROOM));
		players.get(1).giveCard(new Card("Quantum Mechanics",CardType.WEAPON));
		
		players.get(2).giveCard(new Card("Jennifer Lopez",CardType.PERSON));
		players.get(2).giveCard(new Card("Movie theatre",CardType.ROOM));
		players.get(2).giveCard(new Card("Spork",CardType.WEAPON));
		
		players.get(3).giveCard(new Card("RFC",CardType.PERSON));
		players.get(3).giveCard(new Card("Office",CardType.ROOM));
		players.get(3).giveCard(new Card("Airplane",CardType.WEAPON));
		
		board.setPlayers(players);
		
		//No one can disprove
		card=board.handleSuggestion(new Solution("Laith Haddad", "Exercise Room", "Spoon"), players.get(0).getPlayerName());
		assertEquals(card, null);
		
		//Only human player can disprove
		card=board.handleSuggestion(new Solution("Enrique Iglesias", "Exercise Room", "Spoon"), players.get(0).getPlayerName());
		assertTrue(card.equals(new Card("Enrique Iglesias",CardType.PERSON)));
		
		//Only accusing Player can disprove
		card=board.handleSuggestion(new Solution("Laith Haddad", "Exercise Room", "Spoon"), players.get(0).getPlayerName());
		assertEquals(card, null);
		
		//players 3 and 4 have a card, returned card should be from player 3
		card=board.handleSuggestion(new Solution("Laith Haddad", "Movie theatre", "Airplane"), players.get(0).getPlayerName());
		assertTrue(card.equals(new Card("Movie theatre", CardType.ROOM)));
		
		//Checks if all players are queried, player 4 has card
		card=board.handleSuggestion(new Solution("Laith Haddad", "Exercise Room", "Airplane"), players.get(0).getPlayerName());
		assertTrue(card.equals(new Card("Airplane",CardType.WEAPON)));	
	}
	
	
	//tests for making a suggestion
	@Test 
	public void testMakeSuggestion(){
		ComputerPlayer player = new ComputerPlayer("Test",4,8,Color.BLUE);
		Set <Card> seenCards = player.getSeenCards();
		
		Solution suggestion = player.makeSuggestion(board, new BoardCell(4,8,'K',DoorDirection.DOWN, false));
		
		//Check to see if room guess matches room player is in
		String room = suggestion.room;
		String location = Board.getRooms().get(board.getCellAt(player.getRow(),player.getColumn()).getInitial());
		
		assertTrue(room.equals(location));
		
		//Checks if guess is not a seen card
		//room can be suggested if already seen
		for(Card c: seenCards){
			assertNotEquals(c.getCardName(), suggestion.person);
			assertNotEquals(c.getCardName(), suggestion.weapon);
		}
	}
	
}
