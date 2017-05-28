#include "stack.h"


bool is_valid(string expr, string left, string right)
{
	Stack <char> newExpr;
	
	string delims= left+right;
	

	//Check for delims in expr
	for (int j=0; j < expr.length(); j++)
	{
		for (int i=0;i<delims.length();i++)
		{
			if (expr[j]==delims[i])
				newExpr.push(expr[j]); //Stack of delimiters
		}
	}

	string answer;
	int n=newExpr.size();

	//write newExpr to string
	for (int i=0; i<n;i++)
	{
		answer.insert(0,1,newExpr.top());
		newExpr.pop();
	}
	
	if (answer.size()%2==1)
		return false;

	for (int i=0; i<left.length();i++)
	{		
		if (answer.find(left[i]) > answer.find(right[i]))
			return false;
		if (answer.find(left[i])!=-1 && answer.find(right[i])!=-1)
		{
			if ((answer.find(right[i])-answer.find(left[i]))%2==0)
				return false;
			else
			{
				answer.erase(answer.find(left[i]),1);
				answer.erase(answer.find(right[i]),1);
			}
		}
	}
		
	
	return true;

   	
}