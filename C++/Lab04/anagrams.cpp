#include <string>
#include <locale>
#include <algorithm>

Vector<string> anagrams(Vector<string> phrases)
{
	Vector<string>originalPhrases=phrases;
	for (int i=0;i<phrases.size();i++)
	{
		for (int j=0; j<phrases[i].length();j++)
		{
			if(isspace(phrases[i][j]))
			{
				phrases[i].erase(j,1);
				j-=1;
			}
			phrases[i][j]=tolower(phrases[i][j]);
		}
		sort(phrases[i].begin(),phrases[i].end());
	}
	for (int i=0; i<phrases.size(); i++)
	{
		for (int j=i+1;j<phrases.size();j++)
		{
			if (phrases[i]==phrases[j])
			{
				originalPhrases.remove(j);
				phrases.remove(j);
				j-=1;
			}
		}
	}
	return originalPhrases;
}