package pacman.entries.jmelPacMan.NN;

import java.util.List;

import pacman.entries.jmelPacMan.NN.Training.TrainingData;
import pacman.entries.jmelPacMan.NN.Training.TrainingSet;

/**
 * Used for the backpropagation algorithm for neural networks.
 * 
 * @author Jakob Melnyk (jmel)
 */
public class Backpropagator
{
	/**
	 * The neural network to train.
	 */
	private NeuralNetwork neuralNetwork;

	/**
	 * Amount of samples to average over.
	 */
	private int samples = 25;

	/**
	 * The current learning rate of the algorithm.
	 */
	private double learningRate = 0.0;

	/**
	 * The starting learning rate of the algorithm.
	 */
	private double startingLearningRate = 1.0;

	/**
	 * The error threshold for the backpropagation algorithm. Learning stops after average error is below this value.
	 */
	private double errorThreshold = 0.05;

	/**
	 * The maximum number of epochs to train.
	 */
	private int maximumEpochs = 1000;

	/**
	 * The amounts of epochs per learning iteration (how often learning rate is reduced).
	 */
	private int epochsPerIteration = 25;

	/**
	 * The maximum amount of change in a single weight in an epoch.
	 */
	private double maxWeightChange = 0.01;

	/**
	 * The maximum percentage of tuples that can be misclassified in an epoch for it to terminate.
	 */
	private double maximumMisclassificationPercentage = 0.05;

	/**
	 * Instantiates a new instance of the Backpropagator class.
	 * 
	 * @param nn
	 *            The neural network to backpropagate.
	 */
	public Backpropagator(NeuralNetwork nn)
	{
		this.neuralNetwork = nn;
	}

	/**
	 * Instantiates a new instance of the Backpropagator class.
	 * 
	 * @param nn
	 *            The neural network to backpropagate.
	 * @param startingLearningRate
	 *            The starting learning rate.
	 * @param errorThreshold
	 *            The error threshold.
	 * @param maxEpochs
	 *            The maximum amount of epochs.
	 * @param epochsPerIteration
	 *            The amount of epochs per iteration.
	 * @param maxWeightChange
	 *            The maximum change in a single weight in an epoch.
	 * @param maxMisclassificationPercentage
	 *            The percentage of tuples that can be misclassified in an epoch.
	 */
	public Backpropagator(NeuralNetwork nn, double startingLearningRate, double errorThreshold, int maxEpochs,
			int epochsPerIteration, double maxWeightChange, double maxMisclassificationPercentage)
	{
		this(nn);
		this.startingLearningRate = startingLearningRate;
		this.errorThreshold = errorThreshold;
		this.maximumEpochs = maxEpochs;
		this.epochsPerIteration = epochsPerIteration;
		this.maxWeightChange = maxWeightChange;
		this.maximumMisclassificationPercentage = maxMisclassificationPercentage;
	}

	/**
	 * Train the neural network using backpropagation of the specified training set.
	 * 
	 * @param ts
	 *            The training set to train on.
	 */
	public void train(TrainingSet ts)
	{
		// Initialise learning rate.
		learningRate = startingLearningRate;

		// Current epoch.
		int epoch = 1;

		// Current learning iteration.
		int learningIteration = 1;

		// Terminating condition variables.
		TerminatingConditions tc;

		// Average error variables.
		double errorSum = 0.0;
		double epochErrorAverage = (double) samples;
		double[] errors = new double[samples];

		// Do backpropagation until terminating conditions are met.
		do
		{
			// Backpropagate the training data.
			tc = backpropagate(ts);

			// Remove previous sample and add new one.
			errorSum -= (errors[epoch % samples]);
			errors[epoch % samples] = tc.errorSum;
			errorSum += (errors[epoch % samples]);

			// Once enough samples have been collected, start calculating average error in epoch.
			if (epoch > samples)
			{
				epochErrorAverage = errorSum / samples;
			}

			// Increase epoch counter.
			epoch++;

			// Once enough epochs have passed, increase learning rate.
			if (epoch % epochsPerIteration == 0)
			{
				learningIteration++;
				learningRate = startingLearningRate / learningIteration;
			}
		} while (epoch < maximumEpochs // Continue training until maximum epochs have passed
				&& epochErrorAverage > errorThreshold // or average error is less than or equal to error threshold
				&& tc.maxChange > maxWeightChange // or maximum change in weights is less than or equal to threshold
				&& tc.percentMisclassifiedTuples > maximumMisclassificationPercentage); // or percentage of misclassified tuples
																						// is below or equal to threshold
	}

	/**
	 * Backpropagates errors in the neural network, using said errors to change weights.
	 * 
	 * @param ts
	 *            The training set to use for backpropagation.
	 * @return The terminating conditions for this run.
	 */
	private TerminatingConditions backpropagate(TrainingSet ts)
	{
		double error = 0;
		double maxChange = 0;
		double misclassifiedTuples = 0.0;

		for (int i = 0; i <= ts.highestIndexOfSet(); i++)
		{
			TrainingData td = ts.getSpecificData(i);
			boolean misclassified = false;

			// Propagate the inputs forward.
			neuralNetwork.setInputs(td.getInput());
			neuralNetwork.feedForward();

			// Backpropagate the errors.
			List<NeuronLayer> neuronLayers = neuralNetwork.getLayers();
			int numberOfLayers = neuronLayers.size();

			// Calculate errors in output layer.
			NeuronLayer outputLayer = neuronLayers.get(numberOfLayers - 1);
			for (int j = 0; j < outputLayer.getNeurons().size(); j++)
			{
				Neuron n = outputLayer.getNeurons().get(j);
				double output = n.getOutput();

				// Check if output is misclassified.
				if (!misclassified)
				{
					if(Math.abs(n.getOutput() - td.getOutput()[j]) > 0.0001)
					{
						misclassified = true;
					}
				}

				// Set error of the output node.
				double err = output * (1 - output) * (td.getOutput()[j] - output);
				n.setError(err);

				List<Synapse> synapses = n.getInputs();
				int bias = outputLayer.getPreviousLayer().hasBias() ? 1 : 0;

				// Change weight of the synapses connected to the bias, if any.
				if (bias == 1)
				{
					Synapse s = synapses.get(0);

					double deltaWeight = learningRate * n.getError();
					s.setWeight(s.getWeight() + deltaWeight);
				}

				// Change error and weights from previous layer
				for (int sIndex = bias; sIndex < synapses.size(); sIndex++)
				{
					Synapse s = synapses.get(sIndex);
					Neuron sn = s.getSourceNeuron();
					// Increase error of the source neuron.
					double snErrIncrease = s.getWeight() * n.getError();
					double snErr = sn.getError() + snErrIncrease;
					sn.setError(snErr);

					// Change weight based on error of current neuron.
					double deltaWeight = learningRate * n.getError() * sn.getOutput();
					s.setWeight(s.getWeight() + deltaWeight);

					// Keep track of the maximum weight change.
					if (maxChange < Math.abs(deltaWeight))
					{
						maxChange = Math.abs(deltaWeight);
					}
				}
			}

			// Calculate errors in hidden layers
			for (int l = numberOfLayers - 2; l > 0; l--)
			{
				NeuronLayer tempLayer = neuronLayers.get(l);
				int bias = tempLayer.hasBias() ? 1 : 0;
				for (int j = bias; j < tempLayer.getNeurons().size(); j++)
				{
					Neuron n = tempLayer.getNeurons().get(j);
					double output = n.getOutput();

					// Set error of neuron.
					double err = output * (1 - output) * (n.getError());
					n.setError(err);

					List<Synapse> synapses = n.getInputs();
					int previousLayerBias = tempLayer.getPreviousLayer().hasBias() ? 1 : 0;

					// Change weight of bias neuron, if any.
					if (previousLayerBias == 1)
					{
						Synapse s = synapses.get(0);

						double deltaWeight = learningRate * n.getError();
						s.setWeight(s.getWeight() + deltaWeight);
					}

					// Change error and weights from previous layer.
					for (int sIndex = previousLayerBias; sIndex < synapses.size(); sIndex++)
					{
						Synapse s = synapses.get(sIndex);
						Neuron sn = s.getSourceNeuron();

						// Increase error of source neruon.
						double snErrIncrease = s.getWeight() * n.getError();
						double snErr = sn.getError() + snErrIncrease;
						sn.setError(snErr);

						// Change weight based on error of current neuron.
						double deltaWeight = learningRate * n.getError() * sn.getOutput();
						s.setWeight(s.getWeight() + deltaWeight);

						// Keep track of the maximum weight change.
						if (maxChange < Math.abs(deltaWeight))
						{
							maxChange = Math.abs(deltaWeight);
						}
					}
				}
			}

			// Increase error sum.
			double[] output = neuralNetwork.getOutput();
			error += sumError(output, td.getOutput());

			// If tuple was misclassified, increase counter.
			if (misclassified)
			{
				misclassifiedTuples++;
			}
		}

		// Record terminating condition values and return them
		TerminatingConditions tc = new TerminatingConditions();
		tc.errorSum = Math.abs(error);
		tc.percentMisclassifiedTuples = misclassifiedTuples / (((double) ts.highestIndexOfSet() + 1));
		tc.maxChange = maxChange;
		return tc;
	}

	/**
	 * Sums the error of an actual output compared to an expected output.
	 * 
	 * @param actual The actual output.
	 * @param expected The expected output.
	 * @return The error sum.
	 */
	private double sumError(double[] actual, double[] expected)
	{
		double sum = 0;
		for (int i = 0; i < expected.length; i++)
		{
			sum += Math.abs(expected[i] - actual[i]);
		}

		return sum;
	}

	/**
	 * Values for the terminating conditions using in back propagation.
	 * 
	 * @author Jakob Melnyk (jmel)
	 */
	private class TerminatingConditions
	{
		/**
		 * The sum of all errors in an epoch.
		 */
		public double errorSum;

		/**
		 * The percentage of misclassified tuples in an epoch.
		 */
		public double percentMisclassifiedTuples;

		/**
		 * The maximum change in a single weight in an epoch.
		 */
		public double maxChange;
	}
}
