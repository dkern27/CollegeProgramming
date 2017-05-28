package FarmGame;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Map;
import java.awt.Color;


import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class CheckListGUI extends JPanel {

	private ArrayList<Animal> animals = new ArrayList<Animal>();
	Animal correctAnimal;

	public CheckListGUI( ArrayList<Animal> animals ){
		this.animals = animals;
		correctAnimal=null;
		makeLayout();
	}
	
	public CheckListGUI( ArrayList<Animal> animals , Animal correct){
		this.animals = animals;
		correctAnimal = correct;
		makeLayout();
	}

	public void makeLayout(){
		setLayout(new GridLayout(1,0));
		
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(8,1));
		panel.setBorder(new TitledBorder(new EtchedBorder(), ""));
		panel.setBackground(new Color(222,184,135));
		Font font = new Font("Comic Sans MS", Font.PLAIN, 25);
		
		JLabel field = new JLabel("Animals to Find:");
		field.setFont(font);

		field.setPreferredSize( new Dimension(200,100) );
		
		field.setHorizontalAlignment(JLabel.CENTER);
	    field.setVerticalAlignment(JLabel.CENTER);
		
		panel.add(field);

		for (Animal a : animals){
			panel.add( animal( a ) );
		}
		
		add(panel);
	}
	
	private JLabel animal( Animal animal ) {
		
		JLabel number = new JLabel( "# " + Integer.toString(animal.getDecimalValue()) );
		
		Font font = new Font("Comic Sans MS", Font.PLAIN, 38);
		
		if( animal.getFound() ) {
			Map attributes = font.getAttributes();
			attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
			font = new Font(attributes);
		} else if ( GameEngine.easyMode == true && correctAnimal != null && animal.getDecimalValue() == correctAnimal.getDecimalValue()){
			Map attributes = font.getAttributes();
			attributes.put(TextAttribute.FOREGROUND, new Color(255,255,255));
			font = new Font(attributes);
		}
		
		
		number.setFont(font);
	    
	    number.setHorizontalAlignment(JLabel.CENTER);
	    number.setVerticalAlignment(JLabel.CENTER);
		
		return number;
	}
	
	
}
