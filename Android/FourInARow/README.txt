Dylan Kern
dkern@mymail.mines.edu

Assignment 2: Global Thermonuclear War

Description
I chose to implement a version of Connect 4 where the piece can be placed anywhere in a 7x7 grid. The objective of the assignment was to utilize fragment and intents to travel through activities. The welcome screen has three buttons: Play, Options, and Quit. Play will take you to a game board where tapping a square will change the color of the inner circle. There is also a back button and new game button. The options screen allows you to pick the number of players (1 or 2), the color of each player, and to reset the score. Click the tabs to view screenshots.

Usage Section
Press play to start the game. Click on an empty square to place your piece. If playing against the computer, they will place a piece and it will be your turn again. If against another person, the next square tapped will place their piece. 

Press options to change various settings. Make sure to press save options when done. 

Instructions on Compiling
None

Implementation Details
Each screen consists of one activity and one fragment. There is also a Cell class that extends ImageView.

Bugs
Rotating after a game is over will allow pieces to be placed again.
Must use back buttons on screen to have results sent back.