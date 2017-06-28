package com.csci448.dkern_a1.jeopardy;

/**
 * Created by dkern on 1/24/17.
 */

public class FillInTheBlankQuestion extends Question
{
    private String mAnswer;

    /**
     * Constructor
     * @param textResId resource id
     * @param answer Answer to question
     */
    public FillInTheBlankQuestion(int textResId, String answer)
    {
        super(textResId);
        mAnswer = answer;
    }

    /**
     * Getter for mAnswer
     * @return mAnswer
     */
    public String getAnswer() { return mAnswer; }
}
