Dylan Kern
dkern@mymail.mines.edu
Assignment 4: Sensors

Description:
The application is a recreation of a single level of Breakout. A ball must be used to hit the bricks by bouncing it off the wall/paddle. 


Usage instructions:
Move the paddle by tilting the phone. THe more you tilt, the faster the paddle moves.
Hitting the ball on the left side of the paddle will give the ball a little more velcoity in the -x direction. Hitting the ball on the right side will gie it a little more +x velocity.

The color scheme changes depending on how much light there is. It is set fairly low right now, so it may switch back and forth while playing.

When the ball hits the ground, the view stops drawing and a message appears to restart the game. Tap anywhere to restart.


Known Bugs:
Issue with detecting which side of the brick that the ball hits. If the ball hits the y-sides but is partially off the brick, the program thinks the ball hits the x-side and changes the x velocity instead of y. The scenario is drawn below.

                    ----------------------
                    |                    |
                    |        BRICK       |
           -------  |                    |
          |       | |                    |
          |  BALL | ----------------------
          |       |
           -------