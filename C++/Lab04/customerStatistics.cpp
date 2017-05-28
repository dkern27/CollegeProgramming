#include <algorithm>
#include <string>

bool contains(Vector<string> vec, string test)
{
	for (int i=0; i< vec.size();i++)
	{
		if (test==vec[i])
			return true;
	}
	return false;
}

Vector<string> report_duplicates(Vector<string> names) 
{
	int occur=0;
	Vector<string> answer;
	for ( int i=0; i < names.size(); i++)
	{
		for (int j=0; j < names.size();j++)
		{
			if (names[i]==names[j])
				occur++;
		}
		string temp = names[i] + (char)32 + to_string(occur);
		if (occur > 1 && contains(answer, temp)==false)
			answer.add(temp);
		occur=0;
	}
	sort(answer.begin(),answer.end());
	return answer;
}