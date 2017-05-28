package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;


public class BoardCell{
	
	private int row, column;
	private static final int DIM = 25;
	private int xPix, yPix;
	private char initial;
	private DoorDirection doorDirection;
	private boolean isWalkWay = false;
	private boolean drawName;

	public BoardCell(int _row, int _column, char _initial, DoorDirection _doorDirection, boolean drawName) {
		row = _row;
		xPix = _column * DIM;
		yPix = _row * DIM;
		column = _column;
		initial = _initial;
		doorDirection = _doorDirection;
		if(_initial == 'W')
			isWalkWay = true;
		this.drawName = drawName;
	}
	
	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public boolean isDoorway() {
		return !(doorDirection == DoorDirection.NONE);
	}

	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	public boolean isWalkWay() {
		return isWalkWay;
	}
	
	public void draw(Graphics g){
		Graphics2D g2D = (Graphics2D) g;
		//Check if room or walkway
		if(this.initial == 'W'){
			g2D.setColor(Color.CYAN);
			g2D.fillRect(xPix, yPix, DIM, DIM);
			
			//Draw outline to walkway
			g2D.setColor(Color.BLACK);
			g2D.drawRect(xPix, yPix, DIM, DIM);
		}
		else{
			g2D.setColor(Color.MAGENTA);
			g2D.fillRect(xPix, yPix, DIM, DIM);
			
			//For drawing name of room
			if(drawName == true){
				g2D.setColor(Color.BLACK);
				g2D.drawString(Board.getRooms().get(initial).toUpperCase(), xPix, yPix);
			}
		}
		
		
		
		//For drawing doors, will be 1/5 of pixels of a boardcell
		g2D.setColor(Color.YELLOW);
		switch(doorDirection){
		case UP:
			g2D.fillRect(xPix, yPix, DIM, DIM/5);
			break;
		case DOWN:
			g2D.fillRect(xPix, yPix+DIM-DIM/5, DIM, DIM/5);
			break;
		case LEFT:
			g2D.fillRect(xPix, yPix, DIM/5, DIM);
			break;
		case RIGHT:
			g2D.fillRect(xPix+DIM-DIM/5, yPix, DIM/5, DIM);
			break;
		default:
			break;
		}
		
	}

	public void highlight(Graphics g){
		Graphics2D g2D = (Graphics2D) g;
		g2D.setColor(new Color(255,215,0,175));
		g2D.fillRect(xPix+1, yPix+1, DIM-1, DIM-1);
	}
	
	public boolean containsClick(int mouseX, int mouseY){
		Rectangle rect = new Rectangle(xPix, yPix, DIM, DIM);
		if(rect.contains(new Point(mouseX,mouseY))){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "BoardCell [row=" + row + ", column=" + column + ", initial=" + initial + "]";
	}
	
	public char getInitial() {
		return initial;
	}
	
	public static int getCellDimension(){
		return DIM;
	}
	
	public int getxPix() {
		return xPix;
	}

	public int getyPix() {
		return yPix;
	}
	
	public static int getDim() {
		return DIM;
	}
	
}
