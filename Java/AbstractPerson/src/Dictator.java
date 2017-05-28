import java.util.Random;

public class Dictator extends Person{
	String [] questions = {"Do you want to join my Empire?", "How do you feel about oppression?", "Am I not the greatest leader to ever live?","Have your ideals been destroyed yet?"};
	String [] answers = {"I just invaded Poland!", "Yesterday I made 18 hole-in-one's!", "You are expanding too close to my borders...","It is enough that the people know there was an election. The people who cast the votes decide nothing. The people who count the votes decide everything.","Ideas are far more powerful than guns. We don't let our people have guns. Why should we let them have ideas?"};
	Random rand = new Random();

	public Dictator(String myName, String occupation) {
		super(myName, occupation);
	}

	@Override
	public void askQuestion() { //Random question asked
		System.out.println(questions[rand.nextInt(4)]);
	}

	@Override
	public void answerQuestion() { //Random answer given
		System.out.println(answers[rand.nextInt(5)]);
	}

}
