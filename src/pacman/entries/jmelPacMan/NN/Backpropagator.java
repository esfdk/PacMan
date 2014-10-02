package pacman.entries.jmelPacMan.NN;

import java.util.List;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import pacman.entries.jmelPacMan.NN.Training.TrainingData;
import pacman.entries.jmelPacMan.NN.Training.TrainingSet;
import pacman.entries.jmelPacMan.NNPacMan.PacManTrainingData;

public class Backpropagator {

	private NeuralNetwork neuralNetwork;
	private double learningRate;
	private double currentEpoch;	
	
	public static void main(String[] args)
	{
		DataTuple[] dta = DataSaverLoader.LoadPacManData();
		TrainingSet ts = new TrainingSet();
		for(int i = 0; i < dta.length; i++)
		{
			ts.AddTrainingData(new PacManTrainingData(dta[0], 21, 5));
		}
	}
	
	public Backpropagator(NeuralNetwork nn, double startingLearningRate, TrainingSet ts, double errorThreshold)
	{
		boolean terminatingCondition = true;
		this.neuralNetwork = nn;
		learningRate = startingLearningRate;
		
		do
		{
			for(int i = 0; i <= ts.highestIndexOfSet(); i++)
			{
				TrainingData td = ts.getSpecificData(i);
				// Propagate the inputs forward
				neuralNetwork.setInputs(td.getInput());
				neuralNetwork.feedForward();
				// Backpropagate the errors
				List<NeuronLayer> neuronLayers = neuralNetwork.getLayers();
				int numberOfLayers = neuronLayers.size();
				
				// Calculate errors in output layer
				NeuronLayer outputLayer = neuronLayers.get(numberOfLayers - 1);
				for(int j = 0; j < outputLayer.getNeurons().size(); j++)
				{
					Neuron n = outputLayer.getNeurons().get(j);
					double output = n.getOutput();
					double err = output * (1 - output) * (td.getOutput()[j] - output);
					n.setError(err);
					
					List<Synapse> synapses = n.getInputs();
					int bias = outputLayer.getPreviousLayer().hasBias() ? 1 : 0;
					for(int sIndex = bias; sIndex < synapses.size(); sIndex++)
					{
						Synapse s = synapses.get(sIndex);
						Neuron sn = s.getSourceNeuron();
						double snErrIncrease = s.getWeight() * n.getError();
						double snErr = sn.getError() + snErrIncrease;
						sn.setError(snErr);
						
						double deltaWeight = learningRate * n.getError() * sn.getOutput();
						s.setWeight(s.getWeight() + deltaWeight);
					}
					
					if(bias == 1)
					{
						Synapse s = synapses.get(0);
						
						double deltaWeight = learningRate * n.getError();
						s.setWeight(s.getWeight() + deltaWeight);
					}
				}
				
				// Calculate errors in hidden layers
				for(int l = numberOfLayers - 2; l > 0; l--)
				{
					NeuronLayer tempLayer = neuronLayers.get(l);
					int bias = tempLayer.hasBias() ? 1 : 0;
					for(int j = bias; j < tempLayer.getNeurons().size(); j++)
					{
						Neuron n = tempLayer.getNeurons().get(j);
						double output = n.getOutput();
						
						double err = output * (1 - output) * (n.getError());
						n.setError(err);
						
						List<Synapse> synapses = n.getInputs();
						int previousLayerBias = tempLayer.getPreviousLayer().hasBias() ? 1 : 0;
						for(int sIndex = previousLayerBias; sIndex < synapses.size(); sIndex++)
						{
							Synapse s = synapses.get(sIndex);
							Neuron sn = s.getSourceNeuron();
							double snErrIncrease = s.getWeight() * n.getError();
							double snErr = sn.getError() + snErrIncrease;
							sn.setError(snErr);
							
							double deltaWeight = learningRate * n.getError() * sn.getOutput();
							s.setWeight(s.getWeight() + deltaWeight);
						}
						
						if(previousLayerBias == 1)
						{
							Synapse s = synapses.get(0);
							
							double deltaWeight = learningRate * n.getError();
							s.setWeight(s.getWeight() + deltaWeight);
							System.out.println(s.getWeight());
						}
					}
				}
			}
		} while (!terminatingCondition);
	}
}
