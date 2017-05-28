package FarmGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Level {
	
	private String name;
	private String lookForFile;
	private Map<LocationType,Location> locations = new HashMap<LocationType, Location>();
	private CheckList checklist;
	private int numDigits;
	private ArrayList<Animal> animals = new ArrayList<Animal>();
	private AnimalType animalType;
	
	public Level( String name, String lookForFile, int numDigits, AnimalType animalType) {
		this.name = name;
		this.lookForFile = lookForFile;
		this.numDigits = numDigits;
		this.animalType = animalType;
		
		//Initialize animals
		generateAnimals();
		
		
		//Create the checklist
		checklist = new CheckList(animals);
		
		//Initialize locations
		generateLocations();
		
	}
	
	public String getLookForFile(){
		return lookForFile;
	}
	
	public LevelGUI draw(MainGUI maingui) {
		LevelGUI gui = new LevelGUI( lookForFile, locations, maingui);
		return gui;
	}
	

	public void generateAnimals() {
		
		//Calculate the maximum possible value given the number of binary digits
		double maxNum = Math.pow(2, (numDigits))-1; 

		//Make an array of all the possible animals
		for( int i=0; i <= maxNum; i++ ) {
			animals.add( new Animal( animalType, i, numDigits ));
		}	
	}
	
	public void generateLocations() {
		locations.put( LocationType.POND, new Location( "Pond", LocationType.POND, checklist.getChecklist(), animals) );
		locations.put( LocationType.FOREST, new Location( "The Feral Forest", LocationType.FOREST, checklist.getChecklist(), animals) );
		locations.put( LocationType.FIELD, new Location( "Green Pastures", LocationType.FIELD, checklist.getChecklist(), animals) );
		locations.put( LocationType.VOLCANO, new Location( "FIRE!", LocationType.VOLCANO, checklist.getChecklist(), animals) );
		locations.put( LocationType.TOWN, new Location( "Pound Town", LocationType.TOWN, checklist.getChecklist(), animals) );
	}
	
	
	public boolean levelComplete() {
		return checklist.foundAll();
	}

////// Getters and Setters //////
	
	public Map<LocationType,Location> getLocations() {
		return locations;
	}
	
	public ArrayList<Animal> getAnimalsList() {
		return animals;
	}
	
	public CheckList getChecklist() {
		return checklist;
	}
	
	public String getName() {
		return name;
	}
	
	public int getDigits(){
		return numDigits;
	}



}