package gameEngine;

//Allows player to advance to next level
public class Treasure implements InteractingPiece {

	private int location;
	private String symbol = "T";
	private static InteractionResult interaction = InteractionResult.ADVANCE;
	
	public Treasure(int location) {
		this.location = location;
	}

	@Override
	public void draw() {
		System.out.print(symbol);
	}

	@Override
	public InteractionResult interact(Drawable[] pieces, int playerLocation) {
		if (location == playerLocation){
			return interaction;
		}
		return InteractionResult.NONE;
	}
	
	public int getLocation(){
		return location;
	}

}
