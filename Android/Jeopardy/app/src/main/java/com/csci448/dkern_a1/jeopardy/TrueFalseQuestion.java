package com.csci448.dkern_a1.jeopardy;

/**
 * Created by dkern on 1/24/17.
 */

public class TrueFalseQuestion extends Question
{

    private boolean mAnswerTrue;

    /**
     * Constructor
     * @param textResId For superclass
     * @param answerTrue Whether answer is true or not
     */
    public TrueFalseQuestion(int textResId, boolean answerTrue)
    {
        super(textResId);
        mAnswerTrue = answerTrue;
    }

    /**
     * Getter for mAnswerTrue
     * @return returns whether answer is true or false as a string
     */
    public String getAnswer()
    {
        if(mAnswerTrue)
        {
            return "True";
        }
        return "False";
    }

}
