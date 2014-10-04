package pacman.entries.jmelPacMan.jmelNNPacMan.NN;

/**
 * Helper class containing static functions used in the Neural Network.
 * @author Jakob Melnyk
 */
public class Helper {

	/**
	 * Calculates the Sigmoid value from input value and response values.
	 * @param inputValue The input value.
	 * @return The resulting Sigmoid value.
	 */
	public static double Sigmoid(double inputValue) {
		return 1.0 / (1.0 + Math.exp(-1.0 * inputValue));
	}
	
}
