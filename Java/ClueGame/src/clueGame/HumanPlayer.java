package clueGame;

import java.awt.Color;

public class HumanPlayer extends Player {
	
	public HumanPlayer(String name, int row, int col, Color color) {
		super(name, row, col, color);
	}
	
	public void makeMove(Board board){
		//Draw highlighted targets
		board.repaint();
		//Let player move and allows suggestion if possible
		board.moveAndSuggest(this);
	}
	
	//Opens suggestion dialog and checks for made suggestion
	public void makeSuggestion(Board board){
		if(board.getCellAt(getRow(), getColumn()).getInitial() != 'W'){
			SuggestionDialog suggestionDialog = new SuggestionDialog(board, this);
			suggestionDialog.setVisible(true);
		
			if(suggestionDialog.getSubmitted()){
				board.updatePlayerLocation(this, suggestionDialog.getPerson());
				
				ControlPanel.updateField(2, suggestionDialog.getPerson());
				ControlPanel.updateField(3, suggestionDialog.getWeapon());
				ControlPanel.updateField(4, suggestionDialog.getRoom());
				
				Card c = board.handleSuggestion(new Solution(suggestionDialog.getPerson(), suggestionDialog.getRoom(), suggestionDialog.getWeapon()), getPlayerName());
				if(c!=null){
					ControlPanel.updateField(5,  c.getCardName());
				}
				else{
					ControlPanel.updateField(5, "No Card Shown");
				}
			}
		}
		ClueGame.setHumanTurnDone(true);
	}
	


}
