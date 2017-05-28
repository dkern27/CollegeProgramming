#include "map.h"

string encrypt(string message)
{
	Map <char, char> code;
	int count(0);
	for (int i=0; i<message.length();i++)
	{
		if (!code.containsKey(message[i]))
		{
			code.put (message[i], 'a'+ count);
			count ++;
		}
	}
	string answer;
	for (char c: message)
		answer+=code.get(c);
	return answer;
}