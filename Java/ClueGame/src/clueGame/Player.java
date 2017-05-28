package clueGame;


import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class Player {
	
	private String playerName;
	private int row;
	private int column;
	private int xPix, yPix;
	private static final int DIAMETER = 25;
	private Color color;
	private Set <Card> myCards = new HashSet<Card>();
	protected Set <Card> seenCards = new HashSet<Card>();

	public Player(String name, int row, int col, Color color) {
		playerName=name;
		this.row=row;
		this.column=col;
		this.color = color;
		xPix = col*DIAMETER;
		yPix = row*DIAMETER;
	}
	
	public abstract void makeMove(Board board);
	
	//Tests to see if the suggestion is correct or not. If incorrect, one of the cards that disproves the 
	//suggestion is randomly returned. Otherwise null is returned
	public Card disproveSuggestion(Solution suggestion) {
		//Cards that disprove the suggestion are added to the arraylist cards
		ArrayList<Card> cards = new ArrayList<Card>();

		for( Card c : this.getMyCards() ) {
			if(suggestion.contains(c.getCardName()))
				cards.add(c);
		}
		
		//If cards is not empty, one of the cards within the arraylist is randomly returned
		if(cards.size() > 0) {
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(cards.size());
			return cards.get(index);
		}
		//Null is returned if no cards in cards
		else {
			return null;
		}
	}
	
	//Checks to see if card has been seen by the player or not. If not, adds to the seenCards set
	public void addSeenCard( Card card ) {
		if(!seenCards.contains(card)) {
			seenCards.add(card);
		}
	}
	
	//Draws colored oval representing the player
	public void draw(Graphics g){
		g.setColor(this.color);
		g.fillOval(xPix, yPix, DIAMETER, DIAMETER);
	}
	

	//Getters
	public String getPlayerName() {
		return playerName;
	}

	public int getRow() {
		return row;
	}
	
	public void setRow(int r) {
		row = r;
	}

	public int getColumn() {
		return column;
	}
	
	public void setColumn(int c) {
		column = c;
	}

	public Color getColor() {
		return color;
	}

	public Set <Card> getMyCards() {
		return myCards;
	}
	
	public Set<Card> getSeenCards(){
		return seenCards;
	}
	
	public void giveCard(Card c){
		myCards.add(c);
		seenCards.add(c);
	}
	
	public void setRowCol(int row, int col){
		this.row = row;
		this.column = col;
		xPix = col * DIAMETER;
		yPix = row * DIAMETER;
	}


	
}
