package pacman.entries.jmelPacMan.EA;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;

import pacman.Executor;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.entries.jmelPacMan.BTPacMan.PacManBT;
import pacman.entries.jmelPacMan.BTPacMan.PacManContext;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

/**
 * Class used to run a evolutionary algorithm on a Behaviour Trees used in a Ms PacMan controller.
 * 
 * @author Jakob Melnyk (jmel)
 */
public class BTEvolver
{
	/**
	 * The executor used to run the trials.
	 */
	private static Executor exec;

	/**
	 * The controller to run trials against.
	 */
	private static Controller<EnumMap<GHOST, MOVE>> ghostController = new StarterGhosts();

	/**
	 * The number of trials to run.
	 */
	private int trials;

	/**
	 * The number of parents to use when mutating.
	 */
	private int parentsToSelect;

	/**
	 * The number of new children to generate per generation.
	 */
	private int childrenPerGeneration;

	/**
	 * The population to evolve.
	 */
	public PMCGenotype[] population;

	/**
	 * The random generator used in mutation.
	 */
	private Random r;

	/**
	 * Runs an instance of the BTEvolver class.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		new BTEvolver(10, 2, 10, 200, 1000);
	}

	/**
	 * Instantiates a new instance of the BTEvolver class, generates and evolves the population and then saves (and prints) the
	 * population after a number of generations.
	 * 
	 * @param popSize
	 *            The size of the population.
	 * @param parents
	 *            The number of parents to select for mutation.
	 * @param children
	 *            The number of children a new generation should produce.
	 * @param generations
	 *            The number of generations to evolve.
	 * @param trials
	 *            The number of trials when testing.
	 */
	public BTEvolver(int popSize, int parents, int children, int generations, int trials)
	{
		exec = new Executor();
		r = new Random();

		population = new PMCGenotype[popSize];
		this.parentsToSelect = parents;
		this.trials = trials;
		this.childrenPerGeneration = children;

		System.out.println("-------Generation " + 0 + "-------");
		
		// Generate initial population and calculate their fitness
		for (int i = 0; i < popSize; i++)
		{
			PMCGenotype pmcg = PMCGenotype.generateRandom(r);
			pmcg.setFitness(fitness(pmcg, this.trials));
			population[i] = pmcg;
			System.out.println(pmcg.getFitness() + " - " + pmcg.minimumDistance + " - " + pmcg.fleeDistance + " - " + pmcg.eatDistance);
		}

		// Advance generation
		for (int i = 0; i < generations; i++)
		{
			this.population = this.advanceGeneration();

			System.out.println("-------Generation " + (i + 1) + "-------");
			
			for (PMCGenotype g : population)
			{
				System.out.println(g.getFitness() + " - " + g.minimumDistance + " - " + g.fleeDistance + " - " + g.eatDistance);
			}
		}
	}

	/**
	 * Advances the population to the next generation.
	 * 
	 * @return The next generation of the population.
	 */
	private PMCGenotype[] advanceGeneration()
	{
		PMCGenotype[] newPop = new PMCGenotype[population.length];
		PMCGenotype[] parents = selectParents();
		ArrayList<PMCGenotype> candidates = new ArrayList<PMCGenotype>();

		// Add the current population to the list of candidates.
		for (PMCGenotype pmcg : population)
		{
			candidates.add(pmcg);
		}

		// For each parent, generate a number of mutations (children), calculate their fitness
		// and add them to the list of candidates.
		int childrenPerParent = this.childrenPerGeneration / this.parentsToSelect;
		for (PMCGenotype p : parents)
		{
			for (int i = 0; i < childrenPerParent; i++)
			{
				PMCGenotype pmcg = PMCGenotype.mutate(p, r);
				pmcg.setFitness(fitness(pmcg, this.trials));
				candidates.add(pmcg);
			}
		}

		// Find candidate with the best fitness and add it to the new population.
		// Repeat this until the new population size matches the previous population size.
		int candidatesChosen = 0;
		while (candidatesChosen < this.population.length)
		{
			int numberOfRemainingCandidates = candidates.size();
			double currentBestFitnessScore = 0;
			int currentBestFitnessScoreIndex = 0;

			for (int i = 0; i < numberOfRemainingCandidates; i++)
			{
				PMCGenotype pmcg = candidates.get(i);
				if (pmcg.getFitness() > currentBestFitnessScore)
				{
					currentBestFitnessScore = pmcg.getFitness();
					currentBestFitnessScoreIndex = i;
				}
			}

			newPop[candidatesChosen] = candidates.get(currentBestFitnessScoreIndex);
			candidatesChosen++;

			candidates.remove(currentBestFitnessScoreIndex);
		}

		return newPop;
	}

	/**
	 * Selects the parents of the current generation.
	 * 
	 * @return The selected parents.
	 */
	private PMCGenotype[] selectParents()
	{
		PMCGenotype[] bestGenes = new PMCGenotype[parentsToSelect];

		int lowestFitnessOfSelectedIndex = 0;
		double lowestFitnessOfSelected = -1;

		// Select initial set of parents.
		bestGenes[0] = population[0];
		lowestFitnessOfSelectedIndex = 0;
		lowestFitnessOfSelected = bestGenes[0].getFitness();
		for (int i = 1; i < this.parentsToSelect; i++)
		{
			bestGenes[i] = population[i];

			if (bestGenes[i].getFitness() < lowestFitnessOfSelected)
			{
				lowestFitnessOfSelectedIndex = i;
				lowestFitnessOfSelected = bestGenes[i].getFitness();
			}
		}

		// Select candidates with the highest fitness, replacing those with lowest fitness.
		for (int i = parentsToSelect; i < population.length; i++)
		{
			PMCGenotype pmcg = population[i];
			if (pmcg.getFitness() > lowestFitnessOfSelected)
			{
				bestGenes[lowestFitnessOfSelectedIndex] = pmcg;

				lowestFitnessOfSelectedIndex = 0;
				lowestFitnessOfSelected = bestGenes[0].getFitness();

				for (int j = 1; j < parentsToSelect; j++)
				{
					if (bestGenes[j].getFitness() < lowestFitnessOfSelected)
					{
						lowestFitnessOfSelectedIndex = j;
						lowestFitnessOfSelected = bestGenes[j].getFitness();
					}
				}
			}
		}

		return bestGenes;
	}

	/**
	 * Encodes a phenotype to a genotype.
	 * 
	 * @param pmc
	 *            The phenotype to encode.
	 * @return The encoded genotype.
	 */
	@SuppressWarnings("unused")
	private static PMCGenotype encode(PacManContext pmc)
	{
		return new PMCGenotype(pmc.MINIMUM_GHOST_DISTANCE, pmc.FLEE_SEARCH_RANGE, pmc.EAT_GHOST_DISTANCE);
	}

	/**
	 * Decodes a genotype to a phenotype.
	 * 
	 * @param pmc
	 *            The phenotype to decode.
	 * @return The decoded phenotype.
	 */
	private static PacManContext decode(PMCGenotype gene)
	{
		return new PacManContext(gene.minimumDistance, gene.fleeDistance, gene.eatDistance);
	}

	/**
	 * Performs a number of trials to calculate the fitness of a specific gene.
	 * 
	 * @param gene
	 *            The gene to calculate fitness for.
	 * @param trials
	 *            The number of trials to run in the calculation.
	 * @return The fitness of the gene.
	 */
	private static double fitness(PMCGenotype gene, int trials)
	{
		Controller<MOVE> btc = new PacManBT(decode(gene));
		return exec.runExperiment(btc, ghostController, trials);
	}
}
