package FarmGame;

import java.util.ArrayList;
import java.util.Random;

public class Location {
	
	private String name;
	private ArrayList<Animal> checklist = new ArrayList<Animal>();
	private ArrayList<Animal> animals = new ArrayList<Animal>();
	private ArrayList<Animal> allAnimals = new ArrayList<Animal>();
	private Animal correctAnimal;
	private LocationType type;
	
	private static final int NUM_ANIMALS = 6;

	public Location(String name, LocationType locationType, ArrayList<Animal> checklist, ArrayList<Animal> allPossibleAnimals) {
		this.name = name;
		this.type = locationType;
		this.checklist = checklist;
		this.allAnimals = allPossibleAnimals;
		
		//Populate location with animals
		populateAnimals();
		
		
	}
	
	public void populateAnimals() {
		Random rand = new Random();
		
		animals.clear();
		
		correctAnimal = checklist.get(rand.nextInt(checklist.size()));

		while( correctAnimal.getFound() ) {
			correctAnimal = checklist.get(rand.nextInt(checklist.size()));
		}

		animals.add(correctAnimal);
		//Get 5 more animals
		while(animals.size() < NUM_ANIMALS){
			Animal a = allAnimals.get(rand.nextInt(allAnimals.size()));
			//Add if the animal is not already in animals
			if(!animals.contains(a) && !checklist.contains(a)){
				animals.add(a);
			}
		}
	}
	
	public LocationGUI draw( MainGUI maingui){
		populateAnimals();
		
		LocationGUI gui = new LocationGUI( type, animals, maingui );
		maingui.changeColor(correctAnimal);
		return gui;
	}
	
	
////// Getters and Setters //////
	
	public LocationType getLocationType(){
		return type;
	}
	
	public ArrayList<Animal> getAnimals() {
		return animals;
	}
	
	public String getName(){
		return name;
	}

}
