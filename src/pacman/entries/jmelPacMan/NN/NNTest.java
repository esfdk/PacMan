package pacman.entries.jmelPacMan.NN;

import pacman.entries.jmelPacMan.NN.Training.TrainingData;
import pacman.entries.jmelPacMan.NN.Training.TrainingSet;
import pacman.entries.jmelPacMan.NNPacMan.NNPacMan;

/**
 * Class for testing neural networks. 
 * @author Jakob Melnyk (jmel)
 */
public class NNTest
{
	/**
	 * The neural network to use.
	 */
	public static NeuralNetwork nn;

	/**
	 * Trains a new PacMan controller.
	 * 
	 * @param args
	 *            N/A
	 */
	public static void main(String[] args)
	{
//		pacManNetwork();
		AND();
//		XOR();
	}

	/**
	 * Trains a new controller.
	 */
	@SuppressWarnings("unused")
	private static void pacManNetwork()
	{
		NNPacMan.newController(5);
	}

	/**
	 * Trains the Neural Network to recognise AND.
	 */
	@SuppressWarnings("unused")
	private static void AND()
	{
		nn = createTwoInputOneOutputNeuralNetwork("XOR");

		double[] in1 =
		{ 0, 0 };
		double[] in2 =
		{ 0, 1 };
		double[] in3 =
		{ 1, 0 };
		double[] in4 =
		{ 1, 1 };
		double[] out1 =
		{ 1 };
		double[] out2 =
		{ 0 };

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

		Backpropagator bp = new Backpropagator(nn, 1.0, 0.005, Integer.MAX_VALUE, Integer.MAX_VALUE / 4000, 0.001, 0.05);
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

	/**
	 * Trains the Neural Network to recognise XOR.
	 */
	@SuppressWarnings("unused")
	private static void XOR()
	{
		nn = createTwoInputOneOutputNeuralNetwork("XOR");
		
		double[] in1 =
		{ 0, 0 };
		double[] in2 =
		{ 0, 1 };
		double[] in3 =
		{ 1, 0 };
		double[] in4 =
		{ 1, 1 };
		double[] out1 =
		{ 1 };
		double[] out2 =
		{ 0 };

		TrainingData td1 = new TrainingData(2, 1);
		td1.setData(in1, out2);
		TrainingData td2 = new TrainingData(2, 1);
		td2.setData(in2, out1);
		TrainingData td3 = new TrainingData(2, 1);
		td3.setData(in3, out1);
		TrainingData td4 = new TrainingData(2, 1);
		td4.setData(in4, out2);

		TrainingSet ts = new TrainingSet();
		ts.AddTrainingData(td1);
		ts.AddTrainingData(td2);
		ts.AddTrainingData(td3);
		ts.AddTrainingData(td4);

		Backpropagator bp = new Backpropagator(nn, 1.0, 0.005, Integer.MAX_VALUE, Integer.MAX_VALUE / 4000, 0.001, 0.05);
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
	
	/**
	 * Creates a neural network with 2 inputs, 2 hidden nodes and 1 output.
	 * @param name The name of the neural network.
	 * @return The newly created neural network.
	 */
	private static NeuralNetwork createTwoInputOneOutputNeuralNetwork(String name)
	{
		NeuralNetwork neuralNetwork = new NeuralNetwork(name);

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
		neuralNetwork.addLayer(inputLayer);
		neuralNetwork.addLayer(h1Layer);
		neuralNetwork.addLayer(outputLayer);
		neuralNetwork.feedForward();
		
		return neuralNetwork;
	}
}
