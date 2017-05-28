/*
    CSCI 262 Data Structures, Spring 2015, Assignment 2 - mazes

    maze_solver.h

    Class declaration for the maze_solver class.

    C. Painter-Wakefield
*/

#ifndef _MAZE_SOLVER_H
#define _MAZE_SOLVER_H

#include <iostream>

// The provided code uses a Vector<string> to store the maze.
// You can change this if you wish.
#include "vector.h"

// TODO: add additional includes here for the data structures you use
#include "stack.h"
#include "queue.h"
#include "point.h"

using namespace std;

// TODO: make sure you read all of the assignment instructions before you
// begin.  You need to modify/create the following methods of this class:
//    initialize()
//    step() 
//    write_maze()
// You will also need to add instance variables for your stack and queue 
// objects.  You may also add other instance variables or methods as needed 
// to effectively complete the task.

class maze_solver {
public:
	// constructor
	maze_solver(string infile, bool use_stak, bool pause=false);

	// public methods
	void initialize();
	void run();
	void step();
	void write_maze(ostream& out);
	void pause();

	void stepStack();
	void stepQueue();
	
	// instance variables
	string maze_name;
	bool use_stack;
	bool do_pause;

	int rows, columns;
	Vector<string> maze;

	bool no_more_steps;
	bool goal_reached;

	// TODO: your own public methods or variables here?

private:
	// TODO: your own private methods or variables here?
	bool isValid(int, int);
	Stack <Point> locationS;
	Queue <Point> locationQ;
	Point end;
	Point currentPos;
	
};

#endif
