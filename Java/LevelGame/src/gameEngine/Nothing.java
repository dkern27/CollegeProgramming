package gameEngine;

//Does nothing
public class Nothing implements Drawable{
	
	private String symbol = "N";
	private int location;

	public Nothing(int location) {
		super();
		this.location=location;
	}

	@Override
	public void draw() {
		System.out.print(symbol);
	}
	
	public int getLocation() {
		return location;
	}

}