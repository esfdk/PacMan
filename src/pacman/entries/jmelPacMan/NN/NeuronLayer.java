package pacman.entries.jmelPacMan.NN;

import java.util.ArrayList;
import java.util.List;

/**
 * A layer of neurons in a neural network.
 * 
 * Based on work by vivin, https://github.com/vivin/DigitRecognizingNeuralNetwork/tree/master/src/main/java/net/vivin/neural
 * 
 * @author Jakob Melnyk (jmel)
 */
public class NeuronLayer
{
	/**
	 * List of neurons in the layer.
	 */
	private List<Neuron> neurons;

	/**
	 * The layer prior to this one in the neural network.
	 */
	private NeuronLayer previousLayer;

	/**
	 * The layer after this in the neural network.
	 */
	private NeuronLayer nextLayer;

	/**
	 * The bias node of the layer.
	 */
	private Neuron bias;

	/**
	 * Instantiates a new instance of the NeuronLayer class.
	 */
	public NeuronLayer()
	{
		neurons = new ArrayList<Neuron>();
		previousLayer = null;
	}

	/**
	 * Instantiates a new instance of the NeuronLayer class.
	 * 
	 * @param bias
	 *            The bias to include in the layer.
	 */
	public NeuronLayer(Neuron bias)
	{
		this();
		this.bias = bias;
		neurons.add(bias);
	}

	/**
	 * Instantiates a new instance of the NeuronLayer class.
	 * 
	 * @param previousLayer
	 *            The layer prior to this one in the neural network.
	 */
	public NeuronLayer(NeuronLayer previousLayer)
	{
		this();
		this.previousLayer = previousLayer;
	}

	/**
	 * Instantiates a new instance of the NeuronLayer class.
	 * 
	 * @param previousLayer
	 *            The layer prior to this one in the neural network.
	 * @param bias
	 *            The bias to include in the layer.
	 */
	public NeuronLayer(NeuronLayer previousLayer, Neuron bias)
	{
		this(previousLayer);
		this.bias = bias;
		neurons.add(bias);
	}

	/**
	 * Gets the list of neurons in the layer.
	 * 
	 * @return The list of neurons in the layer.
	 */
	public List<Neuron> getNeurons()
	{
		return this.neurons;
	}

	/**
	 * Adds a neuron to the layer.
	 * 
	 * @param neuron
	 *            The neuron to add to the layer.
	 */
	public void addNeuron(Neuron neuron)
	{
		neurons.add(neuron);

		if (previousLayer != null)
		{
			for (Neuron previousLayerNeuron : previousLayer.getNeurons())
			{
				// initialise weights with a random weight between -0.5 and 0.5
				neuron.addInput(new Synapse(previousLayerNeuron, (Math.random() * 1) - 0.5));
			}
		}
	}

	/**
	 * Adds a neuron to the layer using the specified weights.
	 * 
	 * @param neuron
	 *            The neuron to add to the layer.
	 * @param weights
	 *            The weights used as input to the added neuron.
	 */
	public void addNeuron(Neuron neuron, double[] weights)
	{
		neurons.add(neuron);

		if (previousLayer != null)
		{
			List<Neuron> previousLayerNeurons = previousLayer.getNeurons();
			for (int i = 0; i < previousLayerNeurons.size(); i++)
			{
				neuron.addInput(new Synapse(previousLayerNeurons.get(i), weights[i]));
			}
		}
	}

	/**
	 * Activates each non-bias neuron in the layer.
	 */
	public void feedForward()
	{
		int biasCount = hasBias() ? 1 : 0;

		for (int i = biasCount; i < neurons.size(); i++)
		{
			neurons.get(i).activate();
		}
	}

	/**
	 * Gets the layer prior to this one.
	 * 
	 * @return The previous layer.
	 */
	public NeuronLayer getPreviousLayer()
	{
		return previousLayer;
	}

	/**
	 * Sets the next layer in the network.
	 * 
	 * @param nextLayer The layer to set as the next layer.
	 */
	void setNextLayer(NeuronLayer nextLayer)
	{
		this.nextLayer = nextLayer;
	}

	/**
	 * Checks if this layer is an output layer. 
	 * 
	 * @return If this layer does not have a next layer, then return true. Else, return false.
	 */
	public boolean isOutputLayer()
	{
		return nextLayer == null;
	}

	/**
	 * Checks if their layer has a bias or not.
	 * 
	 * @return If this layer contains a bias node.
	 */
	public boolean hasBias()
	{
		return bias != null;
	}
}