package com.csci448.dkern_a2.fourinarow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_DRAWS;
import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_LOSSES;
import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_PLAYER1COLOR;
import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_PLAYER2COLOR;
import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_PLAYERS;
import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_WINS;

/**
 * Created by dkern on 2/19/17.
 */

public class WelcomeFragment extends Fragment
{

    Button playButton;
    Button optionsButton;
    Button quitButton;

    private int mNumWins = 0;
    private int mNumLoss = 0;
    private int mNumDraw = 0;
    private int mNumPlayers = 1;
    private int mPlayer1Color = Color.RED;
    private int mPlayer2Color = Color.BLUE;

    /**
     * Creates new instance of WelcomeFragment
     * @param wins number of wins
     * @param losses number of losses
     * @param draws number of draws
     * @return new WelcomeFragment
     */
    public static WelcomeFragment newInstance(int wins, int losses, int draws)
    {
        WelcomeFragment f = new WelcomeFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_WINS, wins);
        args.putInt(KEY_LOSSES, losses);
        args.putInt(KEY_DRAWS, draws);
        f.setArguments(args);
        return f;
    }

    /**
     * Creates fragment, loades values from savedInstanceState
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null)
        {
            mNumWins = savedInstanceState.getInt(KEY_WINS);
            mNumLoss = savedInstanceState.getInt(KEY_LOSSES);
            mNumDraw = savedInstanceState.getInt(KEY_DRAWS);
            mNumPlayers = savedInstanceState.getInt(KEY_PLAYERS);
            mPlayer1Color = savedInstanceState.getInt(KEY_PLAYER1COLOR);
            mPlayer2Color = savedInstanceState.getInt(KEY_PLAYER2COLOR);
        }
    }

    /**
     * Creates view for WelcomeFragment, hooks up listeners to create new activities
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        playButton = (Button)view.findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = GameActivity.newIntent(getActivity(), mNumWins, mNumLoss, mNumDraw, mNumPlayers, mPlayer1Color, mPlayer2Color);
                startActivityForResult(intent, 1);
            }
        });

        optionsButton = (Button)view.findViewById(R.id.options_button);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = OptionsActivity.newIntent(getActivity(), mNumWins, mNumLoss, mNumDraw, mNumPlayers, mPlayer1Color, mPlayer2Color);
                startActivityForResult(i, 0);
            }
        });

        quitButton = (Button)view.findViewById(R.id.quit_button);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getActivity().finishAffinity();
            }
        });
        return view;
    }

    /**
     * Saves wins, losses, draws, number of players, player 1 color, and player 2 color
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_WINS, mNumWins);
        savedInstanceState.putInt(KEY_LOSSES,mNumLoss);
        savedInstanceState.putInt(KEY_DRAWS,mNumDraw);
        savedInstanceState.putInt(KEY_PLAYERS, mNumPlayers);
        savedInstanceState.putInt(KEY_PLAYER1COLOR, mPlayer1Color);
        savedInstanceState.putInt(KEY_PLAYER2COLOR, mPlayer2Color);
    }

    /**
     * Retrieves results from activites
     * @param requestCode requestCode from the child
     * @param resultCode whether
     * @param data intent sent back
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }
        if (requestCode == 0)
        {
            if (data == null)
            {
                return;
            }
            mNumPlayers = data.getIntExtra(KEY_PLAYERS, mNumPlayers);
            mNumWins = data.getIntExtra(KEY_WINS, 0);
            mNumLoss = data.getIntExtra(KEY_LOSSES, 0);
            mNumDraw = data.getIntExtra(KEY_DRAWS, 0);
            mPlayer1Color = data.getIntExtra(KEY_PLAYER1COLOR, Color.RED);
            mPlayer2Color = data.getIntExtra(KEY_PLAYER2COLOR, Color.BLUE);
        }
        else if (requestCode == 1)
        {
            if (data == null)
            {
                return;
            }
            mNumWins = data.getIntExtra(KEY_WINS, 0);
            mNumLoss = data.getIntExtra(KEY_LOSSES, 0);
            mNumDraw = data.getIntExtra(KEY_DRAWS, 0);
        }
    }
}
