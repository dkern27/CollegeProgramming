public class Detective {
	public static final int NUM_DETECTIVES = 10;
	private String name;
	private int badge;
	
	public Detective(String name, int badge) {
		super();
		this.name = name;
		this.badge = badge;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBadge() {
		return badge;
	}
	public void setBadge(int badge) {
		this.badge = badge;
	}
	
	
}
