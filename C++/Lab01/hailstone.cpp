#include <string>
using namespace std;

string sequence(int n)
{
	string answer(to_string(n));
	while (n!=1)
	{
	if (n%2==0)
	{
		n/=2;
		answer.append(" " + to_string(n));
	}	
	else
	{
		n=n*3+1;
		answer.append(" " + to_string(n));
	}
	}
	return answer;
}