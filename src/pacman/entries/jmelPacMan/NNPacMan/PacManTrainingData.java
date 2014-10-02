package pacman.entries.jmelPacMan.NNPacMan;

import pacman.entries.jmelPacMan.NN.Training.TrainingData;
import pacman.game.Constants.MOVE;
import dataRecording.DataTuple;

public class PacManTrainingData extends TrainingData {
	
	private static final double MIN_VALUE = 0.05;
	private static final double MAX_VALUE = 0.95;
	
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
		
		// input[0] = dt.normalizeLevel(dt.mazeIndex);
		// input[1] = dt.normalizeLevel(dt.currentLevel);
		input[0] = dt.normalizePosition(dt.pacmanPosition);
		// input[3] = dt.pacmanLivesLeft;
		// input[4] = dt.normalizeCurrentScore(dt.currentScore);
		// input[5] = dt.normalizeTotalGameTime(dt.totalGameTime);
		// input[6] = dt.normalizeCurrentLevelTime(dt.currentLevelTime);
		input[1] = dt.normalizeNumberOfPills(dt.numOfPillsLeft);
		input[2] = dt.normalizeNumberOfPowerPills(dt.numOfPowerPillsLeft);
		// Ghosts edible?
		input[3] = dt.normalizeBoolean(dt.isBlinkyEdible);
		input[4] = dt.normalizeBoolean(dt.isInkyEdible);
		input[5] = dt.normalizeBoolean(dt.isPinkyEdible);
		input[6] = dt.normalizeBoolean(dt.isSueEdible);
		// Ghost distance
		input[7] = dt.normalizeDistance(dt.blinkyDist);
		input[8] = dt.normalizeDistance(dt.inkyDist);
		input[9] = dt.normalizeDistance(dt.pinkyDist);
		input[10] = dt.normalizeDistance(dt.sueDist);
		// Ghost direction
		input[10] = moveToDouble(dt.blinkyDir);
		input[11] = moveToDouble(dt.inkyDir);
		input[12] = moveToDouble(dt.pinkyDir);
		input[13] = moveToDouble(dt.sueDir);
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
