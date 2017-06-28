package com.csci448.dkern_a2.fourinarow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_DRAWS;
import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_LOSSES;
import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_PLAYER1COLOR;
import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_PLAYER2COLOR;
import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_PLAYERS;
import static com.csci448.dkern_a2.fourinarow.WelcomeActivity.KEY_WINS;

/**
 * Created by dkern on 2/19/17.
 */

public class GameFragment extends Fragment
{
    private static final String KEY_CELLS = "Cells";

    public static final int NUM_COLUMNS = 7;
    public static final int NUM_ROWS = 7;

    private GridLayout mGameGrid;
    private Button mPlayAgainButton;
    private Button mGoBackButton;
    private TextView mTextView;
    private TextView mWhosTurnText;

    private Cell[][] mCells = new Cell[NUM_ROWS][NUM_COLUMNS];
    private ArrayList<Cell> mAvailableCells = new ArrayList<>();

    private int mPlayer1Color = Color.RED;
    private int mPlayer2Color = Color.BLUE;
    private boolean mIsPlayerOneTurn = true;
    private boolean mIsGameOver = false;

    private int mNumWins = 0;
    private int mNumLoss = 0;
    private int mNumDraw = 0;
    private int mNumPlayers = 1;

    /**
     * New instance of GameFragment with arguments in bundle
     * @param wins number of wins
     * @param losses number of losses
     * @param draws number of draws
     * @param players number of players
     * @param player1Color player 1 piece color
     * @param player2Color player 2 piece color
     * @return new GameFragment
     */
    public static GameFragment newInstance(int wins, int losses, int draws, int players, int player1Color, int player2Color)
    {
        GameFragment gf = new GameFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_WINS, wins);
        args.putInt(KEY_LOSSES, losses);
        args.putInt(KEY_DRAWS, draws);
        args.putInt(KEY_PLAYERS, players);
        args.putInt(KEY_PLAYER1COLOR, player1Color);
        args.putInt(KEY_PLAYER2COLOR, player2Color);
        gf.setArguments(args);
        return gf;
    }

    /**
     * Creates fragment, gets arguments from intent, loads from savedInstanceState
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mNumWins = getArguments().getInt(KEY_WINS);
        mNumLoss = getArguments().getInt(KEY_LOSSES);
        mNumDraw = getArguments().getInt(KEY_DRAWS);
        mNumPlayers = getArguments().getInt(KEY_PLAYERS);
        mPlayer1Color = getArguments().getInt(KEY_PLAYER1COLOR);
        mPlayer2Color = getArguments().getInt(KEY_PLAYER2COLOR);

        if(savedInstanceState != null)
        {
            mNumWins = savedInstanceState.getInt(KEY_WINS);
            mNumLoss = savedInstanceState.getInt(KEY_LOSSES);
            mNumDraw = savedInstanceState.getInt(KEY_DRAWS);
        }
    }

    /**
     * Creates view, sets up all listeners, creates cells
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view created
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        mWhosTurnText = (TextView)view.findViewById(R.id.whos_turn_text);
        setWhosTurnText();

        mGameGrid = (GridLayout)view.findViewById(R.id.game_board);
        mGameGrid.setColumnCount(NUM_COLUMNS);
        mGameGrid.setRowCount(NUM_ROWS);

        mTextView = (TextView)view.findViewById(R.id.help_text);

        //Buttons
        mPlayAgainButton = (Button)view.findViewById(R.id.play_again_button);
        mPlayAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mCells = null;
                getActivity().recreate();
            }
        });
        mGoBackButton = (Button)view.findViewById(R.id.go_back_button);
        mGoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(KEY_WINS, mNumWins);
                returnIntent.putExtra(KEY_LOSSES, mNumLoss);
                returnIntent.putExtra(KEY_DRAWS, mNumDraw);
                getActivity().setResult(RESULT_OK, returnIntent);
                getActivity().finish();
            }
        });

        //Create Cells
        Cell[][] cells = null;
        if(savedInstanceState != null)
        {
            cells = (Cell[][])savedInstanceState.getSerializable(KEY_CELLS);
        }

        for (int i = 0; i < NUM_ROWS; i++)
        {
            for (int j = 0; j < NUM_COLUMNS; j++)
            {
                if(cells == null)
                {
                    mCells[i][j] = new Cell(mGameGrid.getContext(), i, j);
                }
                else
                {
                    mCells[i][j] = new Cell(mGameGrid.getContext(), cells[i][j]);
                }
                mAvailableCells.add(mCells[i][j]);
                mCells[i][j].setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (mIsGameOver) return;
                        if (((Cell) v).hasBeenChosen())
                        {
                            Toast.makeText(getActivity(), R.string.cell_chosen_already, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            processTurn(v);
                        }
                    }
                });
                mGameGrid.addView(mCells[i][j]);
            }
        }

        //Find adjacent cells
        for (int i = 0; i < NUM_ROWS; i++)
        {
            for (int j = 0; j < NUM_COLUMNS; j++)
            {
                mCells[i][j].findAdjacentCells(mCells);
            }
        }
        return view;
    }

    /**
     * Saves variables: wins, losses, draws, cells
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_WINS, mNumWins);
        savedInstanceState.putInt(KEY_LOSSES,mNumLoss);
        savedInstanceState.putInt(KEY_DRAWS,mNumDraw);
        savedInstanceState.putSerializable(KEY_CELLS, mCells);
        mGameGrid.removeAllViews();
    }

    /**
     * Changes color of chosen cell and moves to next players turn
     */
    private void updateCell(Cell c)
    {
        if(mIsPlayerOneTurn)
        {
            c.setBackgroundColor(mPlayer1Color);
            c.setColor(mPlayer1Color);
        }
        else
        {
            c.setBackgroundColor(mPlayer2Color);
            c.setColor(mPlayer2Color);
        }
        c.setHasBeenChosen(true);
        mAvailableCells.remove(c);
    }

    /**
     * Updates chosen cell, checks if game is over, does computer turn if one player mode
     * @param v cell chosen by user
     */
    private void processTurn(View v)
    {
        updateCell((Cell)v);
        if(didPlayerWin((Cell)v))
        {
            processGameOver(false);
        }
        else
        {
            checkDraw();
            mIsPlayerOneTurn = !mIsPlayerOneTurn;
            setWhosTurnText();
            if(mNumPlayers == 1)
            {
                doComputerTurn();
            }
        }
    }

    /**
     * Chooses random spot for computer
     * Checks if game is over
     */
    private void doComputerTurn()
    {
        Random rand = new Random();
        int choice = rand.nextInt(mAvailableCells.size());
        Cell c = mAvailableCells.get(choice);
        updateCell(c);
        if(didPlayerWin(c))
        {
            processGameOver(false);
        }
        else
        {
            checkDraw();
            mIsPlayerOneTurn = !mIsPlayerOneTurn;
            setWhosTurnText();
        }
    }

    /**
     * Game over logistics
     * Shows toast, shows buttons to go back or play again
     * updates score
     * @param isDraw Whether result is draw or not
     */
    private void processGameOver(boolean isDraw)
    {
        mIsGameOver = true;
        String message;
        if(isDraw)
        {
            message = "It's a Draw!";
            mNumDraw++;
        }
        else if(mIsPlayerOneTurn)
        {
            message = "Player one wins!";
            mNumWins++;
        }
        else
        {
            message = "Player 2 wins!";
            mNumLoss++;
        }
        mTextView.setText("W:" + mNumWins + " L:" + mNumLoss + " D:" + mNumDraw);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Checks if a draw has occurred
     */
    private void checkDraw()
    {
        if(mAvailableCells.size() == 0)
        {
            processGameOver(true);
        }
    }

    /**
     * Checks if a player has won. Checks horizontal, vertical, and diagonal lines
     * @param cell cell just placed
     * @return true if a player has won
     */
    private boolean didPlayerWin(Cell cell)
    {
        int consecutive = 0;
        //Up/Down Win
        for (int i = -3; i < 4; i++)
        {
            int nextRow = cell.getRow() + i;
            if(nextRow < 0 || nextRow >= GameFragment.NUM_ROWS)
            {
                consecutive = 0;
                continue;
            }
            if (mCells[nextRow][cell.getColumn()].getColor() == cell.getColor())
            {
                consecutive++;
            }
            else
            {
                consecutive = 0;
            }
            if(consecutive == 4) return true;
        }

        consecutive = 0;
        //Left/Right Win
        for (int i = -3; i < 4; i++)
        {
            int nextCol = cell.getColumn() + i;
            if(nextCol < 0 || nextCol >= GameFragment.NUM_COLUMNS)
            {
                consecutive = 0;
                continue;
            }
            if (mCells[cell.getRow()][nextCol].getColor() == cell.getColor())
            {
                consecutive++;
            }
            else
            {
                consecutive = 0;
            }
            if(consecutive == 4) return true;
        }

        consecutive = 0;
        //Diagonal /
        for (int i = -3; i < 4; i++)
        {
            int nextCol = cell.getColumn() + i;
            int nextRow = cell.getRow() - i;
            if(nextCol < 0 || nextCol >= GameFragment.NUM_COLUMNS || nextRow < 0 || nextRow >= GameFragment.NUM_ROWS)
            {
                consecutive = 0;
                continue;
            }
            if (mCells[nextRow][nextCol].getColor() == cell.getColor())
            {
                consecutive++;
            }
            else
            {
                consecutive = 0;
            }
            if(consecutive == 4) return true;
        }

        consecutive = 0;
        //Diagonal \
        for (int i = -3; i < 4; i++)
        {
            int nextCol = cell.getColumn() + i;
            int nextRow = cell.getRow() + i;
            if(nextCol < 0 || nextCol >= GameFragment.NUM_COLUMNS || nextRow < 0 || nextRow >= GameFragment.NUM_ROWS)
            {
                consecutive = 0;
                continue;
            }
            if (mCells[nextRow][nextCol].getColor() == cell.getColor())
            {
                consecutive++;
            }
            else
            {
                consecutive = 0;
            }
            if(consecutive == 4) return true;
        }
        return false;
    }

    /**
     * Changes text for whos turn it is and color of text
     */
    private void setWhosTurnText()
    {
        if(mIsPlayerOneTurn)
        {
            mWhosTurnText.setText("Player 1's turn");
            mWhosTurnText.setTextColor(mPlayer1Color);
        }
        else
        {
            mWhosTurnText.setText("Player 2's turn");
            mWhosTurnText.setTextColor(mPlayer2Color);
        }
    }
}
