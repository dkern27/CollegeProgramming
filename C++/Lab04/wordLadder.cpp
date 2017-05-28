#include "set.h"


bool compare(string s1, string s2)
{
	int n=0;
	for(int i=0; i < s1.length(); i++)
	{
		if (s1[i]==s2[i])
			n++;
	}
	if (n==s1.length()-1)
		return true;
	return false;
}


int shortest(Vector<string>words,string from, string to)
{
	Set <string> step;
	int size = words.size();
	int answer(3);

	for (int i=0; i<words.size();i++)
	{
		if (compare(from, words[i]))
		{
			step.add(words[i]);
			words.remove(i);
			i-=1;
		}
	}
	Set <string> temp;
	while (answer<size+2 || !step.isEmpty())
	{
		for (string s:step)
		{
			if (compare(to, s))
				return answer;
		}
		for (string s: step)
		{
			for (int j=0; j<words.size();j++)
			{
				if (compare(s,words[j]))
				{
					temp.add(words[j]);
					words.remove(j);
					j-=1;
				}
			}
		}
		step.clear();
		step=temp;
		temp.clear();
		answer++;
	}
	return 0;
}