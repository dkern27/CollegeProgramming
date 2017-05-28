#include "queue.h"

string winner(Vector<string>bracket,string results)
{
	int size=0;
    	Queue <char> result;
    	for (int i=0; i < results.length(); i++)
        	result.enqueue(results[i]);
	while (bracket.size() !=1)
	{
		size=bracket.size();
		for (int i=0;i<size/2;i++)
		{
			if (bracket[i]=="bye")
				bracket.remove(i);
			else if (bracket[i+1]=="bye")
				bracket.remove(i+1);
			else if (result.peek()=='H')
			{
				bracket.remove(i+1);
                		result.dequeue();			
			}
			else
			{
				bracket.remove(i);
                		result.dequeue();				
			}
		}

	}
	return bracket[0];
}
