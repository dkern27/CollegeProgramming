
public class YoungAdult extends Person {
	String[]questions={"Would you like to see pictures of my baby?", "Are you unemployed? I am.", "How do you pay taxes?", "Have you heard of the new organic lemongrass vegan cookies?", "Are you part of the reddit uprising?"};
	String[]answers={"My debit card number is 4837 9273 6638 0911", "The PIN is 5783", "The security code is 392", "My mother's maiden name is Grebenshchikov", "Someone stole all my money!"};
	int i=0,j=0;
	
	public YoungAdult(String myName, String occupation) {
		super(myName, occupation);
	}

	@Override
	public void askQuestion() {
		System.out.println(questions[i]);
		i++;
		if(i==5)
			i=0;
	}

	@Override
	public void answerQuestion() {
		System.out.println(answers[j]);
		j++;
		if(j==5)
			j=0;
	}

}
