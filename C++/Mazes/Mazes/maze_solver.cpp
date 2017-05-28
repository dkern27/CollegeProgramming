/*
    CSCI 262 Data Structures, Spring 2015, Assignment 2 - mazes

    maze_solver.cpp

    Code for the maze_solver class.  This class will use stack-based depth
    first search or queue-based breadth first search to find a solution (if
    possible) to a simple maze.

    C. Painter-Wakefield
*/

#include "maze_solver.h"

#include <iostream>
#include <fstream>
#include <string>
#include <cstdlib>


using namespace std;

// TODO: read the complete assignment instructions before beginning.  Also look
// at maze_solver.h - you will need to modify it as well.  In this file you
// will need to complete three methods, and add any others as needed for your
// solution. 

// initialize()
// Find the start and goal points.  Push or enqueue the start point.  Set
// the boolean no_more_steps and goal_reached flags to false.
void maze_solver::initialize() {
	// TODO: write this method
	for (int i = 0; i < maze.size(); i++)
	{
		if (maze[i].find('*') != string::npos)
		{
			Point endP(i, maze[i].find('*'));
			end = endP;
		}
		if (maze[i].find('o') != string::npos)
		{
			if (use_stack)
			{
				locationS.push(Point(i, maze[i].find('o')));
			}
			else
				locationQ.enqueue(Point(i, maze[i].find('o')));
		}						
	}
	no_more_steps = false;
	goal_reached = false;
}

// write_maze()
// Output the (partially or totally solved) maze on the provided output stream.
void maze_solver::write_maze(ostream& out) {
	// TODO: write this method
	for (int i = 0; i < maze.size(); i++)
	{
		out << maze[i] << endl;
	}
}

// step()
// Take one step towards solving the maze, setting no_more_steps and 
// goal_reached as appropriate.  This implements the essential maze search
// algorithm; take the next location from your stack or queue, mark the 
// location with '@', add all reachable next points to your stack or queue, 
// etc.
//
// Notes:
//
// - Your initial point should have been pushed/enqueued in the initialize()
//   method.  You should set the no_more_steps variable to true when there
//   are no more reachable points.  The run() method will not call step() once
//   no_more_steps is true.
//
// - You should write an '@' over every location you have previously visited,
//   *except* for the start and goal positions - they should remain 'o' and '*',
//   respectively.
//
// - Since the run() method will call write_maze() in between calls to step(),
//   you probably want to try to make progress on each call to step.  This
//   means having some mechanism to remove from the top/front of your data
//   data structure all points that have already been visited. (Lots of already 
//   visited points can end up in your stack or queue just by virtue of the 
//   fact that some points are reachable from multiple other points - so even 
//   if you don't add visited points to the stack or queue, you will have some
//   in there eventually.)  Remove all visited points before exiting step()
//   will let you avoid going through multiple steps where it seems like 
//   nothing is happening.
//
// - There are many approaches to dealing with invalid/unreachable points; you
//   can choose to add them and then remove them next time you get into step(),
//   you can choose to not add them in the first place, etc.  It is strongly
//   recommended that you make helper methods to avoid code duplication; e.g.,
//   e.g., a function to detect that a point is out of bounds is a real help
//   on the mazes with no walls.
//
// - Make sure you follow the order given in the assignment instructions for
//   adding points to your data structure - up, right, down, left - or your
//   final maze solution won't match the required solution.  Check against the
//   sample solutions provided.
void maze_solver::step() {
	// TODO: write this method
	if (use_stack)
		stepStack();
	else
		stepQueue();
}

// TODO: add helper methods as needed

void maze_solver::stepStack() {
    int x(0), y(0);
    do
    {
        currentPos = locationS.pop();
        x = currentPos.getX();
        y = currentPos.getY();
    } while (maze[x][y] == '@');
	if (currentPos == end)
	{
		goal_reached = true;
		no_more_steps = true;
	}
	else
	{
		if (maze[x][y] == '.')
		{
			maze[x][y] = '@';
		}
        if (isValid(x - 1, y))
        {
            locationS.push(Point(x - 1, y));
        }
		if (isValid(x,y + 1) )
		{
			locationS.push(Point(x, y + 1));
		}
        if (isValid(x + 1, y))
        {
            locationS.push(Point(x + 1, y));
        }
		if (isValid(x,y - 1))
		{
			locationS.push(Point(x, y - 1));
		}
		if (locationS.isEmpty())
			no_more_steps = true;
	}
}

void maze_solver::stepQueue()
{
    int x(0), y(0);
    do
    {
        currentPos = locationQ.dequeue();
        x = currentPos.getX();
        y = currentPos.getY();
    } while (maze[currentPos.getX()][currentPos.getY()] == '@');
	if (currentPos == end)
	{
		goal_reached = true;
		no_more_steps = true;
	}
    else
    {
        if (maze[x][y] != 'o')
        {
            maze[x][y] = '@';
        }

        if (isValid(x - 1, y))
        {
            locationQ.enqueue(Point(x - 1, y));
        }
		if (isValid(x, y + 1))
		{
			locationQ.enqueue(Point(x, y + 1));
		}
		if (isValid(x + 1, y))
		{
			locationQ.enqueue(Point(x + 1, y));
		}
		if (isValid(x, y - 1))
		{
			locationQ.enqueue(Point(x, y - 1));
		}
		if (locationQ.isEmpty())
			no_more_steps = true;
	}
}

bool maze_solver::isValid(int x, int y)
{
    if (x >= rows || x < 0 || y < 0 || y >= columns)
        return false;
	if ((maze[x][y]=='*' || maze[x][y]=='.') && maze[x][y]!='@')
		return true;
	return false;
}


/***************************************************************************
    You should not need to modify code below this point.  Touch at your own
    risk!
****************************************************************************/

// Here's the constructor code.  You should not need to modify this, but you
// can if you want.  Right now it takes care of reading in the maze for you.
// The maze is stored as a Vector<string>, but you can change that if you wish.
maze_solver::maze_solver(string infile, bool use_stak, bool pause) {
	use_stack = use_stak;
	do_pause = pause;

	// parse out maze name for later use in creating output file name
	int pos = infile.find(".");
	if (pos == string::npos) {
		maze_name = infile;
	} else {
		maze_name = infile.substr(0, pos);
	}

	// open input file and read in maze
	ifstream fin(infile);
	if (!fin) {
		cerr << "Error opening input file \"" << infile << "\"; exiting." << endl;
		exit(1);
	}

	fin >> rows >> columns;

	string row;
	getline(fin, row);
	for (int i = 0; i < rows; i++) {
		getline(fin, row);
		maze.add(row);
	}
	
	fin.close();
}

// run()
// Drives the solution forward.
// While more steps are possible (while no_more_steps == false), run repeatedly
// calls step(), then write_maze() on cout, then pause().  Once there are no 
// more steps, it prints a success/failure message to the user (based on the
// goal_reached flag) and writes the final maze to a solution file.
void maze_solver::run() {
	cout << "Solving maze '" << maze_name << "'." << endl;
	cout << "Initial maze: " << endl << endl;
	write_maze(cout);
	cout << endl;
	pause();

	// main loop
	while (!no_more_steps) {
		step();
		cout << endl;
		write_maze(cout);
		cout << endl;
		pause();
	}

	// final output to user
	cout << "Finished: ";
	if (goal_reached) {
		cout << "goal reached!" << endl;
	} else {
		cout << "no solution possible!" << endl;
	}

	// save solution file
	string outfile;
	if (use_stack) outfile = maze_name + "-stack-solution.txt";
	else outfile = maze_name + "-queue-solution.txt";
	
	ofstream fout(outfile);
	if (!fout) {
		cerr << "Could not open file \"" << outfile << "\" for writing." << endl;
		cerr << "Solution file could not be saved!" << endl;
	}
	else {
		write_maze(fout);
		fout.close();
	}
}

void maze_solver::pause() {
	if (!do_pause) return;
	string foo;
	cout << "Hit Enter to continue...";
	getline(cin, foo);
}

