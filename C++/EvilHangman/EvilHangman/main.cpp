/*
Assignment 04: Evil Hangman
CSCI262
Dylan Kern

User plays hangman against the computer
Computer has word bank of words of some length which it uses to cheat by choosing largest group of words that the answer could be
*/


#include <iostream>
#include <string>
#include <fstream>
#include <vector>

#include "game.h"

using namespace std;

vector <string> readFile(string); //Read in dictionary
int getLength(vector<string>&); //Get word length
int getGuesses(); //Get number of guesses
bool yesNo(string); //Yes or No questions

const string FILENAME= "dictionary.txt";

int main()
{
    vector<string> dictionary = readFile(FILENAME);
    bool keepPlaying = true;
    do //Play again loop
    {
        cout << "Let's play Hangman!" << endl;
        int wordLength = getLength(dictionary);

        int guesses = getGuesses();

        //Display size of word list
        bool display=yesNo("Display the number of remaining words?");

        vector<string>wordList;
        //Wordlist of words length wordLength
        for (int i = 0; i < dictionary.size(); i++)
        {
            if (dictionary[i].length() == wordLength)
                wordList.push_back(dictionary[i]);
        }

        game game(wordLength, display, wordList);
        int usedGuesses = 0;

        //Current Game Loop
        while (usedGuesses < guesses && !game.solved())
        {
            cout << "\nRemaining guesses: " << guesses - usedGuesses << endl;
            game.status();

            string attemptGuess;
            char guess;
            //Guessing loop
            do
            {
                cout << "\nGuess a letter: ";
                getline(cin, attemptGuess);

                //Checks valid input
                if (attemptGuess.length() != 1 || isalpha(attemptGuess[0]) == false)
                {
                    cout << "Invalid guess" << endl;
                }
                else if (game.validGuess(attemptGuess[0]) == false)
                    cout << "Already guessed" << endl;
            } while (attemptGuess.length() != 1 || isalpha(attemptGuess[0]) == false || game.validGuess(attemptGuess[0]) == false);
            guess = tolower(attemptGuess[0]);

            game.chooseFamilies(guess);
            usedGuesses++;
        }

        if (game.solved())
        {
            cout << "\nCongratulations, you win!" << endl;
        }
        else
        {
            cout << "\nYou lose!\nThe correct answer was: " << game.getCorrectAnswer() << endl;
        }

        keepPlaying = yesNo("Play again?");
        cout << "\n";
    } while (keepPlaying);
	//system("PAUSE");
	return 0;
}

vector <string> readFile(string file)
{
	ifstream infile;
	infile.open(file);
	if (infile.fail())
	{
		cout << "Error opening input file";
		exit(1);
	}
	string s;
	vector<string> dictionary;
	while (!infile.eof())
	{
		infile >> s;
		dictionary.push_back(s);
	}
	return dictionary;
}

/*
Prompts the user until a valid length is input
*/
int getLength(vector<string> & dictionary)
{
    int wordLength = 0;
    bool valid = false;
    do {
        cout << "Enter the word length: ";
        string templength;
        getline(cin, templength);
        bool invalidInput = false;
        for (int i = 0; i < templength.length(); i++)
        {
            if (!isdigit(templength[i]))
            {
                invalidInput = true;
                break;
            }
        }
        if (invalidInput)
            wordLength = 0;
        else
            wordLength = stoi(templength);
        for (int i = 0; i < dictionary.size(); i++)
        {
            if (wordLength == dictionary[i].length())
            {
                valid = true;
                break;
            }
        }
        if (!valid)
            cout << "Invalid word length" << endl;
    } while (!valid);
    return wordLength;
}

/*
Prompts user until valid number of guesses is input
*/
int getGuesses()
{
    int guesses;
    do {
        string guess;
        bool validInput = true;
        cout << "Number of guesses: ";
        getline(cin, guess);
        for (int i = 0; i < guess.length(); i++)
        {
            if (!isdigit(guess[i]))
            {
                validInput = false;
                break;
            }
        }
        if (!validInput)
            guesses = 0;
        else
            guesses = stoi(guess);

        if (guesses < 1)
            cout << "Invalid number of guesses" << endl;
    } while (guesses < 1);
    return guesses;
}

/*
Generic function for any yes/no question
*/
bool yesNo(string message)
{
    cout << message << " [Y/N] ";
    while (true)
    {
        string userInput;
        getline(cin, userInput);
        for (int i = 0; i < userInput.length(); i++)
            userInput[i] = towlower(userInput[i]);
        if (userInput == "y" || userInput == "yes")
            return true;
        else if (userInput == "n" || userInput == "no")
            return false;
        else
            cout << "Invalid Input\n" << message << " [Y/N] ";
    }
}