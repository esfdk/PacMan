package pacman.entries.jmelPacMan.NN;

import java.util.ArrayList;
import java.util.List;

public class Neuron
{
	/**
	 * The number of inputs the neuron takes.
	 */
	public int numberOfInputs;

	/**
	 * The output of the neuron.
	 */
	private double output;
	/**
	 * The weights of the various inputs.
	 */
	private List<Synapse> inputs;

	private double weightedSum;
	private double error;

	public Neuron()
	{
		inputs = new ArrayList<Synapse>();
		numberOfInputs = 0;
	}

	public void addInput(Synapse s)
	{
		inputs.add(s);
		numberOfInputs++;
	}

	private void calculateWeightedSum()
	{
		weightedSum = 0;
		for (Synapse synapse : inputs)
		{
			weightedSum += synapse.getWeight() * synapse.getSourceNeuron().getOutput();
		}
	}

	public void activate()
	{
		calculateWeightedSum();
		output = sigmoid(weightedSum);
	}

	public double getOutput()
	{
		return this.output;
	}

	public void setOutput(double output)
	{
		this.output = output;
	}

	public double getError()
	{
		return error;
	}

	public void setError(double error)
	{
		this.error = error;
	}

	public List<Synapse> getInputs()
	{
		return this.inputs;
	}

	public double[] getWeights()
	{
		double[] weights = new double[inputs.size()];

		int i = 0;
		for (Synapse synapse : inputs)
		{
			weights[i] = synapse.getWeight();
			i++;
		}

		return weights;
	}
	
	/**
	 * Calculates the Sigmoid value from input value and response values.
	 * @param inputValue The input value.
	 * @return The resulting Sigmoid value.
	 */
	private static double sigmoid(double inputValue) {
		return 1.0 / (1.0 + Math.exp(-1.0 * inputValue));
	}
}