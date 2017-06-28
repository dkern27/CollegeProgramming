package com.csci448.dkern_a1.jeopardy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity
{
    private static final String TAG = "CheatActivity";
    private static final String EXTRA_ANSWER = "com.csci448.dkern.geoquizV2.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN= "com.csci448.dkern.geoquizV2.answer_shown";
    private static final String KEY_USERCHEATED = "UserCheated";

    private TextView mAnswerTextView;
    private Button mShowAnswer;
    private String mAnswer;
    private boolean mUserCheated;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if(savedInstanceState != null)
        {
            mUserCheated = savedInstanceState.getBoolean(KEY_USERCHEATED, false);
            setAnswerShowResult(mUserCheated);
        }

        mAnswer = getIntent().getStringExtra(EXTRA_ANSWER);

        mAnswerTextView = (TextView)findViewById(R.id.answer_text_view);
        mShowAnswer = (Button)findViewById(R.id.show_answer_button);

        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mAnswerTextView.setText(mAnswer);
                mUserCheated = true;
                setAnswerShowResult(mUserCheated);
            }
        });
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putBoolean(KEY_USERCHEATED, mUserCheated);
    }

    /**
     * Creates new intent to launch this activity
     * @param packageContext Context object
     * @param answer Answer to the current question
     * @return
     */
    public static Intent newIntent(Context packageContext, String answer)
    {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER, answer);
        return i;
    }

    /**
     * Gets whether user cheated
     * @param result Intent storing whether user cheated
     * @return whether user cheated
     */
    public static boolean wasAnswerShown(Intent result)
    {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    /**
     * Setter for if user cheated
     * @param isAnswerShown Whether user cheated or not
     */
    private void setAnswerShowResult(boolean isAnswerShown)
    {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

}
