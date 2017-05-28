package clueGame;

public class Card {
	
	private String cardName;
	private CardType cardType;

	public Card(String name, CardType type) {
		cardName = name;
		cardType = type;	
	}


	public boolean equals(){
		return false;
	}
	
	public String getCardName() {
		return cardName;
	}

	public CardType getType() {
		return cardType;
	}
	
	public Card clone(){
		Card c = new Card(cardName,cardType);
		return c;
	}
	
	public boolean equals(Card c){
		if(c.getCardName().equals(this.cardName) && c.getType()==this.cardType)
			return true;
		return false;
	}

}
