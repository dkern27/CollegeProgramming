package FarmGame;

public enum AnimalType {
	CHICKEN("/AnimalImages/Chicken.png"),
	PIG("/AnimalImages/Pig.png"),
	COW("/AnimalImages/Cow.png"),
	SHEEP("/AnimalImages/Sheep.png");
	
	private final String fileName;
	
	AnimalType(String fileName){
		this.fileName = fileName;
	}
	
	public String getFileName(){
		return fileName;
	}
}
