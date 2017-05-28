package FarmGame;

public enum LocationType {

	POND("/BackgroundImages/Lake.jpg"),
	FOREST("/BackgroundImages/Forest.jpg"), 
	FIELD("/BackgroundImages/Pasture.jpg"), 
	VOLCANO("/BackgroundImages/Volcano.jpg"),
	TOWN("/BackgroundImages/PoundTown.jpg");
	
	private final String fileName;
	
	LocationType (String fileName){
		this.fileName = fileName;
	}
	
	public String getfileName(){
		return fileName;
	}
}