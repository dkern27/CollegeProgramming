#include "set.h"
#include <cmath>

int points(Vector<string> &player, Vector<string> &dictionary)
{
	Set <string> p;
	for (string s: player)
		p.add(s);

	Set <string> d;
	for (string s: dictionary)
		d.add(s);
	
	Set <string> intersect = p*d;
	int answer(0);
	while (!intersect.isEmpty())
	{
		answer+=pow(intersect.first().length(),2);
		intersect.remove(intersect.first());
	}
	return answer;
	
}