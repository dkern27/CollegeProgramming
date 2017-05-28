package gameEngine;

import java.util.Random;

//Gives player +1 health
public class Fairy implements InteractingPiece, Moveable{
	
	private Random r = new Random(); //For random movement
	private static InteractionResult interaction = InteractionResult.HEAL;
	private String symbol = "F";
	private int location;
	private boolean usedFairy = false; //Keep track of whether fairy has been landed on

	public Fairy(int location) {
		super();
		this.location=location;
	}

	@Override
	public void draw() {
		if(usedFairy==false)
			System.out.print(symbol);
		else{System.out.print(" ");}
		
	}

	@Override
	public void move(Drawable[] pieces, int playerLocation) {
		pieces[location]=null;
		do{
			location=r.nextInt(GameEngine.BOARD_SIZE);
		}while(pieces[location]!=null); //Allows fairy to move onto player but not other pieces
		pieces[location]=this;

	}

	@Override
	public InteractionResult interact(Drawable[] pieces, int playerLocation) {
		if(location==playerLocation&&usedFairy==false){
			usedFairy=true;
			return interaction;
		}
		return InteractionResult.NONE;
	}
	
	public int getLocation(){
		return location;
	}

}
