package core;

import paramenters.*;
import info.*;

import java.util.ArrayList;

import calc.*;

public class Calculation {
	private Additional addInfo;
	private Conditions conditions;
	private ProductionData production;
	private StellInfo stell;
	private int size;
	//calc intermediate data
	private ArrayList<Double> depth;
	private ArrayList<Double> tubingl;
	private ArrayList<Temperature> temperature;
	private ArrayList<Pressure> pressure;
	private ArrayList<Double> pHactual;
	private ArrayList<Double> fco2;
	
	public Calculation() {
		conditions = new Conditions();
		production = new ProductionData();
		stell =  new StellInfo();
		addInfo  = new Additional();
		size = Vars.TotalSegment;
	}
	
	public Calculation(Conditions conditions, ProductionData production, StellInfo stell, Additional addInfo) {
		this.conditions = conditions;
		this.production = production;
		this.stell = stell;
		this.addInfo = addInfo;
		size = Vars.TotalSegment;
	}
	
	//hitung depth
	public void calcDepth() {
		depth = new ArrayList<Double>();
		for(int i = 0; i < size; i++) {
			if(i == 0) {
				depth.add(new Double(0));
			} else {
				depth.add(depth.get(i-1) + stell.getTubingDepth() / size);
			}
		}
	}
	
	// hitung tubing length
	public void calcTubingLength() {
		tubingl = new ArrayList<Double>();
		for(int i = 0; i < size; i++) {
			tubingl.add(depth.get(i)*3.2808);
		}
	}
	
	// hitung temperature
	public void calcTemp(){
		double scale = (conditions.getTempBottomhole() - conditions.getTempWellhead()) / size;
		double cval;
		cval = conditions.getTempWellhead();
		temperature.add(new Temperature(cval, 'C'));
		for(int i = 1; i < size; i++) {
			cval = temperature.get(i-1).getC() + scale;
			temperature.add(new Temperature(cval, 'C'));
		}
	}
	
	//hitung  tekanan
	public void calcPressure() {
		double scale = (conditions.getPressBottomhole() - conditions.getPressWellhead()) / size;
		double bval = conditions.getPressWellhead();
		pressure.add(new Pressure(bval, "bara"));
		for(int i = 1; i < size; i++) {
			bval = pressure.get(i-1).getBara() + scale;
			pressure.add(new Pressure(bval, "bara"));
		}
	}
	
	public void calcpHactual() {
		for(int i = 0; i < size; i++) {
			pHactual.add(new Double(getpHactual(i)));
		}
	}
	
	
	public void calcFCO2() {
		for(int i = 0; i < size; i++) {
			fco2.add(new Double(getFCO2(i)));
		}
	}
	public double getPCO2(int idx) {
		double press = pressure.get(idx).getBara();
		return conditions.getGasCO2()/100*press;
	}
	
	public double getFCO2(int idx) {
		double press = pressure.get(idx).getBara();
		double temp = temperature.get(idx).getC();
		double pco2 = getPCO2(idx);
		double res = Math.log10(pco2) + (0.0031-1.4/(temp + 273)) * press;
		return Math.pow(10, res);
	}
	
	public double getPH2S(int idx) {
		double press = pressure.get(idx).getBara();
		return conditions.getGasH2S()/100*press;
	}
	
	public double getHCO3(int idx) {
		return conditions.getDisolveBicarbon()/70*(-1/temperature.get(idx).getK()+1/temperature.get(0).getK())+conditions.getDisolveBicarbon()/57471;
	}
	
	public double getpHactual(int idx) {
		double hco3 = getHCO3(idx);
		double tempF = temperature.get(idx).getF();
		double press = pressure.get(idx).getBara();
		return Math.log10((hco3/fco2.get(idx)/14.5037738) + 8.68 + 0.00405*tempF + 0.000000458* Math.pow(tempF,2-0.0000307)*press*14.5);
	}
	
	public double getpHCO2(int idx) {
		double temp = temperature.get(idx).getC();
		double fco2 = getFCO2(idx);
		return 3.71 + 0.00417*temp - 0.5*Math.log10(fco2);
	}
	
	public double getVrNormal(int idx) {
		double temp = temperature.get(idx).getC();
		return Math.pow(10,(4.84-1119/(temp+273)+0.58*Math.log10(fco2.get(idx))-0.34*(pHactual.get(idx)-getpHCO2(idx))));
	}
	
	public double getVrQT(int idx) {
		double temp = temperature.get(idx).getC();
		return Math.pow(10,(5.07-1119/(temp+273)+0.58*Math.log10(fco2.get(idx))-0.34*(pHactual.get(idx)-getpHCO2(idx))));
	}
	
	public double getTFactor(int idx) {
		double tempF = temperature.get(idx).getF();
		return 1-((tempF-32)*5/9+273.15)/647.096;
	}
	
	public double getWaterVP(int idx) {
		double tempF = temperature.get(idx).getF();
		double tfac = getTFactor(idx);
		double res =  0.1450377*22064*Math.exp(647.096/((tempF-32)*5/9+273.15)*(-7.85951783 * tfac + 1.84408259 *
				Math.pow(tfac,1.5) - 11.7866479 *
				Math.pow(tfac,3) + 22.6807411 * Math.pow(tfac,3.5) - 15.9618719 *
				Math.pow(tfac,4) + 1.80122502 * Math.pow(tfac,7.5)));
		return res;
	}
	
	
}
