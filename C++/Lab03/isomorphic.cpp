#include <map>
bool compareValue(map<char,char>&, char, int, string);

int count_pairs(Vector<string> &words)
{
	map <char, char> replace;
	int answer(0), place(1);
	string temp;
	for (string s: words)
	{
		for (int i=place; i<words.size(); i++)
		{
			temp=s;
			for (int k=0; k<s.length();k++)
			{
				if(compareValue(replace, words[i].at(k),k,words[i]))
					replace.emplace(s[k], words[i].at(k));
			}
			for (int j=0;j<s.length();j++)
			{
				temp[j]=replace[s[j]];
			}
			if (temp==words[i])
				answer++;
			replace.clear();
		}
		place++;	
	}
	return answer;
	
}

bool compareValue(map<char,char> &replace, char b, int i, string test)
{
	int n=0;
	for (int k=0; k < i; k++)
	{
		if (test[k]==b)
			n++;
	}
	if (n==0)
		return true;
	return false;
}