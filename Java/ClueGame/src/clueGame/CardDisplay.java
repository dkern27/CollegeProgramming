package clueGame;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class CardDisplay extends JPanel{

	public CardDisplay(Set<Card> cards) {
		setLayout(new GridLayout(3,1));
		setSize(50,500);
		setBorder(new TitledBorder(new EtchedBorder(), "My Cards"));
		add(createTextPanel("People", matchCards(cards, CardType.PERSON)));
		add(createTextPanel("Weapons", matchCards(cards, CardType.WEAPON)));
		add(createTextPanel("Rooms", matchCards(cards, CardType.ROOM)));
			
	}
	private JPanel createTextPanel(String panelName, ArrayList<Card> myCards) {
	 	JPanel panel = new JPanel();
		//Default layout is flow
	 	JTextArea field = new JTextArea(9,15); //Fills panel and enough space for longest card name
	 	for(Card c: myCards){
	 		field.append(c.getCardName()+"\n");
	 	}
	 	//Player cannot edit boxes, only for output
	 	field.setEditable(false);
		panel.add(field);
		panel.setBorder(new TitledBorder (new EtchedBorder(), panelName));
		return panel;
	}
	
	private ArrayList<Card> matchCards(Set<Card> cards, CardType type){
		ArrayList<Card> matchedCards = new ArrayList<Card>();
			for(Card c: cards){
				if(c.getType()==type)
					matchedCards.add(c);	
		}
		return matchedCards;	
	}

}
