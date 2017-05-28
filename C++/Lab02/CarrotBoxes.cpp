int last_box(Vector<int> &carrots, int amount) 
{
	int max,i(0);
	do
	{
		max=0;
		for (int j=0; j < carrots.size(); j++)
		{
			if (carrots[max] < carrots[j])
				max=j;

		}
		carrots[max]--;
		i++;
	} while (i!=amount);
	return max;
}