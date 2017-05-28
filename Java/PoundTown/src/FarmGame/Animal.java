package FarmGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Animal implements Comparable<Animal>{

	private AnimalType animalType;
	private int digits;
	private int decimalValue;
	private String binaryValue;
	private boolean found = false;
	private String drawFile;
	private Rectangle rect;
	
	public Animal(AnimalType animalType, int decimalValue, int digits) {
		
		this.animalType = animalType;
		this.drawFile = animalType.getFileName();
		this.digits = digits;
		this.decimalValue = decimalValue;
		binaryValue = convertToBinary(decimalValue);
		
	}

	//Double checks animal is on list and sets found to true
	public void found(){
		found = true;
	}
	
	//Draws the animal image
	public void draw(Graphics g, int x, int y){
		
		if( found ) {
			return;
		}
		else {
			BufferedImage animalImage = null;
			
			try {
				animalImage = ImageIO.read( getClass().getResource(drawFile) );
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			g.drawImage(animalImage, x, y, null);
			
			rect = new Rectangle(x,y-30,75,125); //the area that the image takes up which you can click on
			g.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
			g.setColor(Color.WHITE);
			//decimal Value for testing. Spacing is approximately good when using binary
			g.drawString(binaryValue, x-(binaryValue.length()-4)*5, y-10);
		}
		
		
	}
	
	//Helper function to convert decimal to binary
	public String convertToBinary(int decNumber){
		int binaryDigitValue = (int) Math.pow(2, digits-1);
		String binaryNumber = "";
		for(int i=0; i < digits; i++){
			if(decNumber - binaryDigitValue >= 0){
				decNumber -= binaryDigitValue;
				binaryNumber += "1";
			}
			else{
				binaryNumber+="0";
			}
			binaryDigitValue = binaryDigitValue / 2;
		}
		return binaryNumber;
	}
	
	//For sorting
	public int compareTo(Animal otherAnimal) {
		if(this.decimalValue > otherAnimal.getDecimalValue())
			return 1;
		return -1;
	}
	
	//Getters
	public int getDecimalValue(){
		return decimalValue;
	}
	
	public String getBinaryValue(){
		return binaryValue;
	}
	
	public boolean getFound(){
		return found;
	}
	
	public void setFound(boolean f){
		found = f;
	}

	public Rectangle getRectangle() {
		return rect;
	}
	
	public AnimalType getAnimalType(){
		return animalType;
	}
}
