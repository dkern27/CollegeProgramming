
public class SuperHero extends Person {
	String[] questions = {"Are you in need of justice?", "*Stares intently*","What do you think of men's onepiece tights?"};
	int i=0;
	
	public SuperHero(String myName, String occupation) {
		super(myName, occupation);
	}

	@Override
	public void askQuestion() { //Asks first question twice then second question
		if(i<2) 
		{
			System.out.println(questions[0]);
			i++;
		}
		else
		{
			System.out.println(questions[1]);
			i=0;
		}
	}

	@Override
	public void answerQuestion() { //Always says same thing
		System.out.println("I'm " + getMyName());
	}

}
