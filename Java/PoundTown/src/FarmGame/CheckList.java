package FarmGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CheckList {

	private ArrayList<Animal> animals = new ArrayList<Animal>();
	private static final int NUM_ANIMALS_TO_FIND = 6;
	
	public CheckList(ArrayList<Animal> allAnimals) {
		Random rand = new Random();
		while(animals.size() < NUM_ANIMALS_TO_FIND){
			Animal a = allAnimals.get(rand.nextInt(allAnimals.size()));
			if(!animals.contains(a))
				animals.add(a);
		}
		Collections.sort(animals);
	}
	
	public boolean foundAll(){
		
		for (Animal animal: animals) {
			if (!animal.getFound()) {
				return false;
			}
		}
		return true;
	}
	
	public CheckListGUI draw() {
		CheckListGUI gui = new CheckListGUI( animals );
		return gui;
	}
	
	public CheckListGUI draw(Animal correct) {
		CheckListGUI gui = new CheckListGUI( animals , correct);
		return gui;
	}
	
	
////// Getters and Setters //////

	public ArrayList<Animal> getChecklist() {
		return animals;
	}
	
	public void setChecklist(ArrayList<Animal> check){
		animals = check;
	}

}