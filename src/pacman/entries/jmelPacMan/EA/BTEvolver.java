package pacman.entries.jmelPacMan.EA;

import java.util.List;
import java.util.Random;

public class BTEvolver
{
	private double mutationProbability;
	private PMCGenotype[] population;
	private Random r;

	public BTEvolver(int popSize, double mutationProb)
	{
		this.mutationProbability = mutationProb;
		population = new PMCGenotype[popSize];
		for(int i = 0; i < popSize; i++)
		{
			population[i] = generateRandomGene();
		}
	}
	
	public PMCGenotype generateRandomGene()
	{
		int m = r.nextInt(40);
		int f = r.nextInt(400);
		int e = r.nextInt(200);
		return new PMCGenotype(m,f,e);
	}
	
	public List<PMCGenotype> selectParents()
	{
		return null;
	}
	
	public static int fitness(PMCGenotype gene)
	{
		return 0;
	}
	
	public static PMCGenotype mutate(PMCGenotype gene)
	{
		return gene;
	}
}
