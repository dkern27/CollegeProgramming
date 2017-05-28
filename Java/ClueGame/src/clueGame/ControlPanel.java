package clueGame;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ControlPanel extends JPanel {
	
	ClueGame game;
	
	private static ArrayList<JTextField> fields = new ArrayList<JTextField>();
	//Arranged: Whose Turn, Roll, Person, Weapon, Room, Response

	public ControlPanel(ClueGame game)
	{	
		this.game = game;
		//Default layout is flow layout
		//Control panel consists of three separate panels
		
		//GridBagLayout Panel that holds two other panels for each section
		
		//Includes "Whose Turn" and "Roll" Boxes.
		JPanel panel1 = new JPanel(new GridBagLayout());
		GridBagConstraints constraint1 = new GridBagConstraints();
		JPanel panel = createTextPanel("Whose Turn?", 15);
		panel1.add(panel);
		panel = createTextPanel("Roll", 5);
		constraint1. gridy = 1;
		constraint1.anchor = GridBagConstraints.LINE_START;
		panel1.add(panel, constraint1);
		add(panel1);
		
		//Includes "Guess" and "Response" Boxes
		JPanel panel2 = new JPanel(new GridBagLayout());
		GridBagConstraints constraint2 = new GridBagConstraints();
		panel = createThreeTextPanel("Guess","Person", "Weapon", "Room", 20);
		panel2.add(panel);
		panel = createTextPanel("Response", 20);
		constraint2.gridy = 1;
		constraint2.anchor = GridBagConstraints.LINE_START;
		panel2.add(panel, constraint2);
		add(panel2);
		
		//Includes "Next Player" and "Make an accusation" Buttons
		JPanel panel3 = new JPanel(new GridLayout(2,1));

		JButton nextPlayerButton = new JButton("Next Player");
		nextPlayerButton.setPreferredSize(new Dimension(150,50));
		
		JButton accusationButton = new JButton("Make an Accusation");
		accusationButton.setPreferredSize(new Dimension(150,50));
		
		class buttonListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				if(e.getSource()==nextPlayerButton){
					game.nextTurn();
				}
				else {
					if(ClueGame.isHumanTurnDone() == false){
						AccusationDialog accusationDialog = new AccusationDialog(game.getBoard());
						accusationDialog.setVisible(true);
						
						if(accusationDialog.getSubmitted()){
							ClueGame.setHumanTurnDone(true);
							game.getBoard().getTargets().clear();
							game.getBoard().repaint();
							game.getBoard().setMouseListenerActive(false);
							game.processAccusation(new Solution(accusationDialog.getPerson(), accusationDialog.getRoom(), accusationDialog.getWeapon()));
						}
					}
					else{
						JOptionPane.showMessageDialog(game.getBoard(), "You can only make an accusation at the beginning of your turn!");
					}
				}
			}
		}
		
		buttonListener buttonPress = new buttonListener();
		
		nextPlayerButton.addActionListener(buttonPress);
		accusationButton.addActionListener(buttonPress);
		
		panel = new JPanel();
		panel.add(nextPlayerButton);
		panel3.add(panel);
		panel = new JPanel();
		panel.add(accusationButton);
		panel3.add(panel);
		add(panel3);
	}

	//Creates a single text field
	//Parameters:
	//panelName -> Name of the panel
	//length->Number of characters in a text box
	 private JPanel createTextPanel(String panelName, int length) {
		 	JPanel panel = new JPanel();
			//Default layout is flow
		 	JTextField field = new JTextField((int)(length));
		 	//Player cannot edit boxes, only for output
		 	field.setEditable(false);
		 	fields.add(field);
			panel.add(field);
			panel.setBorder(new TitledBorder (new EtchedBorder(), panelName));
			return panel;
	}
	 
	 //Makes a three field text panel
	 //panelName->Name of the panel
	 //fieldOne, fieldTwo, fieldThree -> Name of text boxes
	 //length -> number of characters in text field
	 private JPanel createThreeTextPanel(String panelName, String fieldOne, String fieldTwo, String fieldThree, int length) {
		 	JPanel panel = new JPanel();
			panel.setLayout(new GridBagLayout());
			GridBagConstraints constraint = new GridBagConstraints();
			
			//Three labels and text fields
			JLabel fieldName1 = new JLabel(fieldOne);
		 	JTextField field1 = new JTextField(length);
		 	JLabel fieldName2 = new JLabel(fieldTwo);
		 	JTextField field2 = new JTextField(length);
		 	JLabel fieldName3 = new JLabel(fieldThree);
		 	JTextField field3 = new JTextField(length);
		 	
		 	//Constraint sets the row and column for each component
		 	//Also controls length of field
		 	
		 	//All text fields set to false for output only
		 	field1.setEditable(false);
		 	constraint.gridx=0;
			constraint.gridy=0;
		 	panel.add(fieldName1,constraint);
		 	
		 	//Fills remainder of panel
		 	constraint.gridwidth=GridBagConstraints.REMAINDER;
		 	constraint.gridx=1;
			panel.add(field1,constraint);
			
			field2.setEditable(false);
			//Takes up 1 grid width
			constraint.gridwidth=1;
			constraint.gridx=0;
			constraint.gridy=1;
			panel.add(fieldName2,constraint);
			
			constraint.gridwidth=GridBagConstraints.REMAINDER;
			constraint.gridx=1;
			panel.add(field2,constraint);
			
			field3.setEditable(false);
			constraint.gridwidth=1;
			constraint.gridx=0;
			constraint.gridy=2;
			panel.add(fieldName3,constraint);
			
			constraint.gridwidth=GridBagConstraints.REMAINDER;
			constraint.gridx=1;
			panel.add(field3,constraint);
			
			fields.add(field1);
			fields.add(field2);
			fields.add(field3);
			
			panel.setBorder(new TitledBorder (new EtchedBorder(), panelName));
			return panel;
	}
	
	public static void updateField(int index, String message){
		fields.get(index).setText(message);
	}


}
