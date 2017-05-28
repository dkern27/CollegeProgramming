package clueGame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.lang.reflect.Field;

public class Board extends JPanel{
	
	private int numRows, numColumns;
	public final static int BOARD_SIZE = 50;
	private BoardCell[][] board = new BoardCell[BOARD_SIZE][BOARD_SIZE];
	private static Map<Character,String> rooms = new HashMap<Character,String>();
	private Map<BoardCell, LinkedList<BoardCell>> adjacenciesMap;
	private Set<BoardCell> visitedCells;
	private Set<BoardCell> targets;
	private String boardConfigFile, roomConfigFile, playerConfigFile, cardConfigFile;
	private ArrayList<Player> players;
	private ArrayList<Card> cards;
	private Solution theAnswer;
	public static final int numPlayers = 6;
	private boolean mouseListenerActive = false;
	private Card lastCardSeen = null;
	private boolean disprovedSuggestion = true;
	private Solution lastSuggestion;

	public Board() {
		roomConfigFile = "Configs/Legends/ClueLegend_ST_DH.txt";
		boardConfigFile = "Configs/Layouts/ClueLayout_ST_DH.csv";
		playerConfigFile = "Configs/PlayerConfig.txt";
		cardConfigFile = "Configs/CardConfig.txt";
		
		adjacenciesMap = new HashMap<BoardCell, LinkedList<BoardCell>>();
		visitedCells = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
	}
	
	public Board(String layout, String legend) {
		roomConfigFile = legend;
		boardConfigFile = layout;
		playerConfigFile = "Configs/PlayerConfig.txt";
		cardConfigFile = "Configs/CardConfig.txt";
		
		adjacenciesMap = new HashMap<BoardCell, LinkedList<BoardCell>>();
		visitedCells = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
	}
	
	public void initialize() {
		try {
			loadRoomConfig();
			loadBoardConfig();
			calcAdjacencies();
			loadConfigFiles();
		} catch (BadConfigFormatException e) {
			e.getMessage();
		} catch (FileNotFoundException e){
			e.getMessage();
		} catch (Exception e){
			e.getMessage();
		}
		
		selectAnswer();
		dealCards();
	}
	
	public void loadBoardConfig() throws BadConfigFormatException, FileNotFoundException{
		FileReader reader = new FileReader(boardConfigFile);
		Scanner in = new Scanner(reader);
		
		boolean isFirstLine = true;
		String rowData;
		DoorDirection tempDoorDirection = DoorDirection.NONE;
		int rowIndex = 0;
		
		//while there is data to read, parse in lines
		while(in.hasNext()){
			rowData = in.nextLine();
			
			//figure out the number of columns from the first line, compare all other lines to this
			if (isFirstLine){
				isFirstLine = false;
				for (int k = 0; k < rowData.length(); k++){
					if (rowData.charAt(k) == ',')
						numColumns++;
				}
				numColumns++;
			}
			
			int columnIndex = 0; //reset column variable used for storage before every new row data is parsed

			//loop throw one row of data, storing based on whether or not chars are in the map of rooms
			//and based on comma placement. Get door direction if applicable.
			for (int i = 0; i < rowData.length(); i++ ){
				boolean drawName = false;
				
				//throw error for bad room in config file
				if (rooms.containsKey(rowData.charAt(i)) == false && ((i == 0) || (i - 1 > 0 && rowData.charAt(i - 1) == ','))){
					in.close();
					throw new BadConfigFormatException("Error, board config file contains invalid room.");
				}
				
				//if character being looked at is in room map, and is either the first character or
				//the character before it is a comma then we want to add the data to the board
				else if (rooms.containsKey(rowData.charAt(i)) && ((i == 0) || ((i - 1 > 0) && (rowData.charAt(i - 1) == ',')))){
	
					//if the room character is valid and the next character is not a comma, must figure out door information
					if (i + 1 < rowData.length() && rowData.charAt(i + 1) != ','){
						if(rowData.charAt(i+1) == 'N'){
							drawName = true;
						}
						else
							tempDoorDirection = getTempDoorDirection(rowData.charAt(i + 1));
						
					}
					else{tempDoorDirection = DoorDirection.NONE;}
					
					board[rowIndex][columnIndex] = new BoardCell(rowIndex, columnIndex, rowData.charAt(i), tempDoorDirection, drawName);
					columnIndex++;
				}
			}
			
			if (columnIndex != numColumns){
				in.close();
				throw new BadConfigFormatException("Error, all rows do not have the same number of columns.");
			}
			rowIndex++;//after entirely parsing one row of data, increment index of row
		}
		numRows = rowIndex;
		in.close();
	}

	//helper function for loadBoardConfig()
	private DoorDirection getTempDoorDirection(char _directionChar){
		switch(_directionChar){
			case('R'):{
				return DoorDirection.RIGHT;
			}
			case('L'):{
				return DoorDirection.LEFT;
			}
			case('U'):{
				return DoorDirection.UP;
			}
			case('D'):{
				return DoorDirection.DOWN;
			}
			case('N'):{
				return DoorDirection.NONE;
			}
		}
		return DoorDirection.NONE;
	}
	
	//read legend file, store data into map of room initial, room name
	//bad config file exception purely based on comma placement
	public void loadRoomConfig() throws BadConfigFormatException, FileNotFoundException{
		FileReader reader = new FileReader(roomConfigFile);
		Scanner in = new Scanner(reader);
		
		String rowData, roomName;
		Character roomInitial;

		int firstComma, lastComma;
		
		while(in.hasNext()){
			rowData = in.nextLine();
			firstComma = rowData.indexOf(',');
			lastComma = rowData.lastIndexOf(',');

			if (firstComma != 1 || lastComma == -1 || firstComma == lastComma){
				in.close();
				throw new BadConfigFormatException("Improper room config file");
			}

			else{
				roomInitial = rowData.charAt(0);
				roomName =  rowData.substring(firstComma + 2, lastComma);
				rooms.put(roomInitial, roomName);
			}
		}
		in.close();
	}
	
	
	//fills all adjacenciesMap keys with the same incorrect BoardCell linked list
	public void calcAdjacencies(){
		
		LinkedList<BoardCell> tempLinkList = new LinkedList<BoardCell>(); 
		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numColumns; j++){
				tempLinkList.clear(); 
				
				//checking adjacencies if current cell is a walkway
				if (board[i][j].isWalkWay()) {
					if (i - 1 >= 0 && (board[i-1][j].getDoorDirection() == DoorDirection.DOWN || board[i-1][j].getInitial() == 'W')){
						tempLinkList.add(board[i-1][j]);
					}
					if (i + 1 < numRows && (board[i+1][j].getDoorDirection() == DoorDirection.UP || board[i+1][j].getInitial() == 'W')){
						tempLinkList.add(board[i+1][j]);
					}
					if (j - 1 >= 0 && (board[i][j-1].getDoorDirection() == DoorDirection.RIGHT || board[i][j-1].getInitial() == 'W')){
						tempLinkList.add(board[i][j - 1]);
					}
					if (j + 1 < numColumns && (board[i][j+1].getDoorDirection() == DoorDirection.LEFT || board[i][j+1].getInitial() == 'W')){
						tempLinkList.add(board[i][j + 1]);
					}
				}
				
				//checking adjacencies if current cell is a doorway
				if (board[i][j].isDoorway()) {
					switch (board[i][j].getDoorDirection()) {
						case UP: 
							if (i - 1 >= 0)
								tempLinkList.add(board[i-1][j]);
							break;
						case RIGHT:
							if (j + 1 < numColumns)
								tempLinkList.add(board[i][j+1]);
							break;
						case DOWN:
							if (i + 1 < numRows)
								tempLinkList.add(board[i+1][j]);
							break;
						case LEFT:
							if (j - 1 >= 0)
								tempLinkList.add(board[i][j - 1]);
							break;
						default:
							break;
					}
				}
				adjacenciesMap.put(board[i][j], new LinkedList<BoardCell>(tempLinkList));
			}
		}	
	}
	
	
	public void calcTargets(int _row, int _column, int pathLength){
		//should be able to just use same function from IntBoard in experiment
		visitedCells.clear();
		targets.clear();
		visitedCells.add(board[_row][_column]);
		
		findAllTargets(board[_row][_column], pathLength);
	}
	
	private void findAllTargets(BoardCell startCell, int pathLength) {
		LinkedList<BoardCell> adjacentCells = new LinkedList<BoardCell>();
		
		for (BoardCell cell: adjacenciesMap.get(startCell)){
			if (!(visitedCells.contains(cell))){
				adjacentCells.add(cell);
			}
		}

		for (BoardCell cell: adjacentCells){
			visitedCells.add(cell);
			if (pathLength == 1 || cell.isDoorway()){
				targets.add(cell);
			}
			else {
				findAllTargets(cell, pathLength - 1);
			}
			visitedCells.remove(cell);
		}
	}

	//Loads players and cards
	public void loadConfigFiles() throws FileNotFoundException, BadConfigFormatException{
		
		players=new ArrayList<Player>();
		
		//Load players
		//Format: name,row,col,color
		//Human Player is always first entry
		FileReader reader = new FileReader(playerConfigFile);
		Scanner in = new Scanner(reader);

		String name, startRow, startCol, colorString;
		int row,col;
		boolean gotHuman=false;;
		
		
		while(in.hasNextLine()){
			Scanner lineReader=new Scanner(in.nextLine());
			lineReader.useDelimiter(",");
			//Gets next three fields
			 name = lineReader.next();
			 startRow = lineReader.next();
			 startCol = lineReader.next();
			 colorString = lineReader.next();
			 //Convert string to int
			 Color color = convertColor(colorString);
			 try{ 
				 row=Integer.parseInt(startRow);
				 col=Integer.parseInt(startCol);
			 }
			 //If cannot convert to int -> throw error
			 catch(NumberFormatException e){
				 lineReader.close();
				 throw new BadConfigFormatException("Error: Incorrect player configuration file format. Cannot read X or Y.");
			 }
			//Checks if each variable has something in it
			if (name.length()==0 || startRow.length() == 0 || startCol.length()==0){
				lineReader.close();
				throw new BadConfigFormatException("Error: Missing field in player configuration file.");
			}
			
			//Add players to array
			if(gotHuman==false){
				players.add(new HumanPlayer(name,row,col,color));
				gotHuman=true;
			}
			else{
				players.add(new ComputerPlayer(name, row, col,color));
			}
			lineReader.close();
		}
		in.close();
		
		//Load Cards
		//Format: Name, Type
		cards = new ArrayList<Card>();
		
		FileReader cardReader = new FileReader(cardConfigFile);
		Scanner cardIn = new Scanner(cardReader);
		
		String cardName, cardType;
		CardType type=CardType.ROOM;
		
		while(cardIn.hasNextLine()){
			Scanner line = new Scanner(cardIn.nextLine());
			line.useDelimiter(",");
			cardName = line.next();
			cardType = line.next();
			
			//Various error catching attempts
			if(cardName.length()==0 || cardType.length()==0 || (!cardType.equalsIgnoreCase("Weapon") && !cardType.equalsIgnoreCase("Person") && !cardType.equalsIgnoreCase("Room"))){
				line.close();
				throw new BadConfigFormatException("Error: Incorrect card configuration file format");
			}
			if(cardType.equalsIgnoreCase("Weapon"))
				type = CardType.WEAPON;
			if(cardType.equalsIgnoreCase("Person"))
				type = CardType.PERSON;
			if(cardType.equalsIgnoreCase("Room"))
				type = CardType.ROOM;
			cards.add(new Card(cardName, type));
			line.close();
		}
		cardIn.close();
	}
	
	  // Helper function for converting color
	public Color convertColor(String strColor) {
		Color color; 
		try {     
			// We can use reflection to convert the string to a color
			Field field = Class.forName("java.awt.Color").getField(strColor.trim());     
			color = (Color)field.get(null); } 
		catch (Exception e) {  
			color = null; // Not defined } 
		}
		return color;
	}

	//Chooses solution to game
	public void selectAnswer(){
		String person="", room="", weapon="";
		boolean gotPerson=false, gotRoom=false, gotWeapon=false;
		
		Random rand = new Random();
		//Loops until one of each card has been chosen
		do{
			int n = rand.nextInt(cards.size());
			Card card = cards.get(n);
			if(card.getType()==CardType.PERSON && gotPerson == false){
				person = card.getCardName();
				gotPerson = true;
				//cards.remove(n);
			}
			if(card.getType()==CardType.ROOM && gotRoom == false){
				room = card.getCardName();
				gotRoom = true;
				//cards.remove(n);
			}
			if(card.getType()==CardType.WEAPON && gotWeapon == false){
				weapon = card.getCardName();
				gotWeapon = true;
				//cards.remove(n);
			}
				
		}while(!gotPerson || !gotRoom || !gotWeapon);
		
		theAnswer = new Solution (person, room, weapon);	
	}
	
	//Deals cards to each player
	public void dealCards(){
		Random rand = new Random();
		//Making a deep copy of cards to remove them without affecting original cards
		ArrayList<Card> cardsCopy=new ArrayList<Card>();
		for(Card c: cards){
			cardsCopy.add(c.clone());
		}
		
		//Deal until only solution cards are left
		while(cardsCopy.size()>3){
			for (Player p: players){
				if(cardsCopy.size()==3)
					break;
				int n=0;
				//Do not grab card that is in solution
				do{
					n = rand.nextInt(cardsCopy.size());
				}while(theAnswer.contains(cardsCopy.get(n).getCardName()));
				//while(cardsCopy.get(n).getCardName().equals(theAnswer.person) || cardsCopy.get(n).getCardName().equals(theAnswer.room) || cardsCopy.get(n).getCardName().equals(theAnswer.weapon));
				Card card = null;
				for(Card c: cards){
					if(c.equals(cardsCopy.get(n))){
						card = c;
						break;
					}
				}
				p.giveCard(card);
				cardsCopy.remove(n);
			}
		}
	}
	
	public Card handleSuggestion(Solution suggestion, String accusingPlayer){
		Card proof = null;
		
		for( Player p : players ) {
			if(  !p.getPlayerName().equals( accusingPlayer )) {
				proof = p.disproveSuggestion(suggestion);
			}
			if(proof != null) {
				for( Player ply : players ) {
					ply.addSeenCard(proof);
				}
			break;
			}
		}
		lastCardSeen = proof;
		
		return proof;
	}
	
	public boolean checkAccusation(Solution accusation){
		
		if(accusation.person.equals(theAnswer.person) && accusation.room.equals(theAnswer.room) && accusation.weapon.equals(theAnswer.weapon)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void endGame(){
		JOptionPane.showMessageDialog(this, "Congratulations, you won!");
		System.exit(0);
	}
	
	public void moveAndSuggest(HumanPlayer p){
		class cellListener implements MouseListener{
			public void mouseClicked(MouseEvent e) {
				if(mouseListenerActive == true){
					boolean validLocChosen = false;
					for(BoardCell b: getTargets()){
						if(b.containsClick(e.getX(), e.getY())){
							p.setRowCol(b.getRow(), b.getColumn());
							removeMouseListener(this);
							//Clear targets and repaint for new position and remove highlights
							targets.clear();
							repaint();
							//Can go to next player after move
							ClueGame.setHumanTurnDone(true);
							validLocChosen = true;
							p.makeSuggestion(Board.this);
							break;
						}
					}
					if(!validLocChosen)
						JOptionPane.showMessageDialog(Board.this, "Invalid Target Selected");
				}
				else{
					removeMouseListener(this);
				}
			}

			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		}
		
		addMouseListener(new cellListener());
		mouseListenerActive = true;
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		//Draws border around the board
		g.drawRect(0, 0, numColumns * BoardCell.getCellDimension(), numRows*BoardCell.getCellDimension());
		
		//Draws each cell
		for (int r = 0; r<numRows; r++){
			for(int c = 0; c<numColumns; c++){
				board[r][c].draw(g);
			}
		}
		
		//Draws players
		for(Player player: players){
			player.draw(g);
		}
		
		//Highlights targets
		for(BoardCell b: targets){
			b.highlight(g);
		}
		
	}
	
	public void setMouseListenerActive(boolean b){
		mouseListenerActive = b;
	}
	
	//Updates player location if mentioned in a suggestion
	public void updatePlayerLocation(Player player, String otherPlayerName){
		Player player2 = null;
		for(Player p: players){
			if(p.getPlayerName().equals(otherPlayerName)){
				player2 = p;
				break;
			}
		}
		player2.setRowCol(player.getRow(), player.getColumn());
		repaint();
	}
	
	
	
	////////////////////////////////////////////////////////////
	//Getter functions
	////////////////////////////////////////////////////////////
	
	public static Map<Character,String> getRooms() {
		return rooms;
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	public BoardCell getCellAt(int i, int j) {
		return board[i][j];
	}
	
	public Set<BoardCell> getTargets(){
		return targets;
	}
	
	public LinkedList<BoardCell> getAdjList(int i, int j){
		BoardCell tempCell = getCellAt(i, j);
		return adjacenciesMap.get(tempCell);
	}
	
	public ArrayList<Player> getPlayers(){
		return players;
	}
	
	public ArrayList<Card> getCards(){
		return cards;
	}

	public Solution getTheAnswer() {
		return theAnswer;
	}
	
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	public String getLastCardSeen(){
		return lastCardSeen.getCardName();
	}
	
	public boolean getDisprovedSuggestion() {
		return disprovedSuggestion;
	}

	public void setDisprovedSuggestion(boolean disprovedSuggestion) {
		this.disprovedSuggestion = disprovedSuggestion;
	}
	
	public Solution getLastSuggestion() {
		return lastSuggestion;
	}

	public void setLastSuggestion(Solution lastSuggestion) {
		this.lastSuggestion = lastSuggestion;
	}
}
