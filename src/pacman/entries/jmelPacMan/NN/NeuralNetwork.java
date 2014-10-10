package pacman.entries.jmelPacMan.NN;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork
{

	private String name;

	private List<NeuronLayer> layers;
	private NeuronLayer input;
	private NeuronLayer output;

	public NeuralNetwork(String name)
	{
		layers = new ArrayList<NeuronLayer>();
		this.name = name;
	}

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

	public void addLayer(NeuronLayer layer)
	{
		layers.add(layer);

		if (layers.size() == 1)
		{
			input = layer;
		}

		if (layers.size() > 1)
		{
			// clear the output flag on the previous output layer, but only if
			// we have more than 1 layer
			NeuronLayer previousLayer = layers.get(layers.size() - 2);
			previousLayer.setNextLayer(layer);
		}

		output = layers.get(layers.size() - 1);
	}

	public void setInputs(double[] inputs)
	{
		if (input != null)
		{

			int biasCount = input.hasBias() ? 1 : 0;

			if (input.getNeurons().size() - biasCount != inputs.length)
			{
				throw new IllegalArgumentException("The number of inputs must equal the number of neurons in the input layer");
			}

			else
			{
				List<Neuron> neurons = input.getNeurons();
				for (int i = biasCount; i < neurons.size(); i++)
				{
					neurons.get(i).setOutput(inputs[i - biasCount]);
				}
			}
		}
	}

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

	public void feedForward()
	{
		for (int i = 1; i < layers.size(); i++)
		{
			NeuronLayer layer = layers.get(i);
			layer.feedForward();
		}
	}

	public List<NeuronLayer> getLayers()
	{
		return layers;
	}

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

	public String getName()
	{
		return this.name;
	}
}
