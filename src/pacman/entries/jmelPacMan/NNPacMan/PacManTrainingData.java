package pacman.entries.jmelPacMan.NNPacMan;

import pacman.entries.jmelPacMan.NN.Training.TrainingData;
import pacman.entries.jmelPacMan.dataRecording.DataTuple;

/**
 * Training data specific to the PacMan neural network controller.
 * 
 * @author Jakob Melnyk (jmel)
 */
public class PacManTrainingData extends TrainingData
{
	/**
	 * The minimum value of a variable.
	 */
	private static final double MIN_VALUE = 0.0;

	/**
	 * The maximum value of a variable.
	 */
	private static final double MAX_VALUE = 1.0;

	/**
	 * Instantiates a new instance of the PacManTrainingData class.
	 * 
	 * @param dt The data tuple used to instantiate the PacManTrainingData.
	 * @param inputNodes The number of input nodes in the 
	 * @param outputNodes
	 */
	public PacManTrainingData(DataTuple dt, int inputNodes, int outputNodes)
	{
		super(inputNodes, outputNodes);
		for (int i = 0; i < outputNodes; i++)
		{
			output[i] = MIN_VALUE;
		}
		switch (dt.DirectionChosen)
		{
		case UP:
			output[0] = MAX_VALUE;
			break;
		case RIGHT:
			output[1] = MAX_VALUE;
			break;
		case DOWN:
			output[2] = MAX_VALUE;
			break;
		case LEFT:
			output[3] = MAX_VALUE;
			break;
		default:
			output[4] = MAX_VALUE;
			break;
		}

		fillInputArrayFromDataTuple(input, dt);
	}

	/**
	 * Fills an array with data from a data tuple.
	 * 
	 * @param input
	 *            The array to fill.
	 * @param dt
	 *            The data to extract data from.
	 */
	public static void fillInputArrayFromDataTuple(double[] input, DataTuple dt)
	{
		input[0] = dt.normalizePosition(dt.pacmanPosition);
		input[1] = dt.normalizeCurrentLevelTime(dt.currentLevelTime);
	}
}
