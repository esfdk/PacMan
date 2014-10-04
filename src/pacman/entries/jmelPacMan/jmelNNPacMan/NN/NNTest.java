package pacman.entries.jmelPacMan.jmelNNPacMan.NN;

import pacman.entries.jmelPacMan.jmelNNPacMan.jmelPacManNNController;
import pacman.entries.jmelPacMan.jmelNNPacMan.NN.Training.TrainingData;
import pacman.entries.jmelPacMan.jmelNNPacMan.NN.Training.TrainingSet;

public class NNTest
{
	public static NeuralNetwork nn;

	public static void main(String[] args)
	{
//		System.out.println("=====");
//		AND();
//		System.out.println("=====");
//		XOR();
//		System.out.println("=====");
		
		for(int i = 5; i <= 5; i++)
		{
			for(int j = 0; j < 1; j++)
			{
				jmelPacManNNController.newController(i);
			}
		}
	}

	@SuppressWarnings("unused")
	private static void AND()
	{
		nn = new NeuralNetwork("AND");
		Neuron bias = new Neuron();
		bias.setOutput(1.0);
		Neuron i1 = new Neuron();
		Neuron i2 = new Neuron();
		Neuron o1 = new Neuron();
		NeuronLayer inputLayer = new NeuronLayer(bias);
		inputLayer.addNeuron(i1);
		inputLayer.addNeuron(i2);
		NeuronLayer outputLayer = new NeuronLayer(inputLayer);
		outputLayer.addNeuron(o1);
		nn.addLayer(inputLayer);
		nn.addLayer(outputLayer);
		nn.feedForward();
		double[] in1 = { 0, 0 };
		double[] in2 = { 0, 1 };
		double[] in3 = { 1, 0 };
		double[] in4 = { 1, 1 };
		double[] out1 = { 1 };
		double[] out2 = { 0 };
		TrainingSet ts = new TrainingSet();
		TrainingData td1 = new TrainingData(2, 1);
		td1.setData(in1, out2);
		TrainingData td2 = new TrainingData(2, 1);
		td2.setData(in2, out2);
		TrainingData td3 = new TrainingData(2, 1);
		td3.setData(in3, out2);
		TrainingData td4 = new TrainingData(2, 1);
		td4.setData(in4, out1);
		ts.AddTrainingData(td1);
		ts.AddTrainingData(td2);
		ts.AddTrainingData(td3);
		ts.AddTrainingData(td4);
		Backpropagator bp = new Backpropagator(nn);
		bp.train(ts);
		nn.setInputs(in1);
		nn.feedForward();
		double result = nn.getOutput()[0];
		System.out.println("AND (0, 0) = " + result);
		nn.setInputs(in2);
		nn.feedForward();
		result = nn.getOutput()[0];
		System.out.println("AND (1, 0) = " + result);
		nn.setInputs(in3);
		nn.feedForward();
		result = nn.getOutput()[0];
		System.out.println("AND (0, 1) = " + result);
		nn.setInputs(in4);
		nn.feedForward();
		result = nn.getOutput()[0];
		System.out.println("AND (1, 1) = " + result);
	}

	@SuppressWarnings("unused")
	private static void XOR()
	{
		nn = new NeuralNetwork("XOR");
		Neuron bias = new Neuron();
		bias.setOutput(1.0);
		Neuron i1 = new Neuron();
		Neuron i2 = new Neuron();
		Neuron h1 = new Neuron();
		Neuron h2 = new Neuron();
		Neuron o1 = new Neuron();
		NeuronLayer inputLayer = new NeuronLayer(bias);
		inputLayer.addNeuron(i1);
		inputLayer.addNeuron(i2);
		NeuronLayer h1Layer = new NeuronLayer(inputLayer, bias);
		h1Layer.addNeuron(h1);
		h1Layer.addNeuron(h2);
		NeuronLayer outputLayer = new NeuronLayer(h1Layer);
		outputLayer.addNeuron(o1);
		nn.addLayer(inputLayer);
		nn.addLayer(h1Layer);
		nn.addLayer(outputLayer);
		nn.feedForward();
		double[] in1 = { 0, 0 };
		double[] in2 = { 0, 1 };
		double[] in3 = { 1, 0 };
		double[] in4 = { 1, 1 };
		double[] out1 = { 1 };
		double[] out2 =	{ 0 };
		TrainingSet ts = new TrainingSet();
		TrainingData td1 = new TrainingData(2, 1);
		td1.setData(in1, out2);
		TrainingData td2 = new TrainingData(2, 1);
		td2.setData(in2, out1);
		TrainingData td3 = new TrainingData(2, 1);
		td3.setData(in3, out1);
		TrainingData td4 = new TrainingData(2, 1);
		td4.setData(in4, out2);
		ts.AddTrainingData(td1);
		ts.AddTrainingData(td2);
		ts.AddTrainingData(td3);
		ts.AddTrainingData(td4);
		Backpropagator bp = new Backpropagator(nn);
		bp.train(ts);
		nn.setInputs(in1);
		nn.feedForward();
		double result = nn.getOutput()[0];
		System.out.println("XOR (0, 0) = " + result);
		nn.setInputs(in2);
		nn.feedForward();
		result = nn.getOutput()[0];
		System.out.println("XOR (1, 0) = " + result);
		nn.setInputs(in3);
		nn.feedForward();
		result = nn.getOutput()[0];
		System.out.println("XOR (0, 1) = " + result);
		nn.setInputs(in4);
		nn.feedForward();
		result = nn.getOutput()[0];
		System.out.println("XOR (1, 1) = " + result);
	}
}
