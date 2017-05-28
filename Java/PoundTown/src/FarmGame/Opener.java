package FarmGame;


import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Opener extends JPanel implements MouseListener {

	private static final String[] files = {"/OpenerImages/StormyFarm.jpg","/OpenerImages/RadioWarning.jpg","/OpenerImages/Tornado.jpg"};
	private int currentImage = 0;
	private BufferedImage backgroundImage;
	private MainGUI maingui;

	public Opener( MainGUI maingui ) {

		this.maingui = maingui;
		
		drawImage();

		addMouseListener(this);

	}
	
	public void drawImage() {
		try {
			backgroundImage = ImageIO.read( getClass().getResource(files[currentImage]) );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, null);
	}

	public void mouseClicked(MouseEvent e) {
		currentImage++;
		
		if( currentImage == files.length) {
			removeMouseListener(this);
			maingui.startLevel();
			return;
		}
		
		drawImage();
		repaint();
	
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}


}
