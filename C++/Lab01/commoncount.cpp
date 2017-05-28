int count (string a, string b)
{
	int answer(0);
	for (char c:a)
	{
		for (char d:b)
		{
			if (d==c)
			{
				answer++;
				b.replace(b.find(d),1,1,'@');
				break;
			}
		}
	}
	return answer;
}