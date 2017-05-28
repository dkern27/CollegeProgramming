/*
Dylan Kern
CSCI 262: Assignment 6: Segregation
Reorders a generated grid of red,blue, and white until all agents are satisfied according to a similarity variable
*/

#include <iostream>
#include <string>
#include <cstdlib>
#include <ctime>
#include <sstream>
#include <vector>
#include <algorithm>
#include <random>
#include <utility> // For pair

#include <SFML/Window.hpp>
#include <SFML/Graphics.hpp>

using namespace std;
using namespace sf;

bool numeric(string);
vector<vector<RectangleShape>> generateGrid(double, int, int, int);
void shuffleVec(vector<RectangleShape>&);
vector <pair<int,int>> findUnhappy(vector<vector <RectangleShape>>&, int, int);
void makeHappy(vector<pair<int, int>>&, vector<vector<RectangleShape>>&, int);

int main() {
	clock_t checkpoint = 0;
    srand(time(NULL));

    //Defaults
	int similarity = 30;
	int redPercent = 50;
	int emptyPercent = 10;
	int grid = 30;

    //Grid parameters
	string userInput;
	cout << "Enter similarity percentage (Default=30): ";
	getline(cin, userInput);
	if (numeric(userInput))
		similarity = stoi(userInput);
	cout << "Enter red percentage (default=50): ";
	getline(cin, userInput);
	if (numeric(userInput))
		redPercent = stoi(userInput);
	cout << "Enter empty percentage (default=10): ";
	getline(cin, userInput);
	if (numeric(userInput))
		emptyPercent = stoi(userInput);
	cout << "Enter grid size (default=30): ";
	getline(cin, userInput);
	if (numeric(userInput))
		grid = stoi(userInput); //Currently only allows 0 to 100 because of numeric function
	userInput.clear();



	RenderWindow window(VideoMode(640,500), "Segregation Simulation");

	// drawable region dimensions
	Vector2u sz = window.getSize();
	int width = sz.x;
	int height = sz.y;

	// load this only once
	Font font;
	font.loadFromFile("data/DroidSans.ttf");

    double side = height / double(grid);
    vector <vector<RectangleShape>> squares = generateGrid(side, redPercent, emptyPercent, grid);
    int iterations = 0;
    double satisfied = 0.0;
	// run the program as long as the window is open
	while (window.isOpen()) {
		// check all the window's events that were triggered 
		// since the last iteration of the loop
		sf::Event event;
		while (window.pollEvent(event)) {
			// "close requested" event: we close the window
			if (event.type == sf::Event::Closed)
				window.close();

		}
        while (satisfied!=100.0)
        {

            // give an iteration a little time (250 ms)
            if (checkpoint == 0) {
                checkpoint = clock();
            }
            else if (clock() - checkpoint < CLOCKS_PER_SEC / 4) {
                continue;
            }
            else {
                checkpoint = clock();
            }

            // start with a fresh slate each iteration
            window.clear(Color(255, 255, 255));

            //Find all unsatisfied agents
            vector<pair<int, int>> unhappy = findUnhappy(squares, grid, similarity);
            satisfied = (double)(1 - unhappy.size() / (grid*grid - (double)emptyPercent / 100 * grid*grid)) * 100;

            for (int i = 0; i < grid; ++i)
            {
                for (int j = 0; j < grid; ++j)
                    window.draw(squares[i][j]);
            }

            // message text
            string iter = "Iterations: " + to_string(iterations) + "\nSatisfied: " + to_string(satisfied) + "%";
            Text text(iter, font, 16);
            text.setPosition(side * grid + 10, height / 2);
            text.setColor(Color(0, 0, 0));
            window.draw(text);

            // show on screen
            window.display();

            iterations++;
            //Swap unsatisfied squares
            makeHappy(unhappy, squares, grid);
        }
	}

	return 0;
}

/*
Checks if user input is valid
*/
bool numeric(string s)
{
	if (s.empty())
		return false;
	int number;
	if (stringstream(s) >> number)
	{
		if (number < 0 || number > 100)
			return false;
		return true;
	}
    return false;
}

/*
Creates the colored squares
*/
vector<vector<RectangleShape>> generateGrid(double side, int redPercent, int emptyPercent, int grid)
{
    vector<RectangleShape>orderedSquares;
    vector <vector<RectangleShape>> squares(grid);
    int count = 0;
    //Creates agents
    for (int i = 0; i < grid; i++) {
        squares[i].resize(grid);
        for (int j = 0; j < grid; j++) {
            Color color;
            if (count < (grid*grid)*redPercent / 100)
            {
                color = Color::Red;
                count++;
            }
            else if (count < (grid*grid)*(redPercent + emptyPercent) / 100)
            {
                color = Color::White;
                count++;
            }
            else
            {
                color = Color::Blue;
            }
            RectangleShape rect(Vector2f(side, side));

            rect.setFillColor(color);
            rect.setOutlineColor(Color(200, 200, 200));
            rect.setOutlineThickness(1);

            orderedSquares.push_back(rect);
        }
    }
    shuffleVec(orderedSquares);

    //Adds position and puts into 2D vector
    for (int i = 0; i < orderedSquares.size(); i++)
    {
        int row = i / grid;
        int col = i % grid;
        squares[row][col] = orderedSquares[i];
        double x = row * side;
        double y = col * side;
        squares[row][col].setPosition(x, y);
    }
    return squares;
}

/*
Shuffles the grid
*/
void shuffleVec(vector <RectangleShape> &v) {
    int n = v.size();
    for (int i = 0; i < n - 1; i++) 
    {
        int j = i + rand() % (n - i);
        swap(v[i], v[j]);
    }
}

/*
Finds all unsatisfied squares according to similarity parameter
*/
vector <pair <int, int>> findUnhappy(vector<vector <RectangleShape>>& squares, int grid, int similarity)
{
	vector <pair <int, int>> unhappySquares;
	Color w = Color::White;
	for (int j = 0; j < grid; j++) //Left side
	{
		int neighbors=0;
		int similar=0;
		Color c = squares[0][j].getFillColor();
		if (c != w)
		{

            if (j != grid - 1)
            {
                if (squares[0][j + 1].getFillColor() != w)
                {
                    neighbors++;
                    if (squares[0][j + 1].getFillColor() == c) //down
                        similar++;
                }
                if (squares[1][j + 1].getFillColor() != w)
                {
                    neighbors++;
                    if (squares[1][j + 1].getFillColor() == c) //down right
                        similar++;
                }
			}
            if (j != 0)
            {
                if (squares[0][j - 1].getFillColor() != w)
                {
                    neighbors++;
                    if (squares[0][j - 1].getFillColor() == c) //up
                        similar++;
                }
                if (squares[1][j - 1].getFillColor() != w)
                {
                    neighbors++;
                    if (squares[1][j - 1].getFillColor() == c) // up right
                        similar++;
                }
			}
            if (squares[1][j].getFillColor() != w)
            {
                neighbors++;
                if (squares[1][j].getFillColor() == c) //right
                    similar++;
            }
            if (neighbors != 0)
            {
                if ((double)similar / neighbors < (double)similarity / 100)
                    unhappySquares.push_back(make_pair(0, j));
            }
		}
	}

	for (int j = 0; j < grid; j++) //Right side
	{
		int neighbors = 0;
		int similar = 0;
		Color c = squares[grid-1][j].getFillColor();
		if (c != Color::White)
		{

			if (j != grid-1)
			{
                if (squares[grid - 1][j + 1].getFillColor() != w)
                {
                    neighbors++;
                    if (squares[grid - 1][j + 1].getFillColor() == c) //down
                        similar++;
                }
                if (squares[grid - 2][j + 1].getFillColor() != w)
                {
                    neighbors++;
                    if (squares[grid - 2][j + 1].getFillColor() == c) //down left
                        similar++;
                }
			}
			if (j != 0)
			{
                if (squares[grid - 1][j - 1].getFillColor() != w)
                {
                    neighbors++;
                    if (squares[grid - 1][j - 1].getFillColor() == c) //up
                        similar++;
                }
                if (squares[grid - 2][j - 1].getFillColor() != w)
                {
                    neighbors++;
                    if (squares[grid - 2][j - 1].getFillColor() == c) // up left
                        similar++;
                }
			}
            if (squares[grid - 2][j].getFillColor() != w)
            {
                neighbors++;
                if (squares[grid - 2][j].getFillColor() == c) //left
                    similar++;
            }
            if (neighbors != 0)
            {
                if ((double)similar / neighbors < (double)similarity / 100)
                    unhappySquares.push_back(make_pair(grid-1, j));
            }
		}
	}
    for (int i = 1; i < grid-1; i++) //Top row
    {
        int neighbors = 0;
        int similar = 0;
        Color c = squares[i][0].getFillColor();
        if (c != Color::White)
        {
            if (squares[i - 1][0].getFillColor() != w)
            {

                neighbors++;
                if (squares[i - 1][0].getFillColor() == c) //left
                    similar++;
            }
            if (squares[i - 1][1].getFillColor() != w)
            {
                neighbors++;
                if (squares[i - 1][1].getFillColor() == c) // down left
                    similar++;
            }
            if (squares[i + 1][0].getFillColor() != w)
            {
                neighbors++;
                if (squares[i + 1][0].getFillColor() == c) //right
                    similar++;
            }
            if (squares[i + 1][1].getFillColor() != w)
            {
                neighbors++;
                if (squares[i + 1][1].getFillColor() == c) //down right
                    similar++;
            }
            if (squares[i][1].getFillColor() != w)
            {
                neighbors++;
                if (squares[i][1].getFillColor() == c) //down
                    similar++;
            }
            if (neighbors != 0)
            {
                if ((double)similar / neighbors < (double)similarity / 100)
                    unhappySquares.push_back(make_pair(i, 0));
            }
        }
    }
    for (int i = 1; i < grid - 1; i++) //Bottom row
    {
        int neighbors = 0;
        int similar = 0;
        Color c = squares[i][grid - 1].getFillColor();
        if (c != Color::White)
        {
            if (squares[i - 1][grid - 1].getFillColor() != w)
            {

                neighbors++;
                if (squares[i - 1][grid - 1].getFillColor() == c) //left
                    similar++;
            }
            if (squares[i - 1][grid - 1].getFillColor() != w)
            {
                neighbors++;
                if (squares[i - 1][grid - 2].getFillColor() == c) // up left
                    similar++;
            }
            if (squares[i + 1][grid - 1].getFillColor() != w)
            {
                neighbors++;
                if (squares[i + 1][grid - 1].getFillColor() == c) //right
                    similar++;
            }
            if (squares[i + 1][grid - 2].getFillColor() != w)
            {
                neighbors++;
                if (squares[i + 1][grid - 2].getFillColor() == c) //up right
                    similar++;
            }
            if (squares[i][grid - 2].getFillColor() != w)
            {
                neighbors++;
                if (squares[i][grid - 2].getFillColor() == c) //up
                    similar++;
            }
            if (neighbors != 0)
            {
                if ((double)similar / neighbors < (double)similarity / 100)
                    unhappySquares.push_back(make_pair(i, grid-1));
            }
        }
    }
    for (int i = 1; i < grid - 1; i++) //Interior
    {
        for (int j = 1; j < grid - 1; j++)
        {
            int neighbors = 0;
            int similar = 0;
            Color c = squares[i][j].getFillColor();
            if (c != Color::White)
            {
                if (squares[i][j + 1].getFillColor() != w) // down
                {
                    neighbors++;
                    if (squares[i][j + 1].getFillColor() == c)
                        similar++;
                }
                if (squares[i+1][j + 1].getFillColor() != w) // down right
                {
                    neighbors++;
                    if (squares[i+1][j + 1].getFillColor() == c)
                        similar++;
                }
                if (squares[i-1][j + 1].getFillColor() != w) // down left
                {
                    neighbors++;
                    if (squares[i-1][j + 1].getFillColor() == c)
                        similar++;
                }
                if (squares[i][j - 1].getFillColor() != w) // up
                {
                    neighbors++;
                    if (squares[i][j - 1].getFillColor() == c)
                        similar++;
                }
                if (squares[i + 1][j - 1].getFillColor() != w) // up right
                {
                    neighbors++;
                    if (squares[i + 1][j - 1].getFillColor() == c)
                        similar++;
                }
                if (squares[i - 1][j - 1].getFillColor() != w) // up left
                {
                    neighbors++;
                    if (squares[i - 1][j - 1].getFillColor() == c)
                        similar++;
                }
                if (squares[i-1][j].getFillColor() != w) // left
                {
                    neighbors++;
                    if (squares[i-1][j].getFillColor() == c)
                        similar++;
                }
                if (squares[i + 1][j].getFillColor() != w) // right
                {
                    neighbors++;
                    if (squares[i + 1][j].getFillColor() == c)
                        similar++;
                }
                if (neighbors != 0)
                {
                    if ((double)similar / neighbors < (double)similarity / 100)
                        unhappySquares.push_back(make_pair(i, j));
                }
            }
        }
    }
	return unhappySquares;
}

/*
Swaps color of unhappy square with another unhappy square or white
*/
void makeHappy(vector<pair<int, int>>& unhappy, vector<vector<RectangleShape>> &squares, int grid)
{
    //Adds white agents to unhappy list
    for (int i = 0; i < grid; i++)
    {
        for (int j = 0; j < grid; j++)
        {
            if (squares[i][j].getFillColor() == Color::White)
                unhappy.push_back(make_pair(i, j));
        }
    }
    //Swaps every agent
    while (unhappy.size() != 0)
    {
        int randLoc = rand() % unhappy.size();
        int i = unhappy[randLoc].first;
        int j = unhappy[randLoc].second;
        Color temp = squares[i][j].getFillColor();
        squares[i][j].setFillColor(squares[unhappy.front().first][unhappy.front().second].getFillColor());
        (squares[unhappy.front().first][unhappy.front().second].setFillColor(temp));

        unhappy.erase(unhappy.begin() + randLoc);
        if (unhappy.size()!=0)
            unhappy.erase(unhappy.begin());
    }
}