package pacman.entries.jmelPacMan.NN;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {

	private List<NeuronLayer> layers;
	private NeuronLayer input;
	private NeuronLayer output;

	public NeuralNetwork() {
		layers = new ArrayList<NeuronLayer>();
	}

	public void addLayer(NeuronLayer layer) {
		layers.add(layer);

		if (layers.size() == 1) {
			input = layer;
		}

		if (layers.size() > 1) {
			// clear the output flag on the previous output layer, but only if
			// we have more than 1 layer
			NeuronLayer previousLayer = layers.get(layers.size() - 2);
			previousLayer.setNextLayer(layer);
		}
		
		output = layers.get(layers.size() - 1);
	}

	public void setInputs(double[] inputs) {
		if (input != null) {

			int biasCount = input.hasBias() ? 1 : 0;

			if (input.getNeurons().size() - biasCount != inputs.length) {
				throw new IllegalArgumentException(
						"The number of inputs must equal the number of neurons in the input layer");
			}

			else {
				List<Neuron> neurons = input.getNeurons();
				for (int i = biasCount; i < neurons.size(); i++) {
					neurons.get(i).setOutput(inputs[i - biasCount]);
				}
			}
		}
	}

	public double[] getOutput() {
		double[] outputs = new double[output.getNeurons().size()];
		
		int i = 0;
		for (Neuron neuron : output.getNeurons()) {
			outputs[i] = neuron.getOutput();
			i++;
		}

		return outputs;
	}
	
	public void feedForward()
	{
		for (int i = 1; i < layers.size(); i++) {
			NeuronLayer layer = layers.get(i);
			layer.feedForward();
		}
	}

	public List<NeuronLayer> getLayers() {
		return layers;
	}

	public void reset() {
		for (NeuronLayer layer : layers) {
			for (Neuron neuron : layer.getNeurons()) {
				for (Synapse synapse : neuron.getInputs()) {
					synapse.setWeight((Math.random() * 1) - 0.5);
				}
			}
		}
	}

	public double[] getWeights() {

		List<Double> weights = new ArrayList<Double>();

		for (NeuronLayer layer : layers) {

			for (Neuron neuron : layer.getNeurons()) {

				for (Synapse synapse : neuron.getInputs()) {
					weights.add(synapse.getWeight());
				}
			}
		}

		double[] allWeights = new double[weights.size()];

		int i = 0;
		for (Double weight : weights) {
			allWeights[i] = weight;
			i++;
		}

		return allWeights;
	}

	public NeuralNetwork copy() {
		NeuralNetwork copy = new NeuralNetwork();

		NeuronLayer previousLayer = null;
		for (NeuronLayer layer : layers) {

			NeuronLayer layerCopy;

			if (layer.hasBias()) {
				Neuron bias = layer.getNeurons().get(0);
				Neuron biasCopy = new Neuron();
				biasCopy.setOutput(bias.getOutput());
				layerCopy = new NeuronLayer(null, biasCopy);
			}

			else {
				layerCopy = new NeuronLayer();
			}

			layerCopy.setPreviousLayer(previousLayer);

			int biasCount = layerCopy.hasBias() ? 1 : 0;

			for (int i = biasCount; i < layer.getNeurons().size(); i++) {
				Neuron neuron = layer.getNeurons().get(i);

				Neuron neuronCopy = new Neuron();
				neuronCopy.setOutput(neuron.getOutput());
				neuronCopy.setError(neuron.getError());

				if (neuron.getInputs().size() == 0) {
					layerCopy.addNeuron(neuronCopy);
				}

				else {
					double[] weights = neuron.getWeights();
					layerCopy.addNeuron(neuronCopy, weights);
				}
			}

			copy.addLayer(layerCopy);
			previousLayer = layerCopy;
		}

		return copy;
	}

	public void copyWeightsFrom(NeuralNetwork sourceNeuralNetwork) {
		if (layers.size() != sourceNeuralNetwork.layers.size()) {
			throw new IllegalArgumentException(
					"Cannot copy weights. Number of layers do not match ("
							+ sourceNeuralNetwork.layers.size()
							+ " in source versus " + layers.size()
							+ " in destination)");
		}

		int i = 0;
		for (NeuronLayer sourceLayer : sourceNeuralNetwork.layers) {
			NeuronLayer destinationLayer = layers.get(i);

			if (destinationLayer.getNeurons().size() != sourceLayer
					.getNeurons().size()) {
				throw new IllegalArgumentException(
						"Number of neurons do not match in layer " + (i + 1)
								+ "(" + sourceLayer.getNeurons().size()
								+ " in source versus "
								+ destinationLayer.getNeurons().size()
								+ " in destination)");
			}

			int j = 0;
			for (Neuron sourceNeuron : sourceLayer.getNeurons()) {
				Neuron destinationNeuron = destinationLayer.getNeurons().get(j);

				if (destinationNeuron.getInputs().size() != sourceNeuron
						.getInputs().size()) {
					throw new IllegalArgumentException(
							"Number of inputs to neuron " + (j + 1)
									+ " in layer " + (i + 1)
									+ " do not match ("
									+ sourceNeuron.getInputs().size()
									+ " in source versus "
									+ destinationNeuron.getInputs().size()
									+ " in destination)");
				}

				int k = 0;
				for (Synapse sourceSynapse : sourceNeuron.getInputs()) {
					Synapse destinationSynapse = destinationNeuron.getInputs()
							.get(k);

					destinationSynapse.setWeight(sourceSynapse.getWeight());
					k++;
				}

				j++;
			}

			i++;
		}
	}
}
