package FarmGame;

public class MapClickBox {

	private int height;
	private int width;
	private int x;
	private int y;
	LocationType location;
	
	MapClickBox(int h, int w, int x, int y, LocationType loc){
		height = h;
		width = w;
		this.x = x;
		this.y = y;
		location = loc;
	}
	
	public boolean contains(int x, int y){
		if(x > this.x && x < (this.x+width) && y > this.y && y < (this.y+height))
			return true;
		return false;
	}
	
	public LocationType getLocationType(){
		return location;
	}
}


