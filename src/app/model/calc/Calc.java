package app.model.calc;

import java.util.ArrayList;

import app.model.info.Vars;

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
	
	public static ErBar ErbarPCTC(double GasGrav, double PercentN2, double PercentCO2, double PercentH2S) {
		// Purpose:
		// Estimates critical pressure and temperature using Erbar's correlations.
		double hyh;
		double hyo;
		double awa;
		double cor;
		double ErbarPC = 677.979 - 0.747287 * GasGrav - 22.29407 * GasGrav * GasGrav;
		double ErbarTC = 159.652 + 328.326 * GasGrav + 1.89394 * GasGrav * GasGrav;
		// apply Turek corrections to estimated mixture pseudocriticals
		
		ErbarPC = ErbarPC - 163.056 * PercentN2 / 100 + 411.9195 * PercentCO2 / 100 + 658.3781 * PercentH2S / 100;
		ErbarTC = ErbarTC - 250.354 * PercentN2 / 100 - 113.568 * PercentCO2 / 100 + 125.3394 * PercentH2S / 100;
		
		
		// calculate Wichert-Aziz adjustment to Tc and Pc
		hyh = PercentCO2 / 100;
		hyo = PercentH2S / 100;
		awa = hyh + hyo;
		if (awa > 0) {
			cor = 120 * (Math.pow(awa, 0.9) - Math.pow(awa, 1.6)) + 15 * (Math.sqrt(hyo) - Math.pow(hyo, 4));
			ErbarPC = ErbarPC * (ErbarTC - cor) / (ErbarTC + hyo * (1 - hyo) * cor);
			ErbarTC = ErbarTC - cor;
		}
		
		return new ErBar(ErbarPC,ErbarTC);
	}
	
	public static double HallYarbGasZG(double ResTempF , double psia , double GasGrav , 
			double PercentN2 , double PercentCO2 , double PercentH2S ){
		// Purpose:
		//   Calculates gas Z factor based on gas gravity using the
		// Hall-Yarborough correlation 
		
		int jmax = Vars.TotalSegment;
		double eftol  = 0.000001;
		double eylo  = 0.000001;
		double eyhi  = 1 - eylo;
		
		ErBar erbar = ErbarPCTC(GasGrav, PercentN2, PercentCO2, PercentH2S);
		double ErbarPC = erbar.getPC(); 
		double ErbarTC = erbar.getTC();
		double rrt = ErbarTC / (ResTempF + 459.67);
		
		double ea = 0.06125 * rrt * Math.exp(-1.2 * (1 - rrt) * (1 - rrt));
		double eb = rrt * (14.76 - 9.76 * rrt + 4.58 * rrt * rrt);
		double ec = rrt * (90.7 - 242.2 * rrt + 42.4 * rrt * rrt);
		double ed = 1.18 + 2.82 * rrt;
		double rp = psia / ErbarPC;
		double ey = ea * rp;
		double ef;
		
		double res = 0;
		int j = 1;
		while (j <= jmax) {
			if (ey < eylo) ey = eylo;
			if (ey > eyhi) ey = eyhi;
			
			ef = -ea * rp / ey + (1 + ey + ey * ey - (Math.pow(ey, 3))) / ( Math.pow((1 - ey), 3)) - eb * ey + ec * (Math.pow(ey, ed));
			
			if(Math.abs(ef) < eftol) {
				break;
			}
			
			double dfdy = ea * rp / (ey * ey)
			+ (4 + 4 * ey - 2 * ey * ey)
			/ (Math.pow((1 - ey), 4)) - eb + ed * ec * (Math.pow(ey , (ed - 1)));
			ey = ey - ef / dfdy;
			j++;
		}
		
		if (j <= jmax) {
			res = ea * rp / ey;
		} else {
			res = 0;
		}
		return res;
	} 
	
	public double average(ArrayList<Double> elements) {
		double sum = 0;
		for(int i  = 0; i < elements.size(); i++) {
			sum += elements.get(i);
		}
		return sum/elements.size();
	}
	
	public static double sum(ArrayList<Double> elements) {
		double sum = 0;
		for(int i  = 0; i < elements.size(); i++) {
			sum += elements.get(i);
		}
		return sum;
	}
	
}
