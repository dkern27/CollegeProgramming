#pragma once

#include <string>
#include <vector>
#include <set>

class game
{
public:
    game(int,bool, std::vector <std::string>);
    std::string getAnswer();
    void chooseFamilies(char);
    void getGuesses();
    void status();
    bool solved();
	bool validGuess(char);
	std::string getCorrectAnswer();

    

private:
    bool displayNumWords = false;
    int wordSize;
    std::string answer; //Current progress into solving word
    std::string blank; // Used for word families: always "_" of some length
    std::set <char> alreadyGuessed; 
    std::vector<std::string>currentFamily;

    /*
    For testing: display the current word family when less than 25 words left if true
    Also commented out in game.cpp
    static const int displayWords = false;
    */

};

