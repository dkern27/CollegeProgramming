package com.csci448.dkern_a2.fourinarow;

import android.content.Context;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by dkern on 2/20/17.
 */

public class Cell extends ImageView
{
    private int mResourceId = R.drawable.grid_item;
    private int mColumn;
    private int mRow;
    private boolean mHasBeenChosen = false;
    private int mColor;
    private ArrayList<Cell> mAdjacentCells;

    /**
     * Constructor, make all new Cell
     * @param c
     * @param row row of cell
     * @param column column of cell
     */
    public Cell(Context c, int row, int column)
    {
        super(c);
        this.mColumn = column;
        this.mRow = row;
        mAdjacentCells = new ArrayList<>();
        setImageResource(mResourceId);
        setPadding(0, 0, 0, 0);
        setAdjustViewBounds(true);
    }

    /**
     * Make cell based off old cell
     * @param c
     * @param cell old cell
     */
    public Cell(Context c, Cell cell)
    {
        super(c);
        this.mColumn = cell.getColumn();
        this.mRow = cell.getRow();
        mAdjacentCells = new ArrayList<>();
        this.setBackgroundColor(cell.getColor());
        this.setColor(cell.getColor());
        this.mHasBeenChosen = cell.hasBeenChosen();
        setImageResource(mResourceId);
        setPadding(0, 0, 0, 0);
        setAdjustViewBounds(true);
    }

    /**
     * Finds cells next to this cell
     * @param cells
     */
    public void findAdjacentCells(Cell[][] cells)
    {
        if(mColumn != 0)
        {
            mAdjacentCells.add(cells[mRow][mColumn-1]);
        }
        if(mRow != 0)
        {
            mAdjacentCells.add(cells[mRow-1][mColumn]);
        }
        if(mColumn != GameFragment.NUM_COLUMNS - 1)
        {
            mAdjacentCells.add(cells[mRow][mColumn+1]);
        }
        if(mRow != GameFragment.NUM_ROWS - 1)
        {
            mAdjacentCells.add(cells[mRow+1][mColumn]);
        }
        if(mRow != 0 && mColumn != 0)
        {
            mAdjacentCells.add(cells[mRow-1][mColumn-1]);
        }
        if(mRow != 0 && mColumn != GameFragment.NUM_COLUMNS-1)
        {
            mAdjacentCells.add(cells[mRow-1][mColumn+1]);
        }
        if(mRow != GameFragment.NUM_ROWS-1 && mColumn != 0)
        {
            mAdjacentCells.add(cells[mRow+1][mColumn-1]);
        }
        if(mRow != GameFragment.NUM_ROWS-1 && mColumn != GameFragment.NUM_COLUMNS-1)
        {
            mAdjacentCells.add(cells[mRow+1][mColumn+1]);
        }
    }

    //region getters/setters
    public int getColumn()
    {
        return mColumn;
    }

    public void setColumn(int column)
    {
        this.mColumn = column;
    }

    public int getRow()
    {
        return mRow;
    }

    public void setRow(int row)
    {
        this.mRow = row;
    }

    public int getResourceId()
    {
        return mResourceId;
    }

    public void setResourceId(int resourceId)
    {
        this.mResourceId = resourceId;
    }

    public boolean hasBeenChosen()
    {
        return mHasBeenChosen;
    }

    public void setHasBeenChosen(boolean chosen)
    {
        mHasBeenChosen = chosen;
    }

    public int getColor()
    {
        return mColor;
    }

    public void setColor(int color)
    {
        mColor = color;
    }

    public ArrayList<Cell> getAdjacentCells()
    {
        return mAdjacentCells;
    }

    //endregion
}
