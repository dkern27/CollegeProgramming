double ratio (string dna)
{
	int cg=0;
	while (dna.find('c')!=-1)
	{
		cg++;
		dna.replace(dna.find('c'),1,1,'x');
	}

		while (dna.find('g')!=-1)
	{
		cg++;
		dna.replace(dna.find('g'),1,1,'x');
	}

return (double(cg)/dna.size());
}