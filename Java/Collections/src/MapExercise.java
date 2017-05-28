/*
 * Collections
 * Dylan Kern
 * CSCI 306
 * 
 * TestInput(student,course):(Fred, 100), (James,200), (Heather,300), (Bob,400)
 * 
 * HashMap: No obvious order to the print out
 * Result order: (Heather, 300), (James, 200), (Bob, 400), (Fred, 100)
 * 
 * TreeMap: Ordered alphabetically by key
 * Result order: (Bob, 400), (Fred,100), (Heather,300), (James, 200)
 * 
 * Using just Map, compiler gives error "Cannot instantiate type Map<String,String>" because Map is an interface
 */


import java.util.*;

public class MapExercise {
	Map<String,String> courses;

	public MapExercise() {
		courses=new TreeMap<String,String>();	
	}
	
	//Takes input from user to input into courses
	public void inputStudents(){
		Scanner in = new Scanner(System.in);
		String student="";
		String course="";
		do{
			System.out.print("Enter the student's name or Q to end entry: ");
			student=in.next();
			if(student.equalsIgnoreCase("Q"))
				break;
			System.out.print("Enter " + student + "'s favorite course: ");
			course=in.next();
			courses.put(student,course);
		}while(!(student.equalsIgnoreCase("Q")));
	}
	
	//Prints each pair in courses
	public void printCourses(){
		Set<String>keySet=courses.keySet();
		for(String key: keySet){
			System.out.println(key + " likes " + courses.get(key));
		}	
	}
	
	public static void main(String[] args){
		MapExercise test = new MapExercise();
		test.inputStudents();
		test.printCourses();
	}

}
