package calc;

public class Converter {
	public static double tempCtoF(double tempC) {
		return (double)9/5 * tempC + 32;
	}
	
	public static double tempFtoC(double tempF) {
		return (tempF -32) * 5 / 9;
	}
	
	public static double tempCtoK(double tempC) {
		return tempC + 273;
	}
	
	public static double tempKtoC(double tempK) {
		return tempK - 273;
	}
}
