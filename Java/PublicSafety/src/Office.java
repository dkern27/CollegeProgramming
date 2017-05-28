import java.util.Scanner;

public class Office {
	public Station[] stations = new Station[2];
	
	public Office() {
		super();
		stations[0] = new Station("CSM");
		stations[1] = new Station("Golden");
	}

	public void hire(Station station)
	{
		Scanner scan = new Scanner(System.in);
		System.out.print("New hire for " + station.getName()+"...Enter detective's name: ");
		String name = scan.nextLine();
		station.addEmployee(name);
	}
	
	public void printEmployees(Station station)
	{
		System.out.println();
		station.printDetectives();
	}
	
	public static void main(String[] args)
	{
		Office PSO = new Office();
		
		PSO.hire(PSO.stations[1]);
		PSO.hire(PSO.stations[0]);
		PSO.hire(PSO.stations[1]);
		PSO.hire(PSO.stations[1]);
		PSO.hire(PSO.stations[1]);
		PSO.hire(PSO.stations[1]);
		PSO.hire(PSO.stations[1]);
		PSO.hire(PSO.stations[0]);
	
		PSO.printEmployees(PSO.stations[0]);
		PSO.printEmployees(PSO.stations[1]);

	}
}
