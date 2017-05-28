node* compress(node* head) 
{
	node* ptr = head;
	while (ptr->next!=NULL)
	{
		node* link = ptr->next;
		if (ptr->data == (link->data))
		{
			link  = ptr->next;
			ptr->next = link->next;
			delete link;
			
		}
		else
		{
			ptr = link;
		}
		
	}

	return head;
}
