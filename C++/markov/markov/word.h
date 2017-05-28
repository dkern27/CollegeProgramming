#ifndef WORD_H
#define WORD_H

#include <string>
#include <map>
#include <vector>
#include <sstream>

class word
{
public:
	word(std::string s, int k);
	void createMap();
	std::string answer(int sz);

private:
	std::string data;
	int order;
	std::map <std::string, std::vector<std::string>> allkGrams;

};

#endif
