package levelPieces;

import java.util.ArrayList;

import gameEngine.*;
/*
import gameEngine.Drawable;
import gameEngine.Fairy;
import gameEngine.GameEngine;
import gameEngine.InteractingPiece;
import gameEngine.Monster;
import gameEngine.Moveable;
import gameEngine.PointPiece;
import gameEngine.Treasure;
*/
public class LevelEngine {
	
	private Drawable[] pieces=new Drawable[(GameEngine.BOARD_SIZE)];
	private ArrayList<Moveable> movingPieces= new ArrayList<Moveable>();
	private ArrayList<InteractingPiece> interactingPieces = new ArrayList<InteractingPiece>();
	
	public LevelEngine() {
		super();
	}

	public void createLevel(int levelNum) {
		if (levelNum==1){
			
			//Get Point piece
			PointPiece onePoint = new PointPiece(1);
			interactingPieces.add(onePoint);
			pieces[onePoint.getLocation()]=onePoint;		
			
			
			//Get Point Piece
			PointPiece twoPoint = new PointPiece(7);
			interactingPieces.add(twoPoint);
			pieces[twoPoint.getLocation()]=twoPoint;
			
			//Damage piece
			Monster grouch = new Monster(14);
			interactingPieces.add(grouch);
			movingPieces.add(grouch);
			pieces[grouch.getLocation()]=grouch;
			
			//Health Piece
			Fairy godmother=new Fairy(3);
			interactingPieces.add(godmother);
			movingPieces.add(godmother);
			pieces[godmother.getLocation()]=godmother;
			
			//Level advance piece
			Treasure chest=new Treasure(19);
			interactingPieces.add(chest);
			pieces[chest.getLocation()]=chest;
			
			
			//Death be here
			Spikes spike = new Spikes(2);
			interactingPieces.add(spike);
			pieces[spike.getLocation()]= spike;
			
			//This does nothing
			Nothing good = new Nothing(13);
			pieces[good.getLocation()]=good;
			
			
		}
		if (levelNum==2){
			for( int i=0; i<pieces.length; i++){
				pieces[i]=null;
			}
			
			
			interactingPieces.clear();
			movingPieces.clear();
			
			//Hit at a distance piece
			Sniper american = new Sniper(16);
			interactingPieces.add(american);
			pieces[american.getLocation()]=american;
			
			//Get Point Piece
			PointPiece onePoint = new PointPiece(19);
			interactingPieces.add(onePoint);
			pieces[onePoint.getLocation()]=onePoint;
			
			//Get Point Piece
			PointPiece twoPoint = new PointPiece(1);
			interactingPieces.add(twoPoint);
			pieces[twoPoint.getLocation()]=twoPoint;
			
			//Death be here
			Spikes spike = new Spikes(3);
			interactingPieces.add(spike);
			pieces[spike.getLocation()]= spike;
			
			//Death be here
			Spikes spike2 = new Spikes(5);
			interactingPieces.add(spike2);
			pieces[spike2.getLocation()]= spike2;
			
			//Death be here
			Spikes spike3 = new Spikes(7);
			interactingPieces.add(spike3);
			pieces[spike3.getLocation()]= spike3;
			
			//Damage piece
			Monster grump = new Monster(18);
			interactingPieces.add(grump);
			movingPieces.add(grump);
			pieces[grump.getLocation()]=grump;
			
			//Nothing
			Nothing empty = new Nothing(12);
			pieces[empty.getLocation()]=empty;
		}
	}

	public Drawable[] getPieces() {
		return pieces;
	}

	public ArrayList<Moveable> getMovingPieces() {
		return movingPieces;
	}

	public ArrayList<InteractingPiece> getInteractingPieces() {
		return interactingPieces;
	}

}
