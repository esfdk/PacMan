package pacman.entries.jmelPacMan.NN.Training;

import java.util.ArrayList;

/**
 * A set of data tuples used in training a neural network.
 * 
 * @author Jakob Melnyk (jmel)
 */
public class TrainingSet
{
	/**
	 * The list of data tuples in the training set.
	 */
	private ArrayList<TrainingData> trainingDataList;

	/**
	 * The current index of the iterator.
	 */
	private int currentIndex;

	/**
	 * The amount of items in the training set.
	 */
	private int highestIndex;

	/**
	 * Instantiate a new instance of the TrainingSet class.
	 */
	public TrainingSet()
	{
		trainingDataList = new ArrayList<TrainingData>();
		currentIndex = -1;
		highestIndex = -1;
	}

	/**
	 * Gets the next training tuple in the set.
	 * 
	 * @return The next training tuple.
	 */
	public TrainingData getNextTrainingData()
	{
		if (highestIndex == -1)
		{
			System.out.println("NO TRAINING DATA AVAILABLE!");
			return null;
		}

		currentIndex = currentIndex == highestIndex ? 0 : currentIndex + 1;

		return trainingDataList.get(currentIndex);
	}

	/**
	 * Gets training data from a specific index of the set.
	 * 
	 * @param index
	 *            The index of the data tuple.
	 * @return The specified training data.
	 */
	public TrainingData getSpecificData(int index)
	{
		if (index > highestIndex)
		{
			return null;
		}

		return trainingDataList.get(index);
	}

	/**
	 * Gets the amount of items in the set.
	 * 
	 * @return The highest index of the set.
	 */
	public int highestIndexOfSet()
	{
		return highestIndex;
	}

	/**
	 * Adds a training tuple to the set.
	 * 
	 * @param td
	 *            The training data to add.
	 */
	public void AddTrainingData(TrainingData td)
	{
		highestIndex++;
		trainingDataList.add(td);
	}
}