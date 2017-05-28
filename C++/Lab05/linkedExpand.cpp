node* expand(node*head)
{
	node* ptr= head;
	node* prev = ptr->next;
	while (ptr->next!=NULL)
	{
		node* link = ptr-> next;
		if (ptr->data!=link->data && ptr->data!=prev->data)
		{
			node* newLink = new node;
			newLink->data=ptr->data;
			newLink->next=ptr->next;
			ptr->next = newLink;
			prev=newLink;
		}
		else
			prev = ptr;
		ptr=link;
        if (ptr->next==NULL)
        {
            if ( ptr->data!=prev->data)
            {
                ptr->next = new node;
                ptr->next->data = ptr->data;
                ptr->next->next=NULL;
            }
        }
	}

    if (head -> next == NULL)
    {
        head->next = new node;
        head->next->data = head->data;
        head->next->next = NULL;
    }
	return head;
}