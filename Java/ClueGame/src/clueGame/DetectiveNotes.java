package clueGame;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class DetectiveNotes extends JDialog {
	
	public DetectiveNotes( Board board ) {
		setTitle("Detective Notes");
		setSize(500, 600);
		setLayout(new GridLayout(3, 2));
		
		//Make some new panels
		JPanel PeoplePanel = new JPanel();
		JPanel PersonGuessPanel = new JPanel();	
		JPanel RoomsPanel = new JPanel();	
		JPanel RoomGuessPanel = new JPanel();	
		JPanel WeaponsPanel = new JPanel();
		JPanel WeaponGuessPanel = new JPanel();		
		
		//Some arrays to store stuff
		ArrayList<String> persons = new ArrayList<String>();
		ArrayList<String> rooms = new ArrayList<String>();
		ArrayList<String> weapons = new ArrayList<String>();
		
		//Default choice is unknown
		persons.add("-");
		rooms.add("-");
		weapons.add("-");
		
		//A loop to sort all the cards and get the lists of persons,rooms,weapons etc
		for( Card c : board.getCards()) {
			switch(c.getType()) {
				case PERSON : 	JCheckBox person = new JCheckBox(c.getCardName());
								PeoplePanel.add(person);
								persons.add(c.getCardName());
								break;
				case ROOM : 	JCheckBox room = new JCheckBox(c.getCardName());
								RoomsPanel.add(room);
								rooms.add(c.getCardName());
								break;
				case WEAPON : 	JCheckBox weapon = new JCheckBox(c.getCardName());
								WeaponsPanel.add(weapon);
								weapons.add(c.getCardName());
								break;
			}
		}
		
		//Make some drop down menus
		JComboBox<String[]> personsMenu = new JComboBox(persons.toArray());
		PersonGuessPanel.add(personsMenu);
		
		JComboBox<String[]> roomsMenu = new JComboBox(rooms.toArray());
		RoomGuessPanel.add(roomsMenu);
		
		JComboBox<String[]> weaponsMenu = new JComboBox(weapons.toArray());
		WeaponGuessPanel.add(weaponsMenu);
		
		//Add titles and display the various panels
		PeoplePanel.setBorder(BorderFactory.createTitledBorder("People"));
		add(PeoplePanel);
		PersonGuessPanel.setBorder(BorderFactory.createTitledBorder("Person Guess"));
		add(PersonGuessPanel);
		
		RoomsPanel.setBorder(BorderFactory.createTitledBorder("Rooms"));
		add(RoomsPanel);
		RoomGuessPanel.setBorder(BorderFactory.createTitledBorder("Room Guess"));
		add(RoomGuessPanel);
		
		WeaponsPanel.setBorder(BorderFactory.createTitledBorder("Weapons"));
		add(WeaponsPanel);
		WeaponGuessPanel.setBorder(BorderFactory.createTitledBorder("Weapon Guess"));
		add(WeaponGuessPanel);
	}
	
}
