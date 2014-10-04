package pacman.entries.jmelPacMan.jmelNNPacMan;

import pacman.entries.jmelPacMan.jmelNNPacMan.NN.Training.TrainingData;
import pacman.game.Constants.MOVE;
import dataRecording.DataTuple;

public class PacManTrainingData extends TrainingData
{
	private static final double MIN_VALUE = 0.0;
	private static final double MAX_VALUE = 1.0;

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

	public static void fillInputArrayFromDataTuple(double[] input, DataTuple dt)
	{
		input[0] = dt.normalizeLevel(dt.mazeIndex);
		input[1] = dt.normalizeLevel(dt.currentLevel);
		input[2] = dt.normalizePosition(dt.pacmanPosition);
//		input[3] = dt.pacmanLivesLeft;
		input[3] = dt.normalizeCurrentScore(dt.currentScore);
		input[4] = dt.normalizeTotalGameTime(dt.totalGameTime);
		input[5] = dt.normalizeCurrentLevelTime(dt.currentLevelTime);
		input[6] = dt.normalizeNumberOfPills(dt.numOfPillsLeft);
		input[7] = dt.normalizeNumberOfPowerPills(dt.numOfPowerPillsLeft);
		// Ghosts edible?
		input[8] = dt.normalizeBoolean(dt.isBlinkyEdible);
		input[9] = dt.normalizeBoolean(dt.isInkyEdible);
		input[10] = dt.normalizeBoolean(dt.isPinkyEdible);
		input[11] = dt.normalizeBoolean(dt.isSueEdible);
		// Ghost distance
		input[12] = dt.normalizeDistance(dt.blinkyDist);
		input[13] = dt.normalizeDistance(dt.inkyDist);
		input[14] = dt.normalizeDistance(dt.pinkyDist);
		input[15] = dt.normalizeDistance(dt.sueDist);
		// Ghost direction
		input[16] = moveToDouble(dt.blinkyDir);

		for(int ghostDirectionInputs = 16; ghostDirectionInputs <= 31; ghostDirectionInputs++)
		{
			input[ghostDirectionInputs] = 0.0;
		}

		switch (dt.blinkyDir)
		{
				case UP:
					input[16] = MAX_VALUE;
					break;
				case RIGHT:
					input[17] = MAX_VALUE;
					break;
				case DOWN:
					input[18] = MAX_VALUE;
					break;
				case LEFT:
					input[19] = MAX_VALUE;
					break;
				default:
					break;
		}
		switch (dt.inkyDir)
		{
				case UP:
					input[20] = MAX_VALUE;
					break;
				case RIGHT:
					input[21] = MAX_VALUE;
					break;
				case DOWN:
					input[22] = MAX_VALUE;
					break;
				case LEFT:
					input[23] = MAX_VALUE;
					break;
				default:
					break;
		}switch (dt.pinkyDir)
		{
				case UP:
					input[24] = MAX_VALUE;
					break;
				case RIGHT:
					input[25] = MAX_VALUE;
					break;
				case DOWN:
					input[26] = MAX_VALUE;
					break;
				case LEFT:
					input[27] = MAX_VALUE;
					break;
				default:
					break;
		}switch (dt.sueDir)
		{
				case UP:
					input[28] = MAX_VALUE;
					break;
				case RIGHT:
					input[29] = MAX_VALUE;
					break;
				case DOWN:
					input[30] = MAX_VALUE;
					break;
				case LEFT:
					input[31] = MAX_VALUE;
					break;
				default:
					break;
		}
	}
	
	public static double moveToDouble(MOVE move)
	{
		switch (move)
		{
		case NEUTRAL:
			return 0.00;
		case UP:
			return 0.25;
		case RIGHT:
			return 0.5;
		case DOWN:
			return 0.75;
		case LEFT:
			return 1.0;
		default:
			return -1.0;
		}
	}

	public static MOVE doubleToMove(double d)
	{
		if (d >= -0.13 && d < 0.12)
		{
			return MOVE.NEUTRAL;
		}
		if (d >= 0.12 && d < 0.37)
		{
			return MOVE.UP;
		}
		if (d >= 0.37 && d < 0.62)
		{
			return MOVE.RIGHT;
		}
		if (d >= 0.62 && d < 0.87)
		{
			return MOVE.DOWN;
		}
		if (d >= 0.87 && d < 1.12)
		{
			return MOVE.LEFT;
		}
		return MOVE.NEUTRAL;
	}
}
