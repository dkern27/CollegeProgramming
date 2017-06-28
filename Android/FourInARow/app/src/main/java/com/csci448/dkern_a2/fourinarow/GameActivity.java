package com.csci448.dkern_a2.fourinarow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;

/**
 * Created by dkern on 2/19/17.
 */

public class GameActivity extends SingleFragmentActivity
{
    /**
     * Creates new GameFragment with intent extras
     * @return new GameFragment
     */
    @Override
    protected Fragment createFragment()
    {
        Intent i = getIntent();
        return GameFragment.newInstance(i.getIntExtra(WelcomeActivity.KEY_WINS, 0), i.getIntExtra(WelcomeActivity.KEY_LOSSES, 0), i.getIntExtra(WelcomeActivity.KEY_DRAWS, 0), i.getIntExtra(WelcomeActivity.KEY_PLAYERS,1), i.getIntExtra(WelcomeActivity.KEY_PLAYER1COLOR, Color.RED), i.getIntExtra(WelcomeActivity.KEY_PLAYER2COLOR, Color.BLUE));
    }

    /**
     * Creates intent to start GameActivity
     * @param packageContext
     * @param wins number of wins
     * @param losses nuber of losses
     * @param draws number of draws
     * @param players number of players
     * @param player1Color player 1 color pieces
     * @param player2Color player 2 color pieces
     * @return intent to start activity
     */
    public static Intent newIntent(Context packageContext, int wins, int losses, int draws, int players, int player1Color, int player2Color)
    {
        Intent i = new Intent(packageContext, GameActivity.class);
        i.putExtra(WelcomeActivity.KEY_WINS, wins);
        i.putExtra(WelcomeActivity.KEY_LOSSES, losses);
        i.putExtra(WelcomeActivity.KEY_DRAWS, draws);
        i.putExtra(WelcomeActivity.KEY_PLAYERS, players);
        i.putExtra(WelcomeActivity.KEY_PLAYER1COLOR, player1Color);
        i.putExtra(WelcomeActivity.KEY_PLAYER2COLOR, player2Color);
        return i;
    }
}
