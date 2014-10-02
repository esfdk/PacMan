package pacman.entries.jmelPacMan.NN;

public class Helper {

	public static double Sigmoid(double inputValue, double response) {
		double result = -inputValue / response;
		result = 1.0 + Math.pow(Math.E, result);
		result = 1.0 / result;
		return result;
	}
	
}
