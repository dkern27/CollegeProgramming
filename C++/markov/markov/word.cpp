#include "word.h"

#include <iostream>
#include <iterator>

using namespace std;

word::word(string s, int k)
{
	data = s;
	order = k;
}

void word::createMap()
{
	istringstream text(data);
	vector<string> list;
	string kgram;
	string temp;

	for (int i = 0; i < order; i++) //Grabs k words
	{
		text >> temp;
		kgram += temp;
		if (order != 1 && i != order - 1)
			kgram += " ";
		temp.clear();
	}

	while (text >> temp)
	{
		int n = 0;
        int k;
		//if statement to see if key already exists?
		while (data.find(kgram + " ", n) != string::npos)
		{  
            k = data.find(kgram + " ", n) + kgram.length() + 1;
			list.push_back(data.substr(k, data.find(" ", k)-k));
			n = data.find(kgram + " ", n) + kgram.length();
		}
		allkGrams.emplace(kgram, list);
		list.clear();
        kgram = kgram.substr(kgram.find(" ") + 1) + " " + temp;
		temp.clear();
	}
}

string word::answer(int sz)
{
	string answer;
	//Generate random k words
	int start = rand() % allkGrams.size();
	map<string,vector<string>>::iterator randItem=allkGrams.begin();
	advance(randItem, start);
	string seed = randItem->first;

	vector<string> temp;
    string word;
	for (int i = 0; i < sz; i++)
	{
		temp = allkGrams.at(seed);
		word = temp[rand() % temp.size()];
		answer = answer + " " + word;
		seed=seed.substr(seed.find(" ")+1)+ " " + word;
        temp.clear();
        word.clear();
        
	}
	return answer;

}

