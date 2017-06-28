package com.csci448.dkern_a1.jeopardy;

/**
 * Created by dkern on 1/24/17.
 */

public class MultipleChoiceQuestion extends Question
{
    private String[] mOptions = new String[4];
    private String mAnswer;

    /**
     * Constructor
     * @param textResId resource id
     * @param option1 multiple choice option
     * @param option2 multiple choice option
     * @param option3 multiple choice option
     * @param option4 multiple choice option
     * @param correctAnswer Which choice is correct: 1,2,3,4
     */
    public MultipleChoiceQuestion(int textResId, String option1, String option2, String option3, String option4, int correctAnswer)
    {
        super(textResId);
        mOptions[0] = option1;
        mOptions[1] = option2;
        mOptions[2] = option3;
        mOptions[3] = option4;
        mAnswer = mOptions[correctAnswer-1];
    }

    /**
     * Getter for the multiple choice options
     * @return mOptions
     */
    public String[] getOptions()
    {
        return mOptions;
    }

    /**
     * Getter for answer string
     * @return mAnswer
     */
    public String getAnswer() { return mAnswer; }
}
