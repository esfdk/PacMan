package pacman.entries.jmelPacMan.NN;

import java.util.ArrayList;
import java.util.List;

/**
 * A neuron in a neural network.
 * 
 * Based on work by vivin, https://github.com/vivin/DigitRecognizingNeuralNetwork/tree/master/src/main/java/net/vivin/neural
 * 
 * @author Jakob Melnyk (jmel)
 */
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

	/**
	 * The amount of error in the node.
	 */
	private double error;

	/**
	 * Instantiate a new instance of the Neuron class.
	 */
	public Neuron()
	{
		inputs = new ArrayList<Synapse>();
		numberOfInputs = 0;
	}

	/**
	 * Adds a new input in the form of a Synapse.
	 * 
	 * @param s
	 *            The synapse.
	 */
	public void addInput(Synapse s)
	{
		inputs.add(s);
		numberOfInputs++;
	}

	/**
	 * Calculates the weighted sum of this neuron.
	 * 
	 * @return The weighted sum.
	 */
	private double calculateWeightedSum()
	{
		double weightedSum = 0;
		for (Synapse synapse : inputs)
		{
			weightedSum += synapse.getWeight() * synapse.getSourceNeuron().getOutput();
		}

		return weightedSum;
	}

	/**
	 * Calculates the output of the neuron.
	 */
	public void activate()
	{
		output = sigmoid(calculateWeightedSum());
	}

	/**
	 * Gets the output of the neuron.
	 * 
	 * @return The output.
	 */
	public double getOutput()
	{
		return this.output;
	}

	/**
	 * Sets the output to a new value.
	 * 
	 * @param output
	 *            The new output.
	 */
	public void setOutput(double output)
	{
		this.output = output;
	}

	/**
	 * Gets the error of the neuron.
	 * 
	 * @return The error.
	 */
	public double getError()
	{
		return error;
	}

	/**
	 * Sets the error of the neuron to a new value.
	 * 
	 * @param error
	 *            The new error value.
	 */
	public void setError(double error)
	{
		this.error = error;
	}

	/**
	 * Gets the list of input synapses for this neuron.
	 * 
	 * @return The list of input synapses.
	 */
	public List<Synapse> getInputs()
	{
		return this.inputs;
	}

	/**
	 * Gets an array of the weights of all the synapses for this neuron.
	 * 
	 * @return An array containing the weights.
	 */
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
	 * Calculates the Sigmoid value from an input value.
	 * 
	 * @param inputValue
	 *            The input value.
	 * @return The resulting Sigmoid value.
	 */
	private static double sigmoid(double inputValue)
	{
		return 1.0 / (1.0 + Math.exp(-1.0 * inputValue));
	}
}