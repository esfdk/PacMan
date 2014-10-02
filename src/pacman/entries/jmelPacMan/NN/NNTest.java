package pacman.entries.jmelPacMan.NN;

import pacman.entries.jmelPacMan.NN.Training.TrainingData;
import pacman.entries.jmelPacMan.NN.Training.TrainingSet;

public class NNTest
{
	public static NeuralNetwork nn;
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		classificationByBackpropagationTest();
	}

	/**
	 * Runs the test from "Classification by Backpropagation" 
	 * Example 9.1 in section 9.2.3.
	 */
	private static void classificationByBackpropagationTest()
	{
		// Initialize nn
		nn = new NeuralNetwork();
		
		Neuron bias = new Neuron();
		bias.setOutput(1.0);

		// Input neurons
		Neuron x1 = new Neuron();
		x1.setOutput(1.0);
		
		Neuron x2 = new Neuron();
		x2.setOutput(0.0);
		
		Neuron x3 = new Neuron();
		x3.setOutput(1.0);
		
		// Input layer
		NeuronLayer inputLayer = new NeuronLayer(bias);
		inputLayer.addNeuron(x1);
		inputLayer.addNeuron(x2);
		inputLayer.addNeuron(x3);
		
		// Hidden neurons
		Neuron h1 = new Neuron();
		double[] h1weights = {-0.4, 0.2, 0.4, -0.5};
		
		Neuron h2 = new Neuron();
		double[] h2weights = {0.2, -0.3, 0.1, 0.2};
		
		// Hidden layer
		NeuronLayer hLayer = new NeuronLayer(inputLayer, bias);
		hLayer.addNeuron(h1, h1weights);
		hLayer.addNeuron(h2, h2weights);
		
		// Output neuron
		Neuron o1 = new Neuron();
		double[] o1weights = {0.1, -0.3, -0.2};
		
		// Output layer
		NeuronLayer outputLayer = new NeuronLayer (hLayer);
		outputLayer.addNeuron(o1, o1weights);
		
		nn.addLayer(inputLayer);
		nn.addLayer(hLayer);
		nn.addLayer(outputLayer);
		
		nn.feedForward();
		
		TrainingSet ts = new TrainingSet();
		TrainingData td = new TrainingData(3, 1);
		double[] in = {1, 0, 1};
		double[] out = {1};
		td.setData(in, out);
		ts.AddTrainingData(td);
		
		Backpropagator b = new Backpropagator(nn, 0.9, ts, 0);
	}
}
