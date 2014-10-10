package pacman.entries.jmelPacMan.NN;

import java.util.ArrayList;
import java.util.List;

public class NeuronLayer
{
	private List<Neuron> neurons;
	private NeuronLayer previousLayer;
	private NeuronLayer nextLayer;
	private Neuron bias;

	public NeuronLayer()
	{
		neurons = new ArrayList<Neuron>();
		previousLayer = null;
	}

	public NeuronLayer(Neuron bias)
	{
		this();
		this.bias = bias;
		neurons.add(bias);
	}

	public NeuronLayer(NeuronLayer previousLayer)
	{
		this();
		this.previousLayer = previousLayer;
	}

	public NeuronLayer(NeuronLayer previousLayer, Neuron bias)
	{
		this(previousLayer);
		this.bias = bias;
		neurons.add(bias);
	}

	public List<Neuron> getNeurons()
	{
		return this.neurons;
	}

	public void addNeuron(Neuron neuron)
	{
		neurons.add(neuron);

		if (previousLayer != null)
		{
			for (Neuron previousLayerNeuron : previousLayer.getNeurons())
			{
				// initialize with a random weight between -1 and 1
				neuron.addInput(new Synapse(previousLayerNeuron,
						(Math.random() * 1) - 0.5));
			}
		}
	}

	public void addNeuron(Neuron neuron, double[] weights)
	{
		neurons.add(neuron);

		if (previousLayer != null)
		{

			if (previousLayer.getNeurons().size() != weights.length)
			{
				throw new IllegalArgumentException(
						"The number of weights supplied must be equal to the number of neurons in the previous layer");
			}
			else
			{
				List<Neuron> previousLayerNeurons = previousLayer.getNeurons();
				for (int i = 0; i < previousLayerNeurons.size(); i++)
				{
					neuron.addInput(new Synapse(previousLayerNeurons.get(i), weights[i]));
				}
			}
		}
	}

	public void feedForward()
	{
		int biasCount = hasBias() ? 1 : 0;

		for (int i = biasCount; i < neurons.size(); i++)
		{
			neurons.get(i).activate();
		}
	}

	public NeuronLayer getPreviousLayer()
	{
		return previousLayer;
	}

	void setPreviousLayer(NeuronLayer previousLayer)
	{
		this.previousLayer = previousLayer;
	}

	public NeuronLayer getNextLayer()
	{
		return nextLayer;
	}

	void setNextLayer(NeuronLayer nextLayer)
	{
		this.nextLayer = nextLayer;
	}

	public boolean isOutputLayer()
	{
		return nextLayer == null;
	}

	public boolean hasBias()
	{
		return bias != null;
	}
}