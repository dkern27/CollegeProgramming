#include "game.h"

#include <map>
#include <iostream>

using namespace std;

game::game(int wordSize, bool display, vector <string> wordList)
{
    this->wordSize = wordSize;
    for (int i = 0; i < wordSize; i++)
    {
        answer += "-";
        blank += "-";
    }
    displayNumWords = display;
    currentFamily = wordList;
}



void game::chooseFamilies(char guess)
{
    map <string, vector<string>> wordFamilies;
    string key = blank;
    int pos = 0;
    /* Iterates through every word in the current family and finds its key and adds it into the map at that key*/
    for (int i = 0; i < currentFamily.size(); i++)
    {
        while (currentFamily[i].find(guess, pos) != string::npos)
        {
            key[currentFamily[i].find(guess, pos)] = guess;
            pos = currentFamily[i].find(guess, pos) + 1;
        }
        wordFamilies[key].push_back(currentFamily[i]);
        pos = 0;
        key = blank;
    }

    int largest=0;
    vector<string> family;
    /*Finds largest word family*/
    for (auto elem : wordFamilies)
    {
        if (elem.second.size() > largest)
        {
            largest = elem.second.size();
            family = elem.second;
            key = elem.first;
        }
    }

    currentFamily = family;

    for (int i = 0; i < key.length(); i++)
    if (isalpha(answer[i]) == false)
        answer[i] = key[i];

    alreadyGuessed.emplace(guess);
}

/*Prints current game information: already guessed letters, words remaining, number words remaining, and answer progress*/
void game::status()
{
    cout << "Already guessed:";
    for (char c : alreadyGuessed)
        cout << " " << c;

    if (displayNumWords)
        cout << "\nNumber of words remaining : " << currentFamily.size();

    /*
    if (displayWords && currentFamily.size() < 100)
    {
        cout << "\nRemaining words:";
        for (int i = 0; i < currentFamily.size(); i++)
            cout << " " << currentFamily[i];
    }
    */

    cout << "\n\n" << answer << endl;
}

bool game::validGuess(char guess)
{
	if (alreadyGuessed.find(guess) == alreadyGuessed.end())
		return true;
	return false;
}

string game::getCorrectAnswer()
{
	int randNum = rand() % currentFamily.size();
	return currentFamily[randNum];
}

/*Checks if puzzle contains any blanks*/
bool game::solved()
{
    for (int i = 0; i < answer.length(); i++)
    {
        if (answer[i] == '-')
            return false;
    }
    return true;
}