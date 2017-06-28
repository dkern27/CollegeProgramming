package com.csci448.dkern_a4.breakout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by dkern on 4/23/17.
 */

public class GameView extends View
{
    private ArrayList<Rect> mBricks = new ArrayList<>();
    private Paint mPaint;
    private Paint mBackgroundPaint;
    private Rect mPaddle;
    private Ball mBall;
    private int mScreenWidth;
    private int mScreenHeight;
    private final int NUM_ROWS = 2;
    private final int NUM_COLUMNS = 5;
    private final int PADDLE_WIDTH = 250;
    private final int BALL_RADIUS = 25;
    private int mBrickWidth = 180;
    private int mBrickHeight = 100;
    public int mBackgroundColor = Color.LTGRAY;
    public int mBrickColor = Color.BLUE;
    public int mPaddleColor = Color.GREEN;
    public int mBallColor = Color.YELLOW;

    /**
     * Constructor to call programatically
     * @param context
     */
    public GameView(Context context)
    {
        this(context, null);
    }

    /**
     * Constructor
     * Gets display size
     * Makes paint, bricks, paddle, ball
     * @param context
     * @param attrs
     */
    public GameView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        mScreenWidth = p.x;
        mScreenHeight = p.y;
        mBrickWidth = (int)(mScreenWidth*0.8)/5;
        mBrickHeight = mScreenHeight/15;

        mPaint = new Paint();
        mBackgroundPaint = new Paint();

        makeBricks();
        makePaddle();

        mBall = new Ball((mScreenWidth-BALL_RADIUS)/2, (mScreenHeight-BALL_RADIUS)/3, BALL_RADIUS, 2, 8);
    }

    /**
     * Draws screen with bricks, paddle, ball
     * @param canvas canvas to draw onto
     */
    @Override
    public void onDraw(Canvas canvas)
    {
        mBackgroundPaint.setColor(mBackgroundColor);
        canvas.drawPaint(mBackgroundPaint);
        mPaint.setColor(mBrickColor);
        for (Rect b : mBricks)
        {
            canvas.drawRect(b.left, b.top, b.right, b.bottom, mPaint);
        }

        mPaint.setColor(mPaddleColor);
        canvas.drawRect(mPaddle.left, mPaddle.top, mPaddle.right, mPaddle.bottom, mPaint);

        mPaint.setColor(mBallColor);
        canvas.drawCircle(mBall.x, mBall.y, mBall.radius, mPaint);

        checkBallCollision();
    }

    /**
     * Makes bricks according to display size and row/column constants
     */
    private void makeBricks()
    {
        int top = mScreenHeight/10;
        int brickSpacing = 10;
        for (int i = 1; i <= NUM_ROWS; i++)
        {
            int left = (int)(mScreenWidth*0.2/2)-(NUM_COLUMNS-1)/2*brickSpacing;
            for (int j = 1; j <= NUM_COLUMNS; j++)
            {
                Rect r = new Rect(left, top, left+mBrickWidth, top+mBrickHeight);
                left = left + mBrickWidth + brickSpacing;
                mBricks.add(r);
            }
            top = top + mBrickHeight + brickSpacing;
        }
    }

    /**
     * Creates paddle with dimensions from display
     */
    private void makePaddle()
    {
        int left = mScreenWidth/2 - PADDLE_WIDTH/2;
        int right = left + PADDLE_WIDTH;
        int top = (int)(mScreenHeight*0.9);
        int bottom = top + mBrickHeight/2;
        mPaddle = new Rect(left, top, right, bottom);
    }

    /**
     * Moves paddle according to some change from the accelerometer
     * @param tilt ammount of accelerometer chaneg in x
     */
    public void movePaddle(float tilt)
    {
        int sign = Integer.signum((int)tilt);
        int change = (int)(-1*sign*(Math.pow(tilt, 2)));

        if(mPaddle.right + change >= mScreenWidth)
        {
            mPaddle.left = mScreenWidth-PADDLE_WIDTH;
            mPaddle.right = mScreenWidth;
        }
        else if (mPaddle.left + change <= 0)
        {
            mPaddle.left = 0;
            mPaddle.right = PADDLE_WIDTH;
        }
        else
        {
            mPaddle.left += change;
            mPaddle.right += change;
        }
    }

    /**
     * Checks if ball hits bricks, walls, paddle and adjusts ball velocity
     */
    private void checkBallCollision()
    {
        for (Rect r : mBricks)
        {
            if(r.intersects(mBall.x-mBall.radius, mBall.y-mBall.radius, mBall.x+mBall.radius, mBall.y+mBall.radius))
            {
                checkRectangleSideCollision(r);
                mBricks.remove(r);
                break;
            }
        }
        //Side Walls
        if((mBall.x + mBall.radius) >= mScreenWidth || (mBall.x - mBall.radius) <= 0)
        {
            mBall.changexVelDirection();
        }
        //Top wall
        else if(mBall.y - mBall.radius <= 0)
        {
            mBall.changeyVelDirection();
        }
        //Paddle
        else if(((mBall.y + mBall.radius) >= mPaddle.top && mBall.x >= mPaddle.left && mBall.x <= mPaddle.right))
        {
            mBall.changeyVelDirection();
            //Allow for angle changes
            int lDist = Math.abs(mBall.x - mPaddle.left);
            int rDist = Math.abs(mBall.x - mPaddle.right);
            if(lDist > rDist)
            {
                mBall.xVel += 2;
            }
            else if (rDist > lDist)
            {
                mBall.xVel -= 2;
            }
        }
        //Bottom wall
        if ((mBall.y - mBall.radius) >= mPaddle.top)
        {
            mBall.xVel = 0;
            mBall.yVel = 0;
            gameOver();
        }
        else if (mBricks.isEmpty())
        {
            mBall.y = mScreenHeight * 2;
            invalidate();
        }
        else
        {
            invalidate();
        }
        mBall.updatePosition();
    }

    /**
     * Checks which side of the rectangle that the ball hits
     * Issue when ball hits top/bottom and ball sides go past end of rectangle
     * @param r rectangle to check for collision
     */
    private void checkRectangleSideCollision(Rect r)
    {
        int ballTop = mBall.y - mBall.radius;
        int ballBottom = mBall.y + mBall.radius;
        int ballLeft = mBall.x - mBall.radius;
        int ballRight = mBall.x + mBall.radius;
        if(XOR(ballRight >= r.left && ballLeft < r.left, ballLeft <= r.right && ballRight > r.right))
        {
            mBall.changexVelDirection();
        }
        else if(XOR(ballBottom >= r.top && ballTop < r.top, ballTop <= r.bottom && ballBottom > r.bottom))
        {
            mBall.changeyVelDirection();
        }
    }

    /**
     * Exclusive Or logical operator
     * @param a first boolean
     * @param b second boolean
     * @return XOR of the two parameters
     */
    private boolean XOR(boolean a, boolean b)
    {
        return ((a || b) && !(a && b));
    }

    private void gameOver()
    {
        GameActivity ga = (GameActivity)this.getContext();
        ga.gameOver();
    }
}
