#include "set.h"

Vector<string> whos_dishonest(Vector<string> &club1, Vector<string> &club2, Vector<string> &club3)
{
	Set <string> c1;
	Set <string> c2;
	Set <string> c3;
	for(string s: club1)
		c1.add(s);
	for(string s: club2)
		c2.add(s);
	for(string s: club3)
		c3.add(s);

	Set <string> c;

	c = c1*c2 + c1*c3 + c2*c3;
	
	Vector <string> answer;
	
	for (string s: c)
	{
		answer.add(s);	
	}

	return answer;
}