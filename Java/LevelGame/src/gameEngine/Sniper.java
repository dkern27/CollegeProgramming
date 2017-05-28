package gameEngine;

//Kills player if they are at +-2 spaces of sniper
public class Sniper implements InteractingPiece {
	private static InteractionResult interaction = InteractionResult.DIE;
	private String symbol = "S";
	private int location;

	public Sniper(int location) {
		super();
		this.location=location;
	}

	@Override
	public void draw() {
		System.out.print(symbol);
	}

	@Override
	public InteractionResult interact(Drawable[] pieces, int playerLocation) {
		if(playerLocation==location+2 || playerLocation == location-2)
			return interaction;
		return InteractionResult.NONE;
	}
	
	public int getLocation(){
		return location;
	}

}
