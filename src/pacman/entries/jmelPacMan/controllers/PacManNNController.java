package pacman.entries.jmelPacMan.controllers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import pacman.controllers.Controller;
import pacman.entries.jmelPacMan.NN.Backpropagator;
import pacman.entries.jmelPacMan.NN.NeuralNetwork;
import pacman.entries.jmelPacMan.NN.Training.TrainingSet;
import pacman.entries.jmelPacMan.NNPacMan.PacManTrainingData;
import pacman.entries.jmelPacMan.dataRecording.DataSaverLoader;
import pacman.entries.jmelPacMan.dataRecording.DataTuple;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.util.IO;

/**
 * PacMan controller using a Neural Network.
 * 
 * @author Jakob Melnyk (jmel)
 */
public class PacManNNController extends Controller<MOVE>
{
	/**
	 * Controls if weights should be loaded when a controller is initialised.
	 */
	private boolean loadWeights = true;

	/**
	 * Controls if weights should be loaded from an array or from a file.
	 */
	private boolean loadArray = true;

	/**
	 * The file to load weights from.
	 */
	private String fileToLoad = "TrainedNN-2014_10_12_18_25_36_512-5.txt";

	/**
	 * The neural network used by the controller.
	 */
	private NeuralNetwork nn;

	/**
	 * Number of inputs in the neural network.
	 */
	private int numberOfInputs = 2;

	/**
	 * Number of outputs in the neural network.
	 */
	private int numberOfOutputs = 4;

	/**
	 * Number of hidden nodes in the neural network.
	 */
	private int numberOfHiddenNodes = 5;

	/**
	 * Instantiates a new PacManController using a NeuralNetwork.
	 */
	public PacManNNController()
	{
		nn = NeuralNetwork.createSingleHiddenLayerNeuralNetwork(generateNeuralNetworkName(), numberOfInputs, numberOfOutputs,
				numberOfHiddenNodes);
		if (loadWeights)
		{
			nn.setWeights(loadWeights());
		}
	}

	/**
	 * Instantiates a new PacManController using a NeuralNetwork.
	 * 
	 * @param numberOfHiddenNodes
	 *            The number of hidden nodes in the neural network used by the controller.
	 */
	private PacManNNController(int numberOfHiddenNodes)
	{
		this.numberOfHiddenNodes = numberOfHiddenNodes;

		nn = NeuralNetwork.createSingleHiddenLayerNeuralNetwork(generateNeuralNetworkName(), numberOfInputs, numberOfOutputs,
				this.numberOfHiddenNodes);
		if (loadWeights)
		{
			nn.setWeights(loadWeights());
		}
	}

	/**
	 * Generates a name for the neural network using the current time and the number of hidden nodes.
	 * 
	 * @return The name for the neural network.
	 */
	private String generateNeuralNetworkName()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_dd_HH_mm_ss_SSS");
		return "TrainedNN-" + sdf.format(cal.getTime()) + "-" + numberOfHiddenNodes;
	}

	/**
	 * Instantiates a new PacManController using a NeuralNetwork and trains it.
	 */
	public static PacManNNController newController()
	{
		PacManNNController controller = new PacManNNController();

		controller.trainNetwork();

		String data = Arrays.toString(controller.nn.getWeights());

		IO.saveFile(controller.nn.getName() + ".txt", data, false);

		return controller;
	}

	/**
	 * Instantiates a new PacManController using a NeuralNetwork and trains it.
	 * 
	 * @param hiddenNodeNumber
	 *            Number of hidden nodes in the network hidden layer of the network
	 */
	public static PacManNNController newController(int hiddenNodeNumber)
	{
		PacManNNController controller = new PacManNNController(hiddenNodeNumber);

		controller.trainNetwork();

		String data = Arrays.toString(controller.nn.getWeights());

		IO.saveFile(controller.nn.getName() + ".txt", data, false);

		return controller;
	}

	@Override
	public MOVE getMove(Game game, long timeDue)
	{
		double[] input = new double[numberOfInputs];
		DataTuple dt = new DataTuple(game, lastMove);

		PacManTrainingData.fillInputArrayFromDataTuple(input, dt);

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

				default:
					chosenMove = MOVE.NEUTRAL;
					break;
				}
			}
		}

		return chosenMove;
	}

	/**
	 * Trains the neural network.
	 */
	private void trainNetwork()
	{
		TrainingSet ts = getTrainingSet();

		Backpropagator b = new Backpropagator(nn, 1.0, 0.05, 500, 25, 0.001, 0.1);
		b.train(ts);
	}

	/**
	 * Loads weights from an already trained network (either from file or from array.
	 * 
	 * @return The weights to use in the neural network.
	 */
	private double[] loadWeights()
	{
		if (loadArray)
		{
			double[] weights =
			{ -22.326947319947188, -32.692208149219454, 7.926781696708609, 39.44838965941962, -132.72289963979628,
					124.7387360465321, -17.977532882745585, -8.23544211610317, -24.58584617051054, -20.645050977302883,
					-51.35459144902418, 40.822432929262185, 41.657619897275595, -68.1431707304308, 13.083880634732942,
					1.1504420618645466, -13.992558482163414, -26.090779557615928, -4.375601586821584, -13.476540496271861,
					-0.9097559971652787, -0.2850592577831624, 0.9432418202032539, 1.6471543460000617, 5.258368355748813,
					13.98359541953976, -18.492660107390385, 5.091605661282135, 51.00976041998503, -98.97589401611519,
					1.5321161304424824, -84.39082818608102, 162.40839838039162, 28.021045048995436, -34.69757701063767,
					-16.03839007709612, 31.018393502298924, 43.08601901127519, -36.72072829784158, -27.651848069520938,
					-34.44826049862651, 27.842114458078242, 75.51479605872191, -4.38069087018132, -7.811440434138965,
					-168.8847463819836, 68.0567135263223, -13.252959584217237, -15.938909745075684, 0.6052693744386902,
					-3.6270494056631493, 16.152700912790944, -19.767340196396283, 2.3153334774020164, -48.68645036464228,
					69.15208343056598, -31.968774675903827, -94.44752356611195, 14.749047060770048, 93.18041502230089,
					107.61090064261576, 60.952777619981575, -20.023713363760578, 51.4397804441901, -0.4267688370451119,
					4.511422246896552, 49.60681963994369, 34.455543210932284, 41.78942744190147, 3.7739576463095665,
					-0.5879714288842386, 1.7174195242652255, -2.7674486943962866, -2.10276845540951, 1.0387279716288935,
					2.0403739994059413, -0.32346551132723933, -1.9389719532346636, 0.4400819309993993, 0.4439108619284558,
					-0.5246464571996015, 0.11732905908455366, -1.7120954962914807, -0.09750396638845384, 1.3494476946214156,
					1.6444789118890275, -1.0176470398923934, -1.485196109094249, -1.9975625252629976, -0.08409964848495899,
					0.45884506887566245, -0.22284500385365696, 0.5656879371472907, -0.24336340806151005 };
			return weights;
		}

		String s = IO.loadFile(fileToLoad);
		String ss = s.substring(1, s.length() - 2);
		String[] weightStrings = ss.split(", ");
		double[] weights = new double[weightStrings.length];
		for (int i = 0; i < weightStrings.length; i++)
		{
			weights[i] = Double.parseDouble(weightStrings[i]);
		}

		return weights;
	}

	/**
	 * Loads the training set from the PacMan data.
	 * 
	 * @return The loaded training set.
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
