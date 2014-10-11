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
		input[0] = dt.normalizeLevel(dt.mazeIndex);
		input[1] = dt.normalizePosition(dt.pacmanPosition);
		input[2] = dt.normalizeCurrentLevelTime(dt.currentLevelTime);
		input[3] = dt.normalizeNumberOfPills(dt.numOfPillsLeft);
		input[4] = dt.normalizeNumberOfPowerPills(dt.numOfPowerPillsLeft);
		// Ghosts edible?
		input[5] = dt.normalizeBoolean(dt.isBlinkyEdible);
		input[6] = dt.normalizeBoolean(dt.isInkyEdible);
		input[7] = dt.normalizeBoolean(dt.isPinkyEdible);
		input[8] = dt.normalizeBoolean(dt.isSueEdible);
		// Ghost distance
		input[9] = dt.normalizeDistance(dt.blinkyDist);
		input[10] = dt.normalizeDistance(dt.inkyDist);
		input[11] = dt.normalizeDistance(dt.pinkyDist);
		input[12] = dt.normalizeDistance(dt.sueDist);
	}
}
