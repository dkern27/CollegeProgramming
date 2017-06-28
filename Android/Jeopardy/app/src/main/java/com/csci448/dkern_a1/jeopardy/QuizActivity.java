package com.csci448.dkern_a1.jeopardy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity
{
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_USER_CHEATED = "UserCheated";
    private static final String KEY_QUESTIONS_ANSWERED = "QuestionsAnswered";
    private static final String KEY_SCORE = "Score";
    private static final int REQUEST_CODE_CHEAT = 0;

    private TextView mScoreTextView;

    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;

    private LinearLayout mTFLayout;
    private Button mTrueButton;
    private Button mFalseButton;

    private RelativeLayout mMCLayout;
    private Button mOptionButton1;
    private Button mOptionButton2;
    private Button mOptionButton3;
    private Button mOptionButton4;

    private RelativeLayout mFITBLayout;
    private EditText mFITBBox;
    private Button mSubmitButton;


    private Question[] mQuestionBank = new Question[] {
            new TrueFalseQuestion(R.string.tf_question, true),
            new MultipleChoiceQuestion(R.string.mc_question, "South America", "Europe", "Asia", "Africa", 4),
            new FillInTheBlankQuestion(R.string.fb_question, "Earth")
    };

    private int mCurrentIndex = 0;
    private boolean[] mQuestionsAnswered = new boolean[mQuestionBank.length];
    private boolean[] mIsCheater = new boolean[mQuestionBank.length];
    int mScore = 0;

    //region Overridden methods
    //region State Changes

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if(savedInstanceState != null)
        {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBooleanArray(KEY_USER_CHEATED);
            mQuestionsAnswered = savedInstanceState.getBooleanArray(KEY_QUESTIONS_ANSWERED);
            mScore = savedInstanceState.getInt(KEY_SCORE);
        }

        //Layouts For different buttons
        mTFLayout = (LinearLayout)findViewById(R.id.tf_buttons);
        mMCLayout = (RelativeLayout)findViewById(R.id.mc_buttons);
        mFITBLayout = (RelativeLayout)findViewById(R.id.fitbLayout);

        mScoreTextView = (TextView)findViewById(R.id.points_text_view);
        mScoreTextView.setText(String.format(getString(R.string.points), mScore));

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        //Question navigation
        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mPrevButton = (ImageButton)findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCurrentIndex = mCurrentIndex - 1;
                if (mCurrentIndex == -1)
                {
                    mCurrentIndex = mQuestionBank.length - 1;
                }
                updateQuestion();
            }
        });

        //Cheat Button
        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = CheatActivity.newIntent(QuizActivity.this, mQuestionBank[mCurrentIndex].getAnswer());
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        //True/False Buttons
        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAnswer("True");
            }
        });
        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAnswer("False");
            }
        });

        //Multiple choice buttons
        mOptionButton1 = (Button)findViewById(R.id.mc_button1);
        mOptionButton1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAnswer(mOptionButton1.getText().toString());
            }
        });
        mOptionButton2 = (Button)findViewById(R.id.mc_button2);
        mOptionButton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAnswer(mOptionButton2.getText().toString());
            }
        });
        mOptionButton3 = (Button)findViewById(R.id.mc_button3);
        mOptionButton3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAnswer(mOptionButton3.getText().toString());
            }
        });
        mOptionButton4 = (Button)findViewById(R.id.mc_button4);
        mOptionButton4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAnswer(mOptionButton4.getText().toString());
            }
        });

        mFITBBox = (EditText)findViewById(R.id.fitbText);
        mSubmitButton = (Button)findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String userAnswer = mFITBBox.getText().toString();
                if(!userAnswer.isEmpty())
                {
                    checkAnswer(mFITBBox.getText().toString());
                }
                else
                {
                    Toast.makeText(QuizActivity.this, R.string.no_text_entered, Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateQuestion();
    }

    /**
     * Saves the index of the current question, array of booleans for if the user cheated on questions,
     * array of booleans for which questions were answered, and score
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBooleanArray(KEY_USER_CHEATED, mIsCheater);
        savedInstanceState.putBooleanArray(KEY_QUESTIONS_ANSWERED, mQuestionsAnswered);
        savedInstanceState.putInt(KEY_SCORE, mScore);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onStop()
    {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    //endregion

    /**
     * Processes return from CheatActivity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT)
        {
            if (data == null)
            {
                return;
            }
            mIsCheater[mCurrentIndex] = CheatActivity.wasAnswerShown(data);
        }
    }

    //endregion


    /**
     * Updates question text and the view
     */
    private void updateQuestion()
    {
        Question newQuestion = mQuestionBank[mCurrentIndex];
        int question = newQuestion.getTextResId();
        mQuestionTextView.setText(question);
        if(newQuestion instanceof TrueFalseQuestion)
        {
            mMCLayout.setVisibility(View.GONE);
            mFITBLayout.setVisibility(View.GONE);
            mTFLayout.setVisibility(View.VISIBLE);
        }
        else if(newQuestion instanceof MultipleChoiceQuestion)
        {
            String[] options = ((MultipleChoiceQuestion) newQuestion).getOptions();
            mOptionButton1.setText(options[0]);
            mOptionButton2.setText(options[1]);
            mOptionButton3.setText(options[2]);
            mOptionButton4.setText(options[3]);
            mTFLayout.setVisibility(View.GONE);
            mFITBLayout.setVisibility(View.GONE);
            mMCLayout.setVisibility(View.VISIBLE);
        }
        else if (newQuestion instanceof FillInTheBlankQuestion)
        {
            mFITBBox.setText("");
            mTFLayout.setVisibility(View.GONE);
            mMCLayout.setVisibility(View.GONE);
            mFITBLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Checks answer to a question
     * @param optionChosen Which option the user chose, i.e. true/false
     */
    private void checkAnswer(String optionChosen)
    {
        String answer = mQuestionBank[mCurrentIndex].getAnswer();
        int messageResId;
        if(mQuestionsAnswered[mCurrentIndex])
        {
            messageResId = R.string.already_answered;
        }
        else if(mIsCheater[mCurrentIndex])
        {
            messageResId = R.string.judgment_toast;
        }
        else
        {
            mQuestionsAnswered[mCurrentIndex] = true;
            if (answer.toLowerCase().equals(optionChosen.toLowerCase()))
            {
                messageResId = R.string.correct_toast;
                mScore += 10;
                mScoreTextView.setText(String.format(getString(R.string.points), mScore));
                mScoreTextView.invalidate();
            } else
            {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }
}
