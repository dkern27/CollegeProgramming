package com.csci448.dkern_a2.fourinarow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by dkern on 2/22/17.
 */

public class OptionsFragment extends Fragment
{
    private int mNumWins = 0;
    private int mNumLoss = 0;
    private int mNumDraw = 0;

    private int mNumPlayers = 1;
    private int mPlayer1Color = Color.RED;
    private int mPlayer2Color = Color.BLUE;

    Button onePlayerButton;
    Button twoPlayersButton;
    TextView scoreTextView;
    Button resetScoreButton;
    Button goBackButton;
    RadioButton rb_p1Red;
    RadioButton rb_p1Magenta;
    RadioButton rb_p1Yellow;
    RadioButton rb_p2Blue;
    RadioButton rb_p2Green;
    RadioButton rb_p2Cyan;

    /**
     * Makes a new options fragment with the passed in values
     * @param wins number of wins
     * @param losses number of losses
     * @param draws number of draws
     * @param players number of players
     * @param player1Color color of player 1 peice
     * @param player2Color color of player 2 piece
     * @return new fragment
     */
    public static OptionsFragment newInstance(int wins, int losses, int draws, int players, int player1Color, int player2Color)
    {
        OptionsFragment f = new OptionsFragment();
        Bundle args = new Bundle();
        args.putInt(WelcomeActivity.KEY_WINS, wins);
        args.putInt(WelcomeActivity.KEY_LOSSES, losses);
        args.putInt(WelcomeActivity.KEY_DRAWS, draws);
        args.putInt(WelcomeActivity.KEY_PLAYERS, players);
        args.putInt(WelcomeActivity.KEY_PLAYER1COLOR, player1Color);
        args.putInt(WelcomeActivity.KEY_PLAYER2COLOR, player2Color);
        f.setArguments(args);
        return f;
    }

    /**
     * Creates fragment
     * @param savedInstanceState used to load any values from before
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mNumWins = getArguments().getInt(WelcomeActivity.KEY_WINS);
        mNumLoss = getArguments().getInt(WelcomeActivity.KEY_LOSSES);
        mNumDraw = getArguments().getInt(WelcomeActivity.KEY_DRAWS);
        mNumPlayers = getArguments().getInt(WelcomeActivity.KEY_PLAYERS);
        mPlayer1Color = getArguments().getInt(WelcomeActivity.KEY_PLAYER1COLOR);
        mPlayer2Color = getArguments().getInt(WelcomeActivity.KEY_PLAYER2COLOR);
        if(savedInstanceState != null)
        {
            mNumWins = savedInstanceState.getInt(WelcomeActivity.KEY_WINS);
            mNumLoss = savedInstanceState.getInt(WelcomeActivity.KEY_LOSSES);
            mNumDraw = savedInstanceState.getInt(WelcomeActivity.KEY_DRAWS);
            mNumPlayers = savedInstanceState.getInt(WelcomeActivity.KEY_PLAYERS);
            mPlayer1Color = savedInstanceState.getInt(WelcomeActivity.KEY_PLAYER1COLOR);
            mPlayer2Color = savedInstanceState.getInt(WelcomeActivity.KEY_PLAYER2COLOR);
        }
    }

    /**
     * Creates the view, sets default choices, and sets up listeners.
     * @param inflater inflates view
     * @param container
     * @param savedInstanceState
     * @return the view that was created
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        //Number of player buttons
        onePlayerButton = (Button)view.findViewById(R.id.onePlayersButton);
        onePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mNumPlayers = 1;
                onePlayerButton.setBackgroundColor(Color.GREEN);
                twoPlayersButton.setBackgroundColor(Color.GRAY);
            }
        });
        twoPlayersButton = (Button)view.findViewById(R.id.twoPlayersButton);
        twoPlayersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mNumPlayers = 2;
                onePlayerButton.setBackgroundColor(Color.GRAY);
                twoPlayersButton.setBackgroundColor(Color.GREEN);
            }
        });
        //Set initial button color
        if(mNumPlayers == 1)
        {
            onePlayerButton.setBackgroundColor(Color.GREEN);
            twoPlayersButton.setBackgroundColor(Color.GRAY);
        }
        else
        {
            twoPlayersButton.setBackgroundColor(Color.GREEN);
            onePlayerButton.setBackgroundColor(Color.GRAY);
        }

        //Radio Buttons
        rb_p1Red = (RadioButton) view.findViewById(R.id.radio_red);
        rb_p1Red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onRadioButtonClickedP1(v);
            }
        });

        rb_p1Magenta = (RadioButton) view.findViewById(R.id.radio_magenta);
        rb_p1Magenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onRadioButtonClickedP1(v);
            }
        });

        rb_p1Yellow = (RadioButton) view.findViewById(R.id.radio_yellow);
        rb_p1Yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onRadioButtonClickedP1(v);
            }
        });

        rb_p2Blue = (RadioButton) view.findViewById(R.id.radio_blue);
        rb_p2Blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onRadioButtonClickedP2(v);
            }
        });

        rb_p2Green = (RadioButton) view.findViewById(R.id.radio_green);
        rb_p2Green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onRadioButtonClickedP2(v);
            }
        });

        rb_p2Cyan = (RadioButton) view.findViewById(R.id.radio_cyan);
        rb_p2Cyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onRadioButtonClickedP2(v);
            }
        });

        setDefaultRadioButtons();

        //Score
        scoreTextView = (TextView)view.findViewById(R.id.score_text);
        scoreTextView.setText("W:" + mNumWins + " L:" + mNumLoss + " D:" + mNumDraw);

        resetScoreButton = (Button)view.findViewById(R.id.reset_score_button);
        resetScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mNumWins = 0;
                mNumLoss = 0;
                mNumDraw = 0;
                scoreTextView.setText("W:" + mNumWins + " L:" + mNumLoss + " D:" + mNumDraw);
            }
        });

        //Save options
        goBackButton = (Button)view.findViewById(R.id.options_go_back_button);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(WelcomeActivity.KEY_PLAYERS, mNumPlayers);
                returnIntent.putExtra(WelcomeActivity.KEY_WINS, mNumWins);
                returnIntent.putExtra(WelcomeActivity.KEY_LOSSES, mNumLoss);
                returnIntent.putExtra(WelcomeActivity.KEY_DRAWS, mNumDraw);
                returnIntent.putExtra(WelcomeActivity.KEY_PLAYER1COLOR, mPlayer1Color);
                returnIntent.putExtra(WelcomeActivity.KEY_PLAYER2COLOR, mPlayer2Color);
                getActivity().setResult(RESULT_OK, returnIntent);
                getActivity().finish();
            }
        });
        return view;
    }

    /**
     * Saves wins, losses, draws, number of players, and color of players
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(WelcomeActivity.KEY_WINS, mNumWins);
        savedInstanceState.putInt(WelcomeActivity.KEY_LOSSES,mNumLoss);
        savedInstanceState.putInt(WelcomeActivity.KEY_DRAWS,mNumDraw);
        savedInstanceState.putInt(WelcomeActivity.KEY_PLAYERS, mNumPlayers);
        savedInstanceState.putInt(WelcomeActivity.KEY_PLAYER1COLOR, mPlayer1Color);
        savedInstanceState.putInt(WelcomeActivity.KEY_PLAYER2COLOR, mPlayer2Color);
    }


    /**
     * OnClick action for player 1 radio buttons
     * @param view radio button that was clicked
     */
    public void onRadioButtonClickedP1(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_red:
                if (checked)
                    mPlayer1Color = Color.RED;
                    break;
            case R.id.radio_magenta:
                if (checked)
                    mPlayer1Color = Color.MAGENTA;
                    break;
            case R.id.radio_yellow:
                if(checked)
                    mPlayer1Color = Color.YELLOW;
                break;
        }
    }

    /**
     * OnClick action for player 2 radio buttons
     * @param view radio button that was clicked
     */
    public void onRadioButtonClickedP2(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_blue:
                if (checked)
                    mPlayer2Color = Color.BLUE;
                break;
            case R.id.radio_green:
                if (checked)
                    mPlayer2Color = Color.GREEN;
                break;
            case R.id.radio_cyan:
                if(checked)
                    mPlayer2Color = Color.CYAN;
                break;
        }
    }

    /**
     * Sets the default choice for the radio button
     */
    private void setDefaultRadioButtons()
    {
        switch (mPlayer1Color)
        {
            case Color.RED:
                rb_p1Red.setChecked(true);
                break;
            case Color.MAGENTA:
                rb_p1Magenta.setChecked(true);
                break;
            case Color.YELLOW:
                rb_p1Yellow.setChecked(true);
        }

        switch (mPlayer2Color)
        {
            case Color.BLUE:
                rb_p2Blue.setChecked(true);
                break;
            case Color.GREEN:
                rb_p2Green.setChecked(true);
                break;
            case Color.CYAN:
                rb_p2Cyan.setChecked(true);
        }
    }
}
