package gameEngine;

/*
 * InteractingPiece extends Drawable because ALL pieces must be able to
 * be shown. 
 */

public interface InteractingPiece extends Drawable {
	/*
	 * interact takes the list of pieces and the player's location. It 
	 * returns the result of the interaction via the InteractionResult 
	 * enum. The list of pieces is included as a parameter because a 
	 * piece might interact with a player at a distance (e.g., a 
	 * lightning bolt might affect a player that gets within 2 spaces). 
	 * The entire list of pieces is passed in because the logic could 
	 * consider the presence of other pieces (e.g., laser could hit
	 * a player within 3 spaces unless there's another object in between).
	 *  
	 */
	public InteractionResult interact(Drawable [] pieces, int playerLocation);

}
