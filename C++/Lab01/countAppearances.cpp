int number_times_appear(int number, int digit)
{
	int count=0;
	while (number!=0)
	{
		if (number%10==digit)
			count++;
		number/=10;
	}
	return count;
}