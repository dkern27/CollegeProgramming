package clueGame;

public class Solution {

	public String person;
	public String room;
	public String weapon;
	
	public String getPerson() {
		return person;
	}

	public String getRoom() {
		return room;
	}

	public String getWeapon() {
		return weapon;
	}

	public Solution(String person, String room, String weapon) {
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}
	
	public boolean contains(String s){
		if(s.equals(person) || s.equals(room) || s.equals(weapon))
			return true;
		return false;
	}

}
