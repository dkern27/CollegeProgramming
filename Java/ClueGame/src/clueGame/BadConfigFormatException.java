package clueGame;

public class BadConfigFormatException extends Exception {
	
	BadConfigFormatException(String _message){
		super(_message);
		//consider writing to a log file
	}
}
