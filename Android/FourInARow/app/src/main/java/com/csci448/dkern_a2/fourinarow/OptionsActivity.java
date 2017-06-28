package com.csci448.dkern_a2.fourinarow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;

import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_DRAWS;
import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_LOSSES;
import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_PLAYER1COLOR;
import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_PLAYER2COLOR;
import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_PLAYERS;
import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_WINS;

/**
 * Created by dkern on 2/22/17.
 */

public class OptionsActivity extends SingleFragmentActivity
{
    /**
     * Creates the new fragment using the extras in the intent
     * @return new OptionsFragment
     */
    @Override
    protected Fragment createFragment()
    {
        Intent i = getIntent();
        return OptionsFragment.newInstance(i.getIntExtra(KEY_WINS, 0), i.getIntExtra(KEY_LOSSES, 0), i.getIntExtra(KEY_DRAWS, 0), i.getIntExtra(KEY_PLAYERS, 1), i.getIntExtra(KEY_PLAYER1COLOR, Color.RED), i.getIntExtra(KEY_PLAYER2COLOR, Color.BLUE));
    }

    /**
     * creates new intent for a OptionsFragment
     * @param packageContext activity
     * @param wins number of wins
     * @param losses number of losses
     * @param draws number of draws
     * @param players number of players
     * @param player1Color color of player 1 pieces
     * @param player2Color color of player 2 pieces
     * @return new intent to be started
     */
    public static Intent newIntent(Context packageContext, int wins, int losses, int draws, int players, int player1Color, int player2Color)
    {
        Intent i = new Intent(packageContext, OptionsActivity.class);
        i.putExtra(KEY_WINS, wins);
        i.putExtra(KEY_LOSSES, losses);
        i.putExtra(KEY_DRAWS, draws);
        i.putExtra(KEY_PLAYERS, players);
        i.putExtra(KEY_PLAYER1COLOR, player1Color);
        i.putExtra(KEY_PLAYER2COLOR, player2Color);
        return i;
    }
}
