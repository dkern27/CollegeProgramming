package FarmGame;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class LocationGUI extends JPanel implements MouseListener {

	private ArrayList<Animal> animals;
	private ArrayList<Point> animalLocations; 
	BufferedImage backgroundImage = null;
	BufferedImage backArrow = null;
	MainGUI maingui;
	
	public LocationGUI(LocationType currentLocation, ArrayList<Animal> animals, MainGUI main) {
		this.animals = animals;
		maingui = main;
		
		animalLocations = new ArrayList<Point>();
		populate();
		try {
			backgroundImage = ImageIO.read( getClass().getResource(currentLocation.getfileName() ) );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			backArrow = ImageIO.read( getClass().getResource("/BackgroundImages/BackArrow.png") );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		addMouseListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, null);
		g.drawImage(backArrow, 5, 5, null);
		Collections.shuffle(animals);
		for(int i=0; i<animals.size(); i++){
			animals.get(i).draw(g, (int)animalLocations.get(i).getX(), (int)animalLocations.get(i).getY());
			
		}
	}

	//Gives set locations for animals
	public void populate(){
		//All in a line
		animalLocations.add(new Point(50, 325));
		animalLocations.add(new Point(175, 325));
		animalLocations.add(new Point(300, 325));
		animalLocations.add(new Point(425, 325));
		animalLocations.add(new Point(550, 325));
		animalLocations.add(new Point(675, 325));

	}
	
	public void mouseClicked(MouseEvent e) {
		if(e.getX() < 60 && e.getY() < 60 ){ // This makes the gui go back to the map if you click in the top left corner.
			//Also goes back to the map if you click on an animal, after checking to see if animal is in the checklist.
			maingui.returnToMap();	
			removeMouseListener(this);
		}
		
		for(Animal a : animals){
			if (a.getRectangle().contains(e.getX(),e.getY())) {
				if(maingui.getTheGame().getCurrentLevel().getChecklist().getChecklist().contains(a)){
						JOptionPane.showMessageDialog(this, "Good Job! You found one of your animals!", "", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(a.getAnimalType().getFileName())));
				}
				else
					JOptionPane.showMessageDialog(this, "Oops! That wasn't one of your animals!","", JOptionPane.PLAIN_MESSAGE);
				removeMouseListener(this);
				maingui.returnToMap();
			}
		}
	}

	public void mouseEntered(MouseEvent e) {}


	public void mouseExited(MouseEvent e) {}


	public void mousePressed(MouseEvent e) {}


	public void mouseReleased(MouseEvent e) {}


}
