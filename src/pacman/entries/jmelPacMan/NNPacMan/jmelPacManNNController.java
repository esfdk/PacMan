package pacman.entries.jmelPacMan.NNPacMan;

import java.util.Arrays;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import pacman.controllers.Controller;
import pacman.entries.jmelPacMan.NN.Backpropagator;
import pacman.entries.jmelPacMan.NN.NeuralNetwork;
import pacman.entries.jmelPacMan.NN.Training.TrainingSet;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.util.IO;

public class jmelPacManNNController extends Controller<MOVE>{

	public NeuralNetwork nn;
	private int numberOfInputs = 14;
	private int numberOfOutputs = 5;
	private int numberOfHiddenNodes = 5;
	
	/**
	 * Instantiates a new PacManController using a NeuralNetwork.
	 */
	public jmelPacManNNController()
	{
		nn = NeuralNetwork.createSingleHiddenLayerNeuralNetwork(numberOfInputs, numberOfOutputs, numberOfHiddenNodes);
//		nn.setWeights(loadWeights());
	}
	
	/**
	 * Instantiates a new PacManController using a NeuralNetwork.
	 * @param train Controls if the controller should be trained or not.
	 */
	public static jmelPacManNNController newController(boolean train)
	{
		jmelPacManNNController controller = new jmelPacManNNController();
		
		if(train) controller.trainNetwork();
		
		String data = Arrays.toString(controller.nn.getWeights()); 
		
		IO.saveFile("trainedNN.txt", data, true);
		
		return controller;
	}
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		double[] input = new double[14];
		DataTuple dt = new DataTuple(game, lastMove);

//		input[0] = dt.normalizeLevel(dt.mazeIndex);
//		input[1] = dt.normalizeLevel(dt.currentLevel);
		input[0] = dt.normalizePosition(dt.pacmanPosition);
//		input[3] = dt.pacmanLivesLeft;
//		input[4] = dt.normalizeCurrentScore(dt.currentScore);
//		input[5] = dt.normalizeTotalGameTime(dt.totalGameTime);
//		input[6] = dt.normalizeCurrentLevelTime(dt.currentLevelTime);
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
		input[10] = PacManTrainingData.moveToDouble(dt.blinkyDir);
		input[11] = PacManTrainingData.moveToDouble(dt.inkyDir);
		input[12] = PacManTrainingData.moveToDouble(dt.pinkyDir);
		input[13] = PacManTrainingData.moveToDouble(dt.sueDir);
		
		nn.setInputs(input);
		nn.feedForward();

		double[] output = nn.getOutput();
		double finalOutput = -50000;
		MOVE chosenMove = MOVE.NEUTRAL;
		for (int i = 0; i < output.length; i++)
		{
			if (output[i] > finalOutput)
			{
				finalOutput = output[i];
				switch (i)
				{
				case 0:
					chosenMove = MOVE.UP;
					break;

				case 1:
					chosenMove = MOVE.RIGHT;
					break;

				case 2:
					chosenMove = MOVE.DOWN;
					break;

				case 3:
					chosenMove = MOVE.LEFT;
					break;

				case 4:
					chosenMove = MOVE.NEUTRAL;
					break;
				}
			}
		}
		
//		System.out.println();
//		double[] results = nn.getOutput();
//		for(double d : results)
//			System.out.print(d + " ||| ");
//		System.out.println();
//		System.out.println(chosenMove.toString());
		
		return chosenMove;
	}

	private void trainNetwork()
	{
		TrainingSet ts = getTrainingSet();
		
		Backpropagator b = new Backpropagator(nn);
		b.train(ts);
	}
	
	private double[] loadWeights()
	{
		// TODO: Not yet implemented!
		double[] weights = {};
		return weights;
	}
	
	/**
	 * Loads the training set from the PacMan data.
	 * @return
	 */
	private TrainingSet getTrainingSet()
	{
		DataTuple[] dta = DataSaverLoader.LoadPacManData();
		TrainingSet ts = new TrainingSet();
		for (int i = 0; i < dta.length; i++)
		{
			ts.AddTrainingData(new PacManTrainingData(dta[i], numberOfInputs, numberOfOutputs));
		}
		
		return ts;
	}
}