
abstract public class Person {
	
	private String myName, occupation;

	public String getMyName() {
		return myName;
	}

	public Person(String myName, String occupation) {
		super();
		this.myName = myName;
		this.occupation = occupation;
	}
	
	public void askName()
	{
		System.out.println("What's your name?");
	}
	
	public void giveName()
	{
		System.out.println("My name is " + myName);
	}
	
	public void whatDoYouDo()
	{
		System.out.println("I am a " + occupation);
	}
	
	abstract public void askQuestion();
	abstract public void answerQuestion();
}
