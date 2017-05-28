package clueGame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame{

	private Board board;
	
	private static ControlPanel control;
	
	//Settings for first turn to be human player
	private static boolean humanTurnDone = true; 
	private int whoseTurn = Integer.MAX_VALUE; //Change to Integer.MAX_VALUE when everything implemented
	
	Random rand;
	

	public ClueGame() {
		
		setSize(800, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		board = new Board();
		board.initialize();
		add(board, BorderLayout.CENTER);
		
		control = new ControlPanel(this);
		add(control, BorderLayout.SOUTH);
		
		//Menu Bar
		JMenuBar menu = new JMenuBar();
		menu.add(createFileMenu());
		setJMenuBar(menu);
		
		//Displays player cards
		CardDisplay cardDisplay = new CardDisplay(board.getPlayers().get(0).getMyCards());
		add(cardDisplay, BorderLayout.EAST);
		
		rand = new Random();
		
	}
	
	public JMenu createFileMenu(){
		JMenu menu = new JMenu("File");
		JMenuItem detNotes = new JMenuItem("Detective Notes");
		JMenuItem exit = new JMenuItem("Exit");
		
		//Opens detective notes
		class menuItemListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				if (e.getSource() == detNotes){
					DetectiveNotes dialog = new DetectiveNotes(board);
					dialog.setVisible(true);
				}
				else if(e.getSource() == exit)
					System.exit(0);
				
			}
		}
		
		menuItemListener l = new menuItemListener();
		
		detNotes.addActionListener(l);
		menu.add(detNotes);
		
		exit.addActionListener(l);
		menu.add(exit);
		
		return menu;
	}
	
	//Moves to next player and executes their turn
	public void nextTurn(){
		if(humanTurnDone == true){
			if(whoseTurn < Board.numPlayers-1) //Because index starts at 0
				whoseTurn++;
			else{
				whoseTurn=0;
				humanTurnDone = false;
			}
			int roll = rollDie();
			Player p = board.getPlayers().get(whoseTurn);
			//Updates displays
			ControlPanel.updateField(0, p.getPlayerName());
			ControlPanel.updateField(1, Integer.toString(roll));
			
			
			board.calcTargets(p.getRow(), p.getColumn(), roll);
			
			p.makeMove(board);
		}
		else{
			JOptionPane.showMessageDialog(this, "Finish your turn to advance!");
		}
	}
	
	public void processAccusation(Solution solution){
		if(board.checkAccusation(solution)){
			JOptionPane.showMessageDialog(this, board.getPlayers().get(whoseTurn).getPlayerName() + " solved the mystery!", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}
		else{
			JOptionPane.showMessageDialog(this, board.getPlayers().get(whoseTurn).getPlayerName() + "'s accusation was incorrect.");
		}
	}
	
	//Helper function to roll dice
	public int rollDie(){
		//Roll from 0 to 5 and add 1;
		return rand.nextInt(6)+1;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//Getters and Setters
	public static boolean isHumanTurnDone() {
		return humanTurnDone;
	}
	
	
	public static void setHumanTurnDone(boolean b){
		humanTurnDone = b;
	}
	
	
	public Board getBoard(){
		return board;
	}
	
	public static ControlPanel getControl() {
		return control;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//MAIN
	//////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		ClueGame gameBoard = new ClueGame();
		gameBoard.setVisible(true);
		//Splash Screen
		JOptionPane.showMessageDialog(gameBoard, "You are \"" + gameBoard.board.getPlayers().get(0).getPlayerName() +"\". Press 'Next Player' to begin playing.","Welcome to Clue!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("DWAYNE.png"));
	}

}
