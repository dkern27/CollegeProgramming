#include <vector>

int cycle_length(node* head)
{
	vector<node*> nodes;
	int count=1;
	nodes.push_back(head);
	node* ptr=head;
	while (ptr ->next!=NULL)
	{
		ptr=ptr->next;
		for (int i=0; i < nodes.size();i++)
			{
				if (ptr == nodes[i])
					return (count -i);	
			}
		nodes.push_back(ptr);
		count++;
	}
	return -1;
}