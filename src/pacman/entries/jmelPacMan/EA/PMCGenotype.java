package pacman.entries.jmelPacMan.EA;

import java.util.Random;

/**
 * The genotype of the PacManContext.
 * 
 * @author Jakob Melnyk (jmel)
 */
public class PMCGenotype
{
	/**
	 * How much a value of the genotype can mutate in a single mutation.
	 */
	public static final int MUTATION_PERCENTAGE = 10;
	
	/**
	 * The maximum value of the minimum distance.
	 */
	public static final int MIN_DIST_MAX_VALUE = 40;

	/**
	 * The maximum value of the flee distance.
	 */
	public static final int FLEE_DIST_MAX_VALUE = 400;

	/**
	 * The maximum value of the eat distance.
	 */
	public static final int EAT_DIST_MAX_VALUE = 200;

	/**
	 * The value of the minimum distance for this individual.
	 */
	public final int minimumDistance;
	
	/**
	 * The value of the fleeing distance for this individual.
	 */
	public final int fleeDistance;
	
	/**
	 * The value of the eating distance for this individual.
	 */
	public final int eatDistance;
	
	/**
	 * The fitness of this individual; it is -1 if it has not yet been set.
	 */
	private double fitness = -1;

	/**
	 * Instantiates a new instance of the PMCGenotype class.
	 * @param min The minimum ghost distance value of this individual.
	 * @param flee The flee distance value of this individual.
	 * @param eat The eat distance value of this individual.
	 */
	public PMCGenotype(int min, int flee, int eat)
	{
		this.minimumDistance = min;
		this.fleeDistance = flee;
		this.eatDistance = eat;
	}

	/**
	 * Gets the fitness of this individual.
	 * @return The fitness of this individual.
	 */
	public double getFitness()
	{
		return fitness;
	}
	
	/**
	 * Updates the fitness value of this individual, unless the current fitness is not -1.
	 * @param newFitness The new fitness.
	 */
	public void setFitness(double newFitness)
	{
		if (this.fitness != -1)
		{
			return;
		}

		this.fitness = newFitness;
	}

	/**
	 * Generates a new random individual.
	 * @param r The random object to use in generation.
	 * @return The newly generated individual.
	 */
	public static PMCGenotype generateRandom(Random r)
	{
		int m = r.nextInt(MIN_DIST_MAX_VALUE);
		int f = r.nextInt(FLEE_DIST_MAX_VALUE);
		int e = r.nextInt(EAT_DIST_MAX_VALUE);
		return new PMCGenotype(m, f, e);
	}

	/**
	 * Mutates an input individual into a different individual.
	 * @param individual The individual to mutate.
	 * @param r The random object used in the mutation.
	 * @return A newly mutated individual.
	 */
	public static PMCGenotype mutate(PMCGenotype individual, Random r)
	{
		int minDistMaximumVariance = (MIN_DIST_MAX_VALUE / MUTATION_PERCENTAGE);
		int minDistVariance = r.nextInt(minDistMaximumVariance * 2) - minDistMaximumVariance;
		int m = individual.minimumDistance + minDistVariance;

		int fleeDistMaximumVariance = (FLEE_DIST_MAX_VALUE / MUTATION_PERCENTAGE);
		int fleeDistVariance = r.nextInt(fleeDistMaximumVariance * 2) - fleeDistMaximumVariance;
		int f = individual.fleeDistance + fleeDistVariance;

		int eatDistMaximumVariance = (EAT_DIST_MAX_VALUE / MUTATION_PERCENTAGE);
		int eatDistVariance = r.nextInt(eatDistMaximumVariance * 2) - eatDistMaximumVariance;
		int e = individual.eatDistance + eatDistVariance;

		PMCGenotype mutated = new PMCGenotype(m, f, e);

		return mutated;
	}
}
