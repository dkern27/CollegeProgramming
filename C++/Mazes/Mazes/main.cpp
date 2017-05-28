/*
    CSCI 262 Data Structures, Spring 2015, Assignment 2 - mazes

    main.cpp

    Provides the user interface for the maze solver.

    C. Painter-Wakefield
*/

#include <iostream>
#include <string>

#include "maze_solver.h"

using namespace std;

// You should not modify any of this code.  Changing it may cause
// difficulty for the grader.  Touch at your own risk!
int main(int argc, char* argv[]) {
	string infile;
	string s_or_q;
	bool do_pause = true;
	bool use_stack = true;

	// Check for command line arguments for maze filename and stack/queue
	// selection.  An optional third argument lets the grader circumvent
	// the pause between UI refreshes for automated testing purposes.
	if (argc >= 3) {
		infile = argv[1];
		s_or_q = argv[2];
		if (argc == 4 && string(argv[3]) == "false") do_pause = false;
	}
	else {
		cout << "Please enter a maze filename: ";
		getline(cin, infile);
		cout << "Please enter (S) to use a stack, or (Q) to use a queue: ";
		getline(cin, s_or_q);
	}

	if (s_or_q == "Q") use_stack = false;

	// create the maze_solver object and run it with the user inputs
	maze_solver solver(infile, use_stack, do_pause);
	solver.initialize();
	solver.run();
	
	system("PAUSE");

	return 0;
}

