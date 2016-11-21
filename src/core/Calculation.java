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
	private Angle angle;
	private int size;
	//temporary output
	private double gasFlowrate;
	private double wbreak;
	private double innerd;
	private double innerPipeArea;
	private double liqFlowRate;
	private double Zfactor;
	private double contInhibitEff;
	private double seqInhibitEff;
	private double fcr;
	private double fc;
	//end temporary
	
	
	//calc intermediate data
	private ArrayList<Double> depth;
	private ArrayList<Double> tubingl;
	private ArrayList<Temperature> temperature;
	private ArrayList<Pressure> pressure;
	private ArrayList<Double> pHactual;
	private ArrayList<Double> fco2;
	private ArrayList<Double> waterRate;
	private ArrayList<Double> waterCut;
	private ArrayList<Double> vcorNorm;
	private ArrayList<Double> vcorQT;
	private ArrayList<Double> vcorCorrectedNorm;
	private ArrayList<Double> vcorCorrectedQT;
	
	public Calculation() {
		conditions = new Conditions();
		production = new ProductionData();
		stell =  new StellInfo();
		addInfo  = new Additional();
		size = Vars.TotalSegment;
		angle = new Angle(size);
		calcTemporary();
	}
	
	public Calculation(Conditions conditions, ProductionData production, StellInfo stell, Additional addInfo, Angle angle) {
		this.conditions = conditions;
		this.production = production;
		this.stell = stell;
		this.addInfo = addInfo;
		size = Vars.TotalSegment;
		this.angle = angle;
		calcTemporary();
	}
	
	//cacl temporary out
	
	public void calcTemporary() {
		gasFlowrate = 35.31*production.getGasFlowrate();
		wbreak = (-0.0166*production.getAPIGravity()+0.83)*100;
		innerd = stell.getTubingOutDiameter()-2*stell.getTubingThick()/1000;
		innerPipeArea = Math.PI*innerd*innerd/4;
		liqFlowRate = production.getWaterFlowrate() + production.getGasFlowrate();
		Zfactor = 0.988831629795888;
		contInhibitEff = (addInfo.getInhibitorAvail()/100)*(addInfo.getInhibitorEff()/100)*100;
		seqInhibitEff = 0;
		
		if(stell.getChromium() == 0) {
			fcr = 0;
		} else {
			fcr = 1.6337*stell.getChromium()+0.9711;
		}
		
		if(stell.getCarbon() == 0) {
			fc = 0;
		} else {
			fc = 2.4872*stell.getCarbon()+0.8211;
		}
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
		temperature = new ArrayList<Temperature>();
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
		pressure = new ArrayList<Pressure>();
		double scale = (conditions.getPressBottomhole() - conditions.getPressWellhead()) / size;
		double bval = conditions.getPressWellhead();
		pressure.add(new Pressure(bval, "bara"));
		for(int i = 1; i < size; i++) {
			bval = pressure.get(i-1).getBara() + scale;
			pressure.add(new Pressure(bval, "bara"));
		}
	}
	
	public void calcpHactual() {
		pHactual = new ArrayList<Double>();
		for(int i = 0; i < size; i++) {
			pHactual.add(new Double(getpHactual(i)));
		}
	}
	
	
	public void calcFCO2() {
		fco2 = new ArrayList<Double>();
		for(int i = 0; i < size; i++) {
			fco2.add(new Double(getFCO2(i)));
		}
	}
	
	public void calcWaterRate() {
		waterRate = new ArrayList<Double>();
		Double wrate =  new Double(production.getWaterFlowrate());
		waterRate.add(wrate);
		for(int i = 1; i < size ; i++) {
			double dM3d = getM3PerD(i) - getM3PerD(i-1);
			if ((waterRate.get(i-1) - (dM3d)) < 0) {
				wrate = 0.001;
			} else {
				wrate = dM3d;
			}
			waterRate.add(wrate);
		}
	}
	
	public void calcWaterCut() {
		waterCut = new ArrayList<Double>();
		for(int i = 0; i < size; i++) {
			waterCut.add(waterRate.get(i)/(production.getOilFlowrate()+waterRate.get(i))*100);
		}
	}
	
	public void calcVcorNorm() {
		vcorNorm = new ArrayList<Double>();
		for (int i = 0; i < size; i++) {
			double res = 1/(fcr/fc/getVrNormal(i) + 1/getVmNormal(i));
			vcorNorm.add(new Double(res));
		}
	}
	
	public void calcVcorQT() {
		vcorQT = new ArrayList<Double>();
		for (int i = 0; i < size; i++) {
			double res = 1/(fcr/getVrQT(i)+1/getVmQT(i));
			vcorQT.add(new Double(res));
		}
	}
	
	public void caclVcorCNOrm() {
		vcorCorrectedNorm = new ArrayList<Double>();
		for (int i = 0; i < size; i++) {
			double res = vcorNorm.get(i)* getFScale(i) * getFoil(i)* getFH2S(i);
			vcorCorrectedNorm.add(new Double(res));
		}
	}
	
	public void caclVcorCQT() {
		vcorCorrectedQT = new ArrayList<Double>();
		for (int i = 0; i < size; i++) {
			double res = vcorQT.get(i)*getFScale(i)*getFoil(i)* getFH2S(i);
			if (addInfo.getInhibitionStat() == Additional.INHIB_NONE) {
				res *= 1;
			} else if(addInfo.getInhibitionStat() == Additional.INHIB_CONT) {
				res *= getFinhibitCont(i);
			} else {
				res *= this.getFinhibitSeq(i);
			}
			vcorCorrectedQT.add(new Double(res));
		}
	}
	
	public void calcAll() {
		calcDepth();
		calcTubingLength();
		calcTemp();
		calcPressure();
		calcpHactual();
		calcFCO2();
		calcWaterRate();
		calcWaterCut();
		calcVcorNorm();
		calcVcorQT();
		caclVcorCNOrm();
		caclVcorCQT();
	}
	//intermediate calculations
	
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
		return 3.71 + 0.00417*temp - 0.5*Math.log10(fco2.get(idx));
	}
	
	public double getVrNormal(int idx) {
		double temp = temperature.get(idx).getC();
		return Math.pow(10,(4.84-1119/(temp+273)+0.58*Math.log10(fco2.get(idx))-0.34*(pHactual.get(idx)-getpHCO2(idx))));
	}
	
	public double getVrQT(int idx) {
		double temp = temperature.get(idx).getC();
		return Math.pow(10,(5.07-1119/(temp+273)+0.58*Math.log10(fco2.get(idx))-0.34*(pHactual.get(idx)-getpHCO2(idx))));
	}
	
	public double getVmNormal(int idx) {
		return 2.8* Math.pow(getLiqVel(idx),0.8)/Math.pow(innerd,0.2)*fco2.get(idx);
	}
	
	public double getVmQT(int idx) {
		return 2.7* Math.pow(getLiqVel(idx),0.8)/Math.pow(innerd,0.2)*fco2.get(idx);
	}
	
	//Water condensation calculation //
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
	
	public double getGasZFactor(int idx) {
		double tempF = temperature.get(idx).getF();
		double press = pressure.get(idx).getPsia();
		return Calc.HallYarbGasZG(tempF, press, Vars.GasGravity, conditions.getN2(), conditions.getGasCO2(), conditions.getGasH2S());
	}
	
	public double getACorrelation(int idx) {
		return getWaterVP(idx)*18*1000000*conditions.getStandardPress() / (10.73*(459.6+ conditions.getStandardTemp())*getGasZFactor(idx));
	}
	
	public double getLogBCor(int idx) {
		return 3083.87/(temperature.get(idx).getF()+460)+6.6944;
	}
	
	public double getGasGravCorrection(int idx) {
		double tempF = temperature.get(idx).getF();
		double res = 1 + (Vars.GasGravity - 0.55)/(15500 * Vars.GasGravity * Math.pow(tempF,(-1.446)) - 18300 * Math.pow(tempF,(-1.288)));
		return res;
	}
	
	public double getBrineCorrection(int idx) {
		double equivNacl = conditions.getEqNaCl();
		return 1-0.00000000392* Math.pow(equivNacl,1.44);
	}
	
	public double getWaterContentGas(int idx) {
		double acor = getACorrelation(idx);
		double lbcor = getLogBCor(idx);
		double psia = pressure.get(idx).getPsia();
		return (acor/psia + Math.pow(10,lbcor))*getGasGravCorrection(idx)*getBrineCorrection(idx);
	}
	
	public double getLbmPerD(int idx) {
		return getWaterContentGas(idx) * gasFlowrate;
	}
	
	public double getKgPerD(int idx) {
		return 0.45359237 * getLbmPerD(idx);
	}
	
	public double getM3PerD(int idx) {
		return getKgPerD(idx)/900;
	}
	//End Water condensation calculation //
	
	public double getLiquidFrate(int idx) {
		return production.getOilFlowrate() + waterRate.get(idx);
	}
	
	public double getBG(int idx) {
		return 0.350958*getGasZFactor(idx)*temperature.get(idx).getK()/pressure.get(idx).getKpa();
	}
	
	public double getVsl(int idx) {
		return getLiquidFrate(idx)/24/60/60/innerPipeArea;
	}
	
	public double getVsg(int idx) {
		return production.getGasFlowrate()*1000000/24/60/60*getBG(idx)/innerPipeArea;
	}
	
	public double getVm(int idx) {
		return getVsl(idx)+ getVsg(idx);
	}
	
	public double getLiqHoldup(int idx) {
		return getVsl(idx)/getVm(idx) * 100;
	}
	
	public double getLiqHoldupCh(int idx) {
		return getLiqHoldup(idx) + production.getHoldupLiquid();
	}
	
	public double getLiqVel(int idx) {
		return getVsl(idx)/(getLiqHoldupCh(idx)/100);
	}
	
	public double getGasVel(int idx) {
		return getVsg(idx)/(1-getLiqHoldupCh(idx)/100);
	}
	
	public int getFlowPattern(int idx) {
		if(getVsg(idx) > 4) {
			return Vars.ANNULAR_MIST_FLOW;
		} else {
			return Vars.BUBBLE_SLUG_FLOW;
		}
	}
	
	public double getDensGas(int idx) {
		return 0.7*(273+15)/temperature.get(idx).getK()*pressure.get(idx).getBara()/1*1.226;
	}
	
	public double getDensMix(int idx) {
		return Vars.DesLiq*getLiqHoldup(idx)/100 + getDensGas(idx)*(1-getLiqHoldup(idx)/100);
	}
	
	public double getVerosion(int idx) {
		return 122/Math.sqrt(getDensMix(idx));
	}
	
	public double getFoil(int idx) {
		if(waterCut.get(idx) < 75) {
			double res = 0.059*(waterCut.get(idx)/100)/(wbreak/100)*getLiqVel(idx)+
					1.1* Math.pow(10,(-4))/Math.pow((wbreak/100),2)*angle.getAngleAt(idx)/90+
					0.059*(waterCut.get(idx)/100)/(wbreak/100)*getLiqVel(idx)*angle.getAngleAt(idx)/90; 
			return res;
		} else {
			return 1;
		} 
	}
	
	public double getFinhibitCont(int idx) {
		double verosion = getVerosion(idx);
		if(getGasVel(idx)> verosion) {
			return 1;
		} else {
			return (1-this.contInhibitEff/100);
		}
		
	}
	
	public double getFinhibitSeq(int idx) {
		return 0;
	}
	
	public double getFH2S(int idx) {
		return 1/(1+1800*(getPH2S(idx)/getPCO2(idx)));
	}
	
	public double getFScale(int idx) {
		double res;
		if(getGasVel(idx)> getVerosion(idx)) {
			if(getFlowPattern(idx) == Vars.ANNULAR_MIST_FLOW) {
				res = 1;
			} else {
				res = Math.pow(10, (2400/(temperature.get(idx).getC()+273)- 0.6 * Math.log10(fco2.get(idx))- 6.7));
			}
		} else {
			if(Math.pow(10,(2400/(temperature.get(idx).getC()+273)- 0.6* Math.log10(fco2.get(idx))-6.7))>1) {
				res = 1;
			} else {
				res = Math.pow(10,(2400/(temperature.get(idx).getC()+273)-0.6 * Math.log10(fco2.get(idx))-6.7));
			}
		}
		return res;
	}
	
}
