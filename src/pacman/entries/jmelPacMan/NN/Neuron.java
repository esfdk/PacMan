package pacman.entries.jmelPacMan.NN;

import java.util.ArrayList;
import java.util.List;

/**
*/
public class Neuron {

	private final double ACTIVATION_RESPONSE = 1.0;

	/**
	 * The number of inputs the neuron takes.
	 */
	public int numberOfInputs;

	/**
	 * The output of the neuron.
	 */
	private double output;
	/**
	 * The weights of the various inputs.
	 */
	private List<Synapse> inputs;

	private double weightedSum;
	private double error;

	public Neuron() {
		inputs = new ArrayList<Synapse>();
		numberOfInputs = 0;
	}

	public void addInput(Synapse s) {
		inputs.add(s);
		numberOfInputs++;
	}

	private void calculateWeightedSum() {
		weightedSum = 0;
		for (Synapse synapse : inputs) {
			weightedSum += synapse.getWeight()
					* synapse.getSourceNeuron().getOutput();
		}
	}

	public void activate() {
		calculateWeightedSum();
		output = Helper.Sigmoid(weightedSum, ACTIVATION_RESPONSE);
	}

	public double getOutput() {
		return this.output;
	}

	public void setOutput(double output) {
		this.output = output;
	}
	
	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}
	

    public List<Synapse> getInputs() {
        return this.inputs;
    }

    public double[] getWeights() {
        double[] weights = new double[inputs.size()];

        int i = 0;
        for(Synapse synapse : inputs) {
            weights[i] = synapse.getWeight();
            i++;
        }

        return weights;
    }
}