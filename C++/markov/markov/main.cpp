#include <iostream>
#include <fstream>
#include <sstream>
#include <cstdlib>
#include <ctime>
#include "model.h"

#include <vector>
#include <map>

#include "word.h"

using namespace std;

void init_random() {
	// use this to get the same random sequence every time
//	srand(1234);

	// use this to get a different random sequence every time
	time_t t = time(NULL);
	srand(t);
}

int main(int argc, char* argv[]) {
	string infile;
	int k;
	int sz;
	string model_type;

	// allow command line "markov filename k sz model", or partial
	// command line, prompting for missing items
	if (argc > 1) infile = string(argv[1]);
	else {
		cout << "Enter a filename: ";
		cin >> infile;
	}

	if (argc > 2) k = atoi(argv[2]);
	else {
		cout << "Enter Markov model order (k): ";
		cin >> k;
	}

	if (argc > 3) sz = atoi(argv[3]);
	else {
		cout << "Enter size of output text: ";
		cin >> sz;
	}

	if (argc > 4) model_type = string(argv[4]);
	else {
		cout << "Enter method (brute, map, word): ";
		cin >> model_type;
	}


	// get text from input file
	ifstream in(infile);
	if (!in) {
		cerr << "Error opening input file \"" << infile << "\"!" << endl;
		return -1;
	}
	// get all strings; extra whitespace will be ignored 
	ostringstream text;
	while (!in.eof()) {
		string s;
		in >> s;
		text << s << ' ';
	}
	in.close();

	// initialize random number generator
	init_random();

	// create model and output result
	cout << "RESULT:" << endl;
	if (model_type == "brute") {
		brute_model model(text.str(), k);

		clock_t t1, t2;
		t1 = clock();
		string result = model.generate(sz); 	
		t2 = clock();
		cout << result << endl;
		cout << "Generated " << sz << " characters in " << (t2 - t1)/double(CLOCKS_PER_SEC) << " seconds." << endl;

	}
	else if (model_type == "map")
	{
		string data = text.str() + text.str().substr(0, k);

		map <string, vector<char>> allkGrams;
		string kgram = data.substr(0, k);
		int spot = 0;

		while (spot < data.length() - (k - 1))
		{
			int n = 0;
			vector<char> temp;
			while (data.find(kgram, n) != string::npos)
			{
				temp.push_back(data[data.find(kgram, n) + k]);
				n = data.find(kgram, n) + 1;
			}
			allkGrams.emplace(kgram, temp);
			spot++;
			kgram = data.substr(spot, k);
		}
		clock_t t1, t2;
		t1 = clock();
		string answer;
		answer.reserve(sz);
		int start = rand() % data.length();
		string seed = data.substr(start, k);
		for (int i = 0; i < sz; i++)
		{
			vector <char> temp = allkGrams.at(seed);
			char c = temp[rand() % temp.size()];
			answer.push_back(c);
			seed = seed.substr(1) + c;
		}
		t2 = clock();
		cout << answer << endl;
		cout << "Generated " << sz << " characters in " << (t2 - t1) / double(CLOCKS_PER_SEC) << " seconds." << endl;
	}
	else if (model_type == "word")
	{
		word word(text.str(), k);
		word.createMap();
        clock_t t1, t2;
        string answer;
        t1 = clock();
		answer=word.answer(sz);
        t2 = clock();
        cout << answer << endl;
        cout << "Generated " << sz << " words in " << (t2 - t1) / double(CLOCKS_PER_SEC) << " seconds." << endl;
	}
	else {
		cout << "That model is not yet implemented, sorry." << endl;
		return -1;
	}
	system("PAUSE");
	return 0;
}

