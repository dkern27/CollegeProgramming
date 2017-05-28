package gameEngine;

//Hurts player when touched
public class Monster implements InteractingPiece, Moveable {


	private String symbol = "m";
	private int location;
	private int LorR=1; //For movement left or right
	
	private static InteractionResult interaction = InteractionResult.HIT;
	
	
	public Monster(int location) {
		super();
		this.location = location;
	}
	
	public int getLocation() {
		return location;
	}
	
	@Override
	public void draw() {
		System.out.print(symbol);

	}

	@Override
	public void move(Drawable[] pieces, int playerLocation) {
		pieces[location] = null;
		do{
			if (location == 0)
				LorR = 1;
			if(location == GameEngine.BOARD_SIZE-1)
				LorR = -1;
			location = location + LorR;
		}while(pieces[location]!=null);
		pieces[location] = this;
		
		
	}

	@Override
	public InteractionResult interact(Drawable[] pieces, int playerLocation) {
		if (playerLocation == this.location+LorR){
			return interaction;
		}
		
		return InteractionResult.NONE;
	}

}
