#include "set.h"
int which_order(Vector<string> &ingredients, Vector <string> &orders)
{
	Set <string> ingred;
	for (string s: ingredients)
		ingred.add(s);
	Set<string> checkOrder;
	int answer(0);
	
	for(string s:orders)
	{
		int n=0;
		for (int i=0; i < s.length();i++)
		{
			if (isspace(s[i]))
			{	
				checkOrder.add(s.substr(n,i-n));
				n=i+1;
			}	
		}
		checkOrder.add(s.substr(n, -1));
		if (checkOrder.isSubsetOf(ingred))
			return answer;
		checkOrder.clear();
		answer++;
	}
	return -1;
	
}