/*
imgeditor.cpp

author: Dylan Kern
date: 1/21/15

Image editing program for CSCI 262, Fall 2014, Assignment 1.
*/

#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <vector>

using namespace std;

// Global constants 
// Use these!  E.g., 
//	if (selections[FLATTEN_BLUE]) { // call flatten_blue() here }
static const int DO_NOTHING = 0;
static const int GRAYSCALE = 1;
static const int FLIP_HORIZONTAL = 2;
static const int NEGATE_RED = 3;
static const int NEGATE_GREEN = 4;
static const int NEGATE_BLUE = 5;
static const int FLATTEN_RED = 6;
static const int FLATTEN_GREEN = 7;
static const int FLATTEN_BLUE = 8;
static const int EXTREME_CONTRAST = 9;
static const int FLIP_VERTICAL = 10;
static const int BLUR = 11;

static const int EFFECT_COUNT = 12;

static const string MENU_STRINGS[] = {
	"do nothing",
	"convert to grayscale",
	"flip horizontally",
	"negate red",
	"negate green",
	"negate blue",
	"flatten red",
	"flatten green",
	"flatten blue",
	"extreme contrast",
	"flip vertically",
	"blur"
};

// function prototypes
void get_user_selections(bool*);

void grayscale(vector<vector<int>>&, int, int);
void horizontal(vector<vector<int>>&, int, int);
void negate_red(vector<vector<int>>&, int, int, int);
void negate_green(vector<vector<int>>&, int, int, int);
void negate_blue(vector<vector<int>>&, int, int, int);
void flatten_red(vector<vector<int>>&, int, int);
void flatten_green(vector<vector<int>>&, int, int);
void flatten_blue(vector<vector<int>>&, int, int);
void contrast(vector<vector<int>>&, int, int, int);
void vertical(vector<vector<int>>&, int, int);
void blur(vector<vector<int>>&, vector<vector<int>>, int, int);

// MAIN
int main() {
    // user interaction to obtain input filename
    cout << "Portable Pixmap (PPM) Image Editor!" << endl << endl;
    cout << "Enter name of image file: ";
    string infile;
    getline(cin, infile);

    // open input filestream
    ifstream fin;
    fin.open(infile);
    if (fin.fail()) {
        cerr << "Error opening input file '";
        cerr << infile << "', exiting!" << endl;
        // Uncomment if running from within Visual Studio and
        // you want to see the error message.
        // system("pause");
        return -1;
    }

    // read magic 
    string magic;
    fin >> magic;
    if (magic != "P3") {
        cerr << "Input file '" << infile;
        cerr << "' appears to not be a PPM, exiting!" << endl;
        fin.close();
        // Uncomment if running from within Visual Studio and
        // you want to see the error message.
        // system("pause");
        return -1;
    }

    // read in columns/rows/depth values
    int columns, rows, maxColor;
    fin >> columns;
    fin >> rows;
    fin >> maxColor;
    vector<vector<int>> imgData(rows, vector <int>(columns * 3));

    // get output filename and open output file DONE
    cout << "Enter name of output file: ";
    string outfile;
    getline(cin, outfile);
    ofstream fout;
    fout.open(outfile);
    if (fout.fail())
    {
        cerr << "Error opening output file '";
        cerr << outfile << "', exiting";
        return -1;
    }

    // output magic, columns, rows, depth values
	fout << magic << "\n" << columns << " " << rows << "\n" << maxColor << endl;;



    // display menu and get user selections
    bool selections[EFFECT_COUNT];
    get_user_selections(selections);

    //Read in pixel values
    for (int i = 0; i < rows; i++)
    {
        for (int j = 0; j < columns * 3; j++)
        {
            fin >> imgData[i][j];
        }
    }

    //Effects
    if (selections[GRAYSCALE])
        grayscale(imgData, columns, rows);

    if (selections[FLIP_HORIZONTAL])
        horizontal(imgData, columns, rows);

    if (selections[NEGATE_RED])
        negate_red(imgData, columns, rows, maxColor);

    if (selections[NEGATE_GREEN])
        negate_green(imgData, columns, rows, maxColor);

    if (selections[NEGATE_BLUE])
        negate_blue(imgData, columns, rows, maxColor);

    if (selections[FLATTEN_RED])
        flatten_red(imgData, columns, rows);

    if (selections[FLATTEN_GREEN])
        flatten_green(imgData, columns, rows);

    if (selections[FLATTEN_BLUE])
        flatten_blue(imgData, columns, rows);

    if (selections[EXTREME_CONTRAST])
        contrast(imgData, columns, rows, maxColor);

	if (selections[FLIP_VERTICAL])
		vertical(imgData, columns, rows);
	
	if (selections[BLUR])
		blur(imgData, imgData, columns, rows);


    // write to output
    for (int i = 0; i < rows; i++)
    {
        for (int j = 0; j < columns * 3; j++)
        {
            fout << imgData[i][j] << " ";
        }
		fout << "\n";
    }
	//close input and output files
    fin.close();
    fout.close();

    cout << outfile << " created" << endl;

	// Uncomment if desired to use under Visual Studio
	system("pause");

	return 0;
}

// functions provided for you - you shouldn't need to modify these
void get_user_selections(bool* selections) {
	// initialize selections
	for (int i = 0; i < EFFECT_COUNT; i++) selections[i] = false;

	// display menu
	cout << endl;
	cout << "Here are your choices:" << endl;
	int columns = 80;
	for (int i = 0; i < EFFECT_COUNT; i++) {
		// approximate length of menu item assuming 2 digit numbers
		int len = MENU_STRINGS[i].length() + 6;
		columns -= len;
		if (columns < 0) {
			cout << endl;
			columns = 80 - len;
		}
		cout << "[" << i << "] " << MENU_STRINGS[i] << " ";
	}
	cout << endl;
	cout << "Please enter your selections as a comma separated list, e.g., '5,6,1':" << endl;

	// get user response and extract numbers from it
	string response;
	getline(cin, response);

	// actually allows pretty much any characters between integers
	istringstream sin(response);
	while (sin) {
		while ((sin.peek() < '0' || sin.peek() > '9') && sin.peek() != EOF) sin.ignore();
		if (sin.peek() == EOF) break;
		int n;
		sin >> n;
		if (n < EFFECT_COUNT) {
			selections[n] = true;
		}
		else {
			cout << "Invalid selection " << n << " ignored!" << endl;
		}
	}
}

//Averages rgb values and sets each to average
void grayscale(vector<vector<int>> &imgdata, int column, int row)
{
    int gray;
    for (int i = 0; i < row; i++)
    {
        for (int j = 0; j < (column * 3); j+=3)
        {
            gray = (imgdata[i][j] + imgdata[i][j+1] + imgdata[i][j+2]) / 3;
            imgdata[i][j] = gray;
            imgdata[i][j + 1] = gray;
            imgdata[i][j + 2] = gray;
        }
    }
}

//Flips image horizontally by row
void horizontal(vector<vector<int>> &imgdata, int column, int row)
{
    int temp1(0), temp2(0), temp3(0);
    for (int i = 0; i < row; i++)
    {
        for (int j = 0; j < (column*3/2); j+=3)
        {
            temp1 = imgdata[i][j];
            temp2 = imgdata[i][j + 1];
            temp3 = imgdata[i][j + 2];
            imgdata[i][j] = imgdata[i][3 *column - 3 - j];
            imgdata[i][j + 1] = imgdata[i][3 *column - 2 - j];
            imgdata[i][j + 2] = imgdata[i][3 *column - 1 - j];
            imgdata[i][3 * column - 3-j] = temp1;
            imgdata[i][3 * column - 2-j] = temp2;
            imgdata[i][3 * column - 1-j] = temp3;
        }
    }
}

void negate_red(vector<vector<int>> &imgdata, int column, int row, int maxColor)
{
    for (int i = 0; i < row; i++)
    {
        for (int j = 0; j < (column * 3); j+=3)
        {
            imgdata[i][j] = abs(imgdata[i][j] - maxColor);
        }
    }
}

void negate_green(vector<vector<int>> &imgdata, int column, int row, int maxColor)
{
    for (int i = 0; i < row; i++)
    {
        for (int j = 1; j < (column * 3); j += 3)
        {
            imgdata[i][j] = abs(imgdata[i][j] - maxColor);
        }
    }
}


void negate_blue(vector<vector<int>> &imgdata, int column, int row, int maxColor)
{
    for (int i = 0; i < row; i++)
    {
        for (int j = 2; j < (column * 3); j += 3)
        {
            imgdata[i][j] = abs(imgdata[i][j] - maxColor);
        }
    }
}

void flatten_red(vector<vector<int>> &imgdata, int column, int row)
{
    for (int i = 0; i < row; i++)
    {
        for (int j = 0; j < (column * 3); j += 3)
        {
            imgdata[i][j] = 0;
        }
    }
}

void flatten_green(vector<vector<int>> &imgdata, int column, int row)
{
    for (int i = 0; i < row; i++)
    {
        for (int j = 1; j < (column * 3); j += 3)
        {
            imgdata[i][j] = 0;
        }
    }
}

void flatten_blue(vector<vector<int>> &imgdata, int column, int row)
{
    for (int i = 0; i < row; i++)
    {
        for (int j = 2; j < (column * 3); j += 3)
        {
            imgdata[i][j] = 0;
        }
    }
}

void contrast(vector<vector<int>> &imgdata, int column, int row, int maxColor)
{
    for (int i = 0; i < row; i++)
    {
        for (int j = 0; j < (column * 3); j++)
        {
            if (imgdata[i][j] <= maxColor / 2)
                imgdata[i][j] = 0;
            else
                imgdata[i][j] = maxColor;
        }
    }
}

void vertical(vector<vector<int>> &imgdata, int column, int row)
{
	int temp1(0);
	for (int j = 0; j < column*3; j++)
	{
		for (int i = 0; i < row/2; i++)
		{
			temp1 = imgdata[i][j];
			imgdata[i][j] = imgdata[row-1-i][j];
			imgdata[row-1-i][j] = temp1;
		}
	}
}

void blur(vector<vector<int>> &imgdata, vector<vector<int>> temp, int column, int row)
{
	int average1(0),average2(0);
	//Top and bottom row
	for (int j = 0; j < column * 3; j++)
	{
		imgdata[0][j] = temp[1][j];
		if (j < 3)
		{
			average1 = (temp[0][j]+temp[1][j] + temp[0][j + 3]) / 3;
			average2 = (temp[row-1][j]+temp[row - 1][j] + temp[row - 2][j + 3]) / 3;
		}
		else if (j > column*3-4)
		{
			average1 = (temp[0][j]+temp[1][j] + temp[0][j - 3]) / 3;
			average2 = (temp[row - 1][j] + temp[row - 2][j] + temp[row - 1][j-3]) / 3;
		}
		else
		{
			average1 = (temp[0][j]+temp[1][j] + temp[0][j - 3] + temp[0][j + 3]) / 4;
			average2 = (temp[row - 1][j] + temp[row - 2][j] + temp[row - 1][j - 3] + temp[row - 1][j + 3]) / 4;
		}
		imgdata[0][j] = average1;
		imgdata[row - 1][j] = average2;
	}
	
	//Sides of image
	for (int i = 1; i < row-1; i++)
	{
        for (int j = 0; j < 3; j++)
        {
            imgdata[i][j] = (temp[i - 1][0] + temp[i + 1][0] + temp[i][0] + temp[i][j+3]) / 4;
            imgdata[i][column * 3- 1 - j] = (temp[i - 1][column * 3 - 1 - j] + temp[i + 1][column * 3 - 1 - j] + temp[i][column * 3 - 4 - j]) / 4;
        }
	}
	
	//middle of image
	for (int i = 1; i < row - 1; i++)
	{
		for (int j = 3; j < column*3-3; j++)
		{
			imgdata[i][j] = (temp[i][j] + temp[i + 1][j] + temp[i - 1][j] + temp[i][j - 3] + temp[i][j + 3]) / 5;
		}
	}


}