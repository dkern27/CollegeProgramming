package com.csci448.dkern_a2.fourinarow;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class WelcomeActivity extends SingleFragmentActivity
{
    public static final String KEY_WINS = "NumWins";
    public static final String KEY_LOSSES = "NumLosses";
    public static final String KEY_DRAWS = "NumDraws";
    public static final String KEY_PLAYERS = "NumPlayers";
    public static final String KEY_PLAYER1COLOR = "Player1Color";
    public static final String KEY_PLAYER2COLOR = "Player2Color";

    /**
     * Creates fragment for WelcomeFragment
     * @return new WelcomeFragment
     */
    @Override
    protected Fragment createFragment()
    {
        Intent i = getIntent();
        return WelcomeFragment.newInstance(i.getIntExtra(KEY_WINS, 0), i.getIntExtra(KEY_LOSSES, 0), i.getIntExtra(KEY_DRAWS, 0));
    }
}
