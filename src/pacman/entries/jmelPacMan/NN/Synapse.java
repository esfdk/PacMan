package pacman.entries.jmelPacMan.NN;

/**
 * A synapse (input weight) for a neuron.
 * 
 * Based on work by vivin, https://github.com/vivin/DigitRecognizingNeuralNetwork/tree/master/src/main/java/net/vivin/neural
 * 
 * @author Jakob Melnyk (jmel)
 */
public class Synapse
{
	/**
	 * The source of the input.
	 */
	private Neuron sourceNeuron;

	/**
	 * The weight of this synapse.
	 */
	private double weight;

	/**
	 * Instantiates a new instance of the Synapse class.
	 * 
	 * @param sourceNeuron
	 *            The neuron that is source of the input.
	 * @param weight
	 *            The weight to initialise this synapse with.
	 */
	public Synapse(Neuron sourceNeuron, double weight)
	{
		this.sourceNeuron = sourceNeuron;
		this.weight = weight;
	}

	/**
	 * Gets the source neuron.
	 * 
	 * @return The source neuron.
	 */
	public Neuron getSourceNeuron()
	{
		return sourceNeuron;
	}

	/**
	 * Gets the weight of this synapse.
	 * 
	 * @return The weight of this synapse.
	 */
	public double getWeight()
	{
		return weight;
	}

	/**
	 * Sets the weight of this synapse.
	 * 
	 * @param weight
	 *            The new weight.
	 */
	public void setWeight(double weight)
	{
		this.weight = weight;
	}
}
