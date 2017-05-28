package FarmGame;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

public class LevelGUI extends JPanel implements MouseListener {
	
	Map <LocationType,Location> locations;
	String fileName;
	BufferedImage backgroundImage;
	
	MainGUI maingui;
	
	public LevelGUI( String fileName, Map <LocationType, Location> l, MainGUI maingui ) {
		
		locations = l;
		this.maingui = maingui;
		
		this.fileName=fileName;
		
		//BufferedImage backgroundImage = null;
		try {
			backgroundImage = ImageIO.read( getClass().getResource("/BackgroundImages/GeneralMap.jpg") );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		startMouseListener();
		
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		//draw background
		g.drawImage(backgroundImage, 0, 0, null);
		
		//make toFind image
		BufferedImage toFind = null;
		try {
			toFind = ImageIO.read( getClass().getResource(fileName) );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//draw toFind image
		g.drawImage(toFind,0,0,null);
	}
	
	public void startMouseListener(){
		addMouseListener(this);
	}
	
	public void mouseClicked(MouseEvent e) {
		for (MapClickBox c : maingui.getClickBoxes() ){
			if(c.contains(e.getX(), e.getY())){
				
				LocationType type = c.getLocationType();
				
				LocationGUI loc = locations.get( type ).draw( maingui );
						
				maingui.goToLocation(loc);
				
				removeMouseListener(this);
			}
		}
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

}
