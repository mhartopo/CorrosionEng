package calculations;

public class Calc {
	public static double  DensityFromZ(double TempDegF, double PresPsia, double GasZ, double GasGravity ) {
		//Purpose:
		//Calculates density from Z factor with T, P, and gas gravity

		double LbPerCf;
		double mw;  
		mw = GasGravity * 28.9625;
		LbPerCf = PresPsia * mw / (GasZ * 10.7315 * (TempDegF + 459.67));
		return LbPerCf * 0.01601846;
	}
	
	
}
