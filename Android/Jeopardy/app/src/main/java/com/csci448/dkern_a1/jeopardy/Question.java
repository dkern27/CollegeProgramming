package com.csci448.dkern_a1.jeopardy;

/**
 * Created by dkern on 1/13/17.
 */

public abstract class Question
{
    private int mTextResId;

    /**
     * Constructor
     * @param textResId Id for string
     */
    public Question(int textResId)
    {
        mTextResId = textResId;
    }

    /**
     * Returns answer
     * @return String of answer
     */
    public abstract String getAnswer();

    /**
     * Getter for mTextResId
     * @return returns mTextResId
     */
    public int getTextResId()
    {
        return mTextResId;
    }

    /**
     * Setter for mTextResId
     * @param textResId value to set it to
     */
    public void setTextResId(int textResId)
    {
        mTextResId = textResId;
    }
}
