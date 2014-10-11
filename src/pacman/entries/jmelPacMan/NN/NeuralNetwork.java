package pacman.entries.jmelPacMan.NN;

import java.util.ArrayList;
import java.util.List;

/**
 * An artificial neural network.
 * 
 * Based on work by vivin, https://github.com/vivin/DigitRecognizingNeuralNetwork/tree/master/src/main/java/net/vivin/neural
 * 
 * @author Jakob Melnyk (jmel)
 */
public class NeuralNetwork
{
	/**
	 * The name of the neural network.
	 */
	private String name;

	/**
	 * The layers of the neural network.
	 */
	private List<NeuronLayer> layers;

	/**
	 * The input layer of the network.
	 */
	private NeuronLayer input;

	/**
	 * The output layer of the network.
	 */
	private NeuronLayer output;

	/**
	 * Instantiates a new instance of the NeuralNetwork class.
	 * 
	 * @param name
	 *            The name of the network.
	 */
	public NeuralNetwork(String name)
	{
		layers = new ArrayList<NeuronLayer>();
		this.name = name;
	}

	/**
	 * Creates a new neural network with a single hidden layer.
	 * 
	 * @param name
	 *            The name of the neural network.
	 * @param numberOfInputNodes
	 *            The amount of input nodes.
	 * @param numberOfOutputNodes
	 *            The amount of output nodes.
	 * @param numberOfHiddenNodes
	 *            The amount of hidden nodes in the hidden layer.
	 * @return The created neural network.
	 */
	public static NeuralNetwork createSingleHiddenLayerNeuralNetwork(String name, int numberOfInputNodes,
			int numberOfOutputNodes, int numberOfHiddenNodes)
	{
		NeuralNetwork nn = new NeuralNetwork(name);

		Neuron bias = new Neuron();
		bias.setOutput(1.0);

		// Create input layer
		NeuronLayer inputLayer = new NeuronLayer(bias);
		for (int i = 0; i < numberOfInputNodes; i++)
		{
			inputLayer.addNeuron(new Neuron());
		}

		// Create hidden layer
		NeuronLayer hiddenLayer = new NeuronLayer(inputLayer, bias);
		for (int i = 0; i < numberOfHiddenNodes; i++)
		{
			hiddenLayer.addNeuron(new Neuron());
		}

		// Create output layer
		NeuronLayer outputLayer = new NeuronLayer(hiddenLayer);
		for (int i = 0; i < numberOfOutputNodes; i++)
		{
			outputLayer.addNeuron(new Neuron());
		}

		// Add layers to network
		nn.addLayer(inputLayer);
		nn.addLayer(hiddenLayer);
		nn.addLayer(outputLayer);

		return nn;
	}

	/**
	 * Adds a layer to the neural network.
	 * 
	 * @param layer
	 *            The layer to add.
	 */
	public void addLayer(NeuronLayer layer)
	{
		layers.add(layer);

		if (layers.size() == 1)
		{
			input = layer;
		}

		if (layers.size() > 1)
		{
			// if there is more than a single layer in the network,
			// clear the output status of the previous output layer
			NeuronLayer previousLayer = layers.get(layers.size() - 2);
			previousLayer.setNextLayer(layer);
		}

		output = layers.get(layers.size() - 1);
	}

	/**
	 * Sets the weights of the neural network.
	 * 
	 * @param inputs
	 *            The new inputs.
	 */
	public void setInputs(double[] inputs)
	{
		if (input != null)
		{
			int biasCount = input.hasBias() ? 1 : 0;
			List<Neuron> neurons = input.getNeurons();
			for (int i = biasCount; i < neurons.size(); i++)
			{
				neurons.get(i).setOutput(inputs[i - biasCount]);
			}
		}
	}

	/**
	 * Gets the values of the output nodes of the network.
	 * 
	 * @return The output values.
	 */
	public double[] getOutput()
	{
		double[] outputs = new double[output.getNeurons().size()];

		int i = 0;
		for (Neuron neuron : output.getNeurons())
		{
			outputs[i] = neuron.getOutput();
			i++;
		}

		return outputs;
	}

	/**
	 * Makes every layer in the neural network feed forward.
	 */
	public void feedForward()
	{
		for (int i = 1; i < layers.size(); i++)
		{
			NeuronLayer layer = layers.get(i);
			layer.feedForward();
		}
	}

	/**
	 * Gets the layers of the network.
	 * 
	 * @return A list of the layers.
	 */
	public List<NeuronLayer> getLayers()
	{
		return layers;
	}

	/**
	 * Gets the weights of the neural network.
	 * 
	 * @return The weights.
	 */
	public double[] getWeights()
	{
		List<Double> weights = new ArrayList<Double>();

		for (NeuronLayer layer : layers)
		{
			for (Neuron neuron : layer.getNeurons())
			{
				for (Synapse synapse : neuron.getInputs())
				{
					weights.add(synapse.getWeight());
				}
			}
		}

		double[] allWeights = new double[weights.size()];

		int i = 0;
		for (Double weight : weights)
		{
			allWeights[i] = weight;
			i++;
		}

		return allWeights;
	}

	/**
	 * Sets the weights of the synapses in the network.
	 * @param weights
	 *            The new weights.
	 */
	public void setWeights(double[] weights)
	{
		int w = 0;

		for (NeuronLayer layer : layers)
		{

			for (Neuron neuron : layer.getNeurons())
			{

				for (Synapse synapse : neuron.getInputs())
				{
					synapse.setWeight(weights[w]);
					w++;
				}
			}
		}
	}

	/**
	 * Gets the name of the neural network.
	 * 
	 * @return The name of the network.
	 */
	public String getName()
	{
		return this.name;
	}
}
