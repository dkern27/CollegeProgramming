package gameEngine;

//Awards player one point
public class PointPiece implements InteractingPiece{

	private static InteractionResult interaction= InteractionResult.GET_POINT; //Player will get a point if interacting with this piece
	private String symbol = "c"; //Represented by a "c" on board
	private int location; //Where piece will be printed on game board
	private boolean hasGotten = false; //Checks if the piece has been gotten already
	

	public int getLocation() {
		return location;
	}

	public PointPiece(int loc) {
		super();
		location = loc;
	}

	@Override
	public void draw() {
		System.out.print(symbol);
	}

	@Override
	public InteractionResult interact(Drawable[] pieces, int playerLocation) {
		if (playerLocation == this.location && hasGotten == false){
			hasGotten = true;
			pieces[location]=null;
			return interaction;
		}
		
		return InteractionResult.NONE;
		
	}
	
}
