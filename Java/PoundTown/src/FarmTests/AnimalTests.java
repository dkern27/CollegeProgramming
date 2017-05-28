package FarmTests;

import FarmGame.*;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class AnimalTests {

	Level level;
	ArrayList<Animal> animals;
	
	@Before
	public void setUp(){
		level = new Level("2",  "/AnimalImages/Pig.png", 4, AnimalType.PIG);
		animals = level.getAnimalsList();
	}
	
	@Test
	//Tests binary and decimal equivalence
	public void testDecimalMatchingBinary(){
		int digits=level.getDigits();
		
		for(Animal a : animals){
			int dec = a.getDecimalValue();
			String bin = a.getBinaryValue();
			int binValue=0;
			
			//Probably have a function somewhere else to do this
			//Convert binary to decimal
			for(int i=0; i<bin.length();i++){
				if(bin.charAt(i) == '1'){
					binValue += Math.pow(2, (digits-1-i)); 
				}
			}
			assertEquals(dec, binValue);
		}
	}
	
	
	@Test
	//Tests random values selected for animals
	public void testAnimalValues(){
		int num0=0;
		int num5=0;
		int num10=0;
		int num15=0;
		for(int i=0; i<25;i++){
			//String name, String backgroundFile, int numDigits, AnimalType animalType, String animalDrawFile 
			Level l = new Level("1",  "/AnimalImages/Pig.png", 4, AnimalType.PIG);
			for(Animal a: l.getLocations().get(LocationType.POND).getAnimals()){
				switch(a.getDecimalValue()){
				case 0: 
					num0++;
					break;
				case 5:
					num5++;
					break;
				case 10:
					num10++;
					break;
				case 15:
					num15++;
					break;
				default:
					break;
				}
			}
	
		}
		assertTrue(num0>0);
		assertTrue(num5>0);
		assertTrue(num10>0);
		assertTrue(num15>0);

	}
	
	

}
