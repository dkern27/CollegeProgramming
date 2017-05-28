package gameEngine;

//Kills player if stepped on
public class Spikes implements InteractingPiece{
	
	private static InteractionResult interaction = InteractionResult.DIE;
	private String symbol = "^";
	private int location;

	public Spikes(int location) {
		this.location=location;
	}

	public int getLocation(){
		return location;
	}
	
	@Override
	public void draw() {
		System.out.print(symbol);
		
	}

	@Override
	public InteractionResult interact(Drawable[] pieces, int playerLocation) {

		if (playerLocation==location)
		{
			return interaction;
		}
		return InteractionResult.NONE;
	}

}
