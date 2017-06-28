package com.csci448.dkern_a4.breakout;

/**
 * Created by dkern on 4/25/17.
 */

public class Ball
{
    public int x;
    public int y;
    public int radius;
    public int xVel;
    public int yVel;

    /**
     * Ball constructor
     * @param x position x
     * @param y position y
     * @param radius radius of ball
     * @param xVel x velocity
     * @param yVel y velocity
     */
    public Ball(int x, int y, int radius, int xVel, int yVel)
    {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.xVel = xVel;
        this.yVel = yVel;
    }

    /**
     * Changes x, y of ball by the xVel and yVel
     */
    public void updatePosition()
    {
        x += xVel;
        y+= yVel;
    }

    /**
     * Changes sign of x velocity
     */
    public void changexVelDirection()
    {
        xVel = xVel * -1;
    }

    /**
     * Changes sign of y velocity
     */
    public void changeyVelDirection()
    {
        yVel = yVel * -1;
    }
}
