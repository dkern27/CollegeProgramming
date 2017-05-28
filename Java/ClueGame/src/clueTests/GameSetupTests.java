package clueTests;

import static org.junit.Assert.*;
import clueGame.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


public class GameSetupTests {

	private Board board;
	
	@Before
	public void setUp() {
		board = new Board();
		board.initialize();
	}
	
	
	//Some tests for loading people
	//This tests that a human player has a name, color, and starting location
	@Test
	public void loadHumanPlayer() {
		//Human player is always first in list
		Player human = board.getPlayers().get(0);
		//Checks each instance variable
		assertEquals("Dwayne 'The Rock' Johnson", human.getPlayerName());
		assertEquals(0,human.getRow());
		assertEquals(6,human.getColumn());
		assertEquals(Color.RED, human.getColor());
	}
	
	//This tests that two computer players each have a name, color, and starting location
	@Test
	public void loadComputerPlayer() {
		Player comp1 = board.getPlayers().get(1);
		assertEquals("Enrique Iglesias",comp1.getPlayerName());
		assertEquals(0,comp1.getRow());
		assertEquals(14,comp1.getColumn());
		assertEquals(Color.BLUE, comp1.getColor());
		
		
		Player comp2 = board.getPlayers().get(4);
		assertEquals("RFC",comp2.getPlayerName());
		assertEquals(22,comp2.getRow());
		assertEquals(7,comp2.getColumn());
		assertEquals(Color.ORANGE, comp2.getColor());
		
		assertTrue(board.getPlayers().contains(comp2));
	}
	
	
	//Some tests for loading cards
	//Tests the total number of cards, the number of each type of card, and that the deck contains a random selected card for each type
	@Test
	public void loadCards() {
		ArrayList<Card> cards = board.getCards();
		assertEquals(21, cards.size());
		
		//Checking if all cards are loaded correctly
		int person=0;
		int weapon=0;
		int room=0;
		for( Card card : cards ) {
			if(card.getType() == CardType.PERSON ) {
				person++;
			}
			if(card.getType() == CardType.WEAPON ) {
				weapon++;
			}
			if(card.getType() == CardType.ROOM ) {
				room++;
			}
		}
		//Checks right number of each card
		assertEquals(6,person);
		assertEquals(6,weapon);
		assertEquals(9,room);
		
		//Get Pikachu
		Card exPerson = cards.get(2);
		assertEquals(exPerson.getCardName(), "Pikachu");
		assertEquals(exPerson.getType(), CardType.PERSON);
		
		//Get Rhinoceros
		Card exWeapon = cards.get(20);
		assertEquals(exWeapon.getCardName(), "Rhinoceros");
		assertEquals(exWeapon.getType(), CardType.WEAPON);
		
		//Get pantry
		Card exRoom = cards.get(8);
		assertEquals(exRoom.getCardName(), "Pantry");
		assertEquals(exRoom.getType(), CardType.ROOM);
	}
	
	
	
	//Some tests for dealing cards
	@Test
	public void dealCards() {
		//Load the deck of cards and the list of players
		ArrayList<Card> cards = board.getCards();
		ArrayList<Player> players = board.getPlayers();
		
		//Determine how many cards each player should have
		int cardsPerPlayer = cards.size() / players.size();
		
		//Check each players cards
		for( Player p : players ) {
			Set<Card> playerCards = p.getMyCards();
			
			//Check if the player has an acceptable number of cards, +- 1 from cardsPerPlayer
			assertTrue( playerCards.size() >= (cardsPerPlayer - 1) && playerCards.size() <= (cardsPerPlayer + 1) );
			
			for( Card c : playerCards ) {
				//Check that the card is in the deck, if not means duplicate
				assertTrue( cards.contains(c) );
				
				//Assuming the card is in the deck, remove it from the list
				cards.remove(c);
			}
		}
		
		//Only three cards should be left which are the solution
		assertEquals(3,cards.size());
		
		Solution solution = board.getTheAnswer();
		
		for( Card c : cards ) {
			switch(c.getType()) {
			case PERSON: assertEquals(solution.person, c.getCardName());
			break;
			case ROOM: assertEquals(solution.room, c.getCardName());
			break;
			case WEAPON: assertEquals(solution.weapon, c.getCardName());
			break;
			}
			
		}
	}
	
}
