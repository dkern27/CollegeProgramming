package clueGame;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AccusationDialog extends JDialog{
	
	private boolean submitted = true;
	JComboBox <String[]> peopleMenu;
	JComboBox <String[]> weaponMenu;
	JComboBox <String[]> roomMenu;
	
	public AccusationDialog(Board board) {
		setTitle("Make an Accusation");
		setLayout(new GridBagLayout());
		setSize(325,175);
		setModal(true);
		
		JPanel textPanel = new JPanel(new GridBagLayout());
		JPanel submitButton = new JPanel();
		JPanel cancelButton = new JPanel();

		//Add text labels
		GridBagConstraints cstr = new GridBagConstraints();
		cstr.anchor = GridBagConstraints.WEST;
		cstr.gridx=0;
		cstr.gridy=0;
		textPanel.add(new JLabel("Person"),cstr);
		cstr.gridy=1;
		textPanel.add(new JLabel("Weapon"),cstr);
		cstr.gridy=2;
		textPanel.add(new JLabel("Room"),cstr);
		
		//Some arrays to store stuff
		ArrayList<String> persons = new ArrayList<String>();
		ArrayList<String> rooms = new ArrayList<String>();
		ArrayList<String> weapons = new ArrayList<String>();
		
		//Default choice in drop down menus
		persons.add("-");
		rooms.add("-");
		weapons.add("-");
		
		//A loop to sort all the cards and get the lists of persons,rooms,weapons etc
		for( Card c : board.getCards()) {
			switch(c.getType()) {
				case PERSON : 	
					persons.add(c.getCardName());
					break;
				case ROOM : 	
					rooms.add(c.getCardName());
					break;
				case WEAPON : 	
					weapons.add(c.getCardName());
					break;
			}
		}
		
		//Make Combo boxes
		peopleMenu = new JComboBox(persons.toArray());
		weaponMenu = new JComboBox(weapons.toArray());
		roomMenu = new JComboBox(rooms.toArray());
		
		//Centers text in combo box
		((JLabel)peopleMenu.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		((JLabel)weaponMenu.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		((JLabel)roomMenu.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		
		//Set uniform size for combo boxes
		peopleMenu.setPreferredSize(new Dimension(225, (int) peopleMenu.getPreferredSize().getHeight()));
		weaponMenu.setPreferredSize(new Dimension(225, (int) weaponMenu.getPreferredSize().getHeight()));
		roomMenu.setPreferredSize(new Dimension(225, (int) roomMenu.getPreferredSize().getHeight()));
		
		//Add combo boxes to panel
		cstr.gridx=1;
		cstr.gridy=0;
		textPanel.add(peopleMenu,cstr);
		cstr.gridy=1;
		textPanel.add(weaponMenu,cstr);
		cstr.gridy=2;
		textPanel.add(roomMenu,cstr);
		
		//Make buttons and listener
		JButton submit = new JButton("Submit");
		JButton cancel = new JButton("Cancel");
		
		class buttonListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == submit){
					submitted = true;
					ClueGame.setHumanTurnDone(true);
					dispose();
				}
				else
					dispose();
			}
		}
		
		buttonListener bl = new buttonListener();
		submit.addActionListener(bl);
		cancel.addActionListener(bl);
		
		submitButton.add(submit);
		cancelButton.add(cancel);
			
		//Add to JDialog
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridwidth=2;
		add(textPanel,constraints);
		
		constraints.gridwidth=1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		add(submitButton,constraints);
		
		constraints.gridx = 1;
		add(cancelButton, constraints);
		
	}
	
	
	//Getters
	public boolean getSubmitted(){
		return submitted;
	}
	
	public String getPerson(){
		return peopleMenu.getSelectedItem().toString();
	}
	
	public String getWeapon(){
		return weaponMenu.getSelectedItem().toString();
	}
	
	public String getRoom(){
		return roomMenu.getSelectedItem().toString();
	}

}
