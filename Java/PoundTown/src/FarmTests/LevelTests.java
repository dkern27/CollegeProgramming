package FarmTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import FarmGame.Animal;
import FarmGame.AnimalType;
import FarmGame.GameEngine;
import FarmGame.Level;
import FarmGame.Location;
import FarmGame.LocationType;

public class LevelTests {
	
		//private static Level level;
		private static GameEngine gEngine;
		private static Location location;
		
		
		//We will probably need to modify the BeforeClass once we start to write the methods
		@Before
		public void setUp() {
			gEngine = new GameEngine();
			location = new Location("1", LocationType.POND, gEngine.getLevels().get(1).getChecklist().getChecklist(), gEngine.getLevels().get(1).getAnimalsList());	
		}
		
		@Test
		public void levelUpWhenFoundAll() {
			ArrayList<Animal> check = new ArrayList<Animal>();
			for (int i = 0;  i < 6 ; i++){ 
				check.add(gEngine.getLevels().get(1).getAnimalsList().get(i));
			}
			
			gEngine.getLevels().get(1).getChecklist().setChecklist(check); //animals to find will be 0-5

			// Find all of these animals
			for (Animal a : gEngine.getLevels().get(1).getAnimalsList()){ 
				a.found();
			}
			
			gEngine.nextLevel();
			
			System.out.println("level up: " + gEngine.getCurrentLevelNumber());
			assertTrue(gEngine.getLevels().get(1).getChecklist().foundAll());
			assertEquals(gEngine.getCurrentLevelNumber(),2);
		}
		
		@Test
		public void doesntLevelUpPrematurely(){
			ArrayList<Animal> check = new ArrayList<Animal>();
			for (int i = 0;  i < 6 ; i++){ 
				check.add(gEngine.getLevels().get(1).getAnimalsList().get(i));
			}
			
			gEngine.getLevels().get(1).getChecklist().setChecklist(check); //animals to find will be 0-5
			
			
			// Find two of these animals
			for (int i=0 ; i < 2; i++){ 
				gEngine.getLevels().get(1).getAnimalsList().get(i).found();
			}

			gEngine.nextLevel();
			
			System.out.println("no level up: " + gEngine.getCurrentLevelNumber());
			assertFalse(gEngine.getLevels().get(1).getChecklist().foundAll());
			assertEquals(gEngine.getCurrentLevelNumber(),1);

		}
		
		@Test
		public void animalLocation() {
			
			assertEquals(location.getAnimals().size(), 6);
			//Tests that there are 6 animals per location
			int animalCounter = 0;
			for (Location l : gEngine.getLevels().get(1).getLocations().values()) {
				animalCounter += l.getAnimals().size();
			}
			assertEquals(30,animalCounter);
			//This goes through every location in levels and adds the amount of animals in each location
			//(I am assuming 4 locations) and tests to see if there is the correct amount of animals in the level
		}

}