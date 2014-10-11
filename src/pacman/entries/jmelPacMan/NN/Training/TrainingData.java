package pacman.entries.jmelPacMan.NN.Training;

/**
 * Data tuple for training a neural network.
 * 
 * @author Jakob Melnyk (jmel)
 */
public class TrainingData
{
	/**
	 * The input data of the training tuple.
	 */
	protected double[] input;

	/**
	 * The output data of the training tuple.
	 */
	protected double[] output;

	/**
	 * Instantiates a new instance of the TrainingData class.
	 * 
	 * @param inputNodes
	 *            The number of input nodes in the tuple.
	 * @param outputNodes
	 *            The number of output nodes in the tuple.
	 */
	public TrainingData(int inputNodes, int outputNodes)
	{
		this.input = new double[inputNodes];
		this.output = new double[outputNodes];
	}

	/**
	 * Sets the input and output values of the data tuple.
	 * 
	 * @param input
	 *            Set the input values of the tuple.
	 * @param output
	 *            Set the output values of the tuple.
	 */
	public void setData(double[] input, double[] output)
	{
		this.input = input;
		this.output = output;
	}

	/**
	 * Gets the input values of the tuple.
	 * 
	 * @return The input values.
	 */
	public double[] getInput()
	{
		return input;
	}

	/**
	 * Gets the output values of the tuple.
	 * 
	 * @return The output values.
	 */
	public double[] getOutput()
	{
		return output;
	}
}
