package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import javax.swing.JOptionPane;

public class ComputerPlayer extends Player {

	private char prevLocation='W';
	
	public ComputerPlayer(String name, int row, int col, Color color) {
		super(name, row, col, color);
	}

	public BoardCell pickLocation(Set<BoardCell> targets) {

		BoardCell[] targetArray = targets.toArray(new BoardCell[targets.size()]);

		Random randomGenerator = new Random();

		for (int i = targetArray.length - 1; i > 0; i--)
		{
			int index = randomGenerator.nextInt(i + 1);
			BoardCell temp = targetArray[index];
			targetArray[index] = targetArray[i];
			targetArray[i] = temp;
		}

		for( BoardCell bc : targetArray ) {
			if( bc.isDoorway() && bc.getInitial() != prevLocation ) {
				return bc;
			}
		}

		return targetArray[0];
	}
	
	public void makeAccusation(Board board, Solution suggestion){
		if(board.checkAccusation(suggestion)){
			JOptionPane.showMessageDialog(null, getPlayerName() + " solved the mystery!", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}
		else{
			JOptionPane.showMessageDialog(null, getPlayerName() + "'s accusation was incorrect.");
		}
	}
	
	public Solution makeSuggestion(Board board, BoardCell location) {
		String room = Board.getRooms().get(location.getInitial());
		String weapon = "";
		String person = "";
		Random randomGenerator = new Random();
		ArrayList<Card> possible = new ArrayList<Card>();
		possible.addAll(board.getCards());
		for(Card c: seenCards)
		{
			for(int i = 0; i < possible.size();i++)
			{
				if(c.getCardName().equals(possible.get(i).getCardName()))
				{
					possible.remove(i);
				}
			}
		}
		
		int k = 0;
		while(weapon.equals("") || person.equals(""))
		{
			k = randomGenerator.nextInt(possible.size());
			if(possible.get(k).getType() == CardType.PERSON) {
				person = possible.get(k).getCardName();
			}
			if(possible.get(k).getType() == CardType.WEAPON) {
				weapon = possible.get(k).getCardName();
			}
		}
		return new Solution(person,room,weapon);
	}
	
	
	public void setPrevLocation(char c){
		prevLocation = c;
	}

	public void makeMove(Board board) {
		ControlPanel.updateField(2,"");
		ControlPanel.updateField(3,"");
		ControlPanel.updateField(4,"");
		ControlPanel.updateField(5,"");
		//if last suggestion had not been disproved, makes an accusation
		if (!board.getDisprovedSuggestion()) {
			makeAccusation(board,board.getLastSuggestion());
			board.setDisprovedSuggestion(true);
		}
		//Pick a target to move to and set the location to where the boardcell is located
		BoardCell newLocation = pickLocation(board.getTargets());
		//Set the previous location so the computer player will not travel to the same
		//room repeatedly
		setPrevLocation(board.getCellAt(getRow(), getColumn()).getInitial());
		super.setRowCol(newLocation.getRow(),newLocation.getColumn());
		board.getTargets().clear();
		board.repaint();
		//If moved to a room, makes a suggestion
		if (newLocation.isWalkWay() == false) {
			board.setLastSuggestion(makeSuggestion(board, newLocation));
			ControlPanel.updateField(2,board.getLastSuggestion().getPerson());
			ControlPanel.updateField(3,board.getLastSuggestion().getWeapon());
			ControlPanel.updateField(4,board.getLastSuggestion().getRoom());
			Card proof = board.handleSuggestion(board.getLastSuggestion(), getPlayerName());
			if (proof != null) {
				ControlPanel.updateField(5,proof.getCardName());
			}
			else {
				ControlPanel.updateField(5,"No Card Shown");
				board.setDisprovedSuggestion(false);}
			board.updatePlayerLocation(this, board.getLastSuggestion().getPerson());
			board.repaint();			
		}
	}
}
