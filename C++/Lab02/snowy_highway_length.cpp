int snowy_highway_length(Vector<int> &start_points, Vector <int> & end_points)
{
	Vector<int>covered(10000,0);
	
	for(int i=0; i<start_points.size(); i++)
	{
		for(int j=start_points[i]; j<end_points[i]; j++)
			covered[j] = 1;
	}

	int answer=0;
	for (int i=0; i<covered.size();i++)
		answer+=covered[i];
	return answer;
}