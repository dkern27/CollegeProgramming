import java.util.Scanner;

public class Station {
	private static final int MAX_EMPLOYEES=5;
	public static int currentBadge=1;
	private String name;
	private Detective[] detectives=new Detective[MAX_EMPLOYEES];
	private int numEmployees = 0;
	
	
	public Station(String name) {
		super();
		this.name = name;
	}
	
	public void addEmployee(String employee) {
		if(numEmployees < MAX_EMPLOYEES)
		{
			detectives[numEmployees]= new Detective(employee, currentBadge);
			numEmployees++;
			currentBadge++;
		}	
	}
	
	public void hire()
	{
		if(numEmployees<MAX_EMPLOYEES)
		{
			Scanner scan = new Scanner(System.in);
			System.out.print("New hire for " + name +"...Enter detective's name: ");
			String name = scan.nextLine();
			addEmployee(name);
		}
		else
		{
			System.out.println("Can't hire any more detectives for " + name);
		}
	}
	
	
	public void printDetectives()
	{
		System.out.println("List of detectives for " + this.name);
		
		for(int i=0; i < this.numEmployees; i++)
		{
			System.out.print("Detective [Badge=" + this.detectives[i].getBadge() + ", Name=" + this.detectives[i].getName() + "] \n");
		}
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Detective[] getDetectives() {
		return detectives;
	}
	public void setDetectives(Detective[] detectives) {
		this.detectives = detectives;
	}
	
}
