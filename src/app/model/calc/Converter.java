package app.model.calc;

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
	
	public static double bara2Psia(double bara) {
		return bara * 14.5037743897283;
	}
	
	public static double psia2Bara(double psia) {
		return psia / 14.5037743897283;
	}
}
