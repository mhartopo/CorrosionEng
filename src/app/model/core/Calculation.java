package app.model.core;

import java.util.ArrayList;

import app.model.calc.*;
import app.model.info.*;
import app.model.params.*;

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
	
	private DetailOutput detailOut;
	
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
	private ArrayList<Double> massLoss;
	private ArrayList<Double> liqHold;
	private ArrayList<Double> liqVel;
	private ArrayList<Double> gasVel;
	
	public Calculation() {
		setDetailOut(new DetailOutput());
		setConditions(new Conditions());
		setProduction(new ProductionData());
		setStell(new StellInfo());
		setAddInfo(new Additional());
		setSize(Vars.TotalSegment);
		setAngle(new Angle(getSize()));
		init();
		setDetailOut(new DetailOutput());
		calcTemporary();
	}
	
	public Calculation(Conditions conditions, ProductionData production, StellInfo stell, Additional addInfo, Angle angle) {
		setDetailOut(new DetailOutput());
		this.setConditions(conditions);
		this.setProduction(production);
		this.setStell(stell);
		this.setAddInfo(addInfo);
		setSize(Vars.TotalSegment);
		this.setAngle(angle);
		init();
		calcTemporary();
	}
	
	public void init() {
		setDepth(new ArrayList<Double>());
		setTubingl(new ArrayList<Double>());
		setTemperature(new ArrayList<Temperature>());
		setpHactual(new ArrayList<Double>());
		setPressure(new ArrayList<Pressure>());
		setWaterRate(new ArrayList<Double>());
		setWaterCut(new ArrayList<Double>());
		setVcorNorm(new ArrayList<Double>());
		setVcorQT(new ArrayList<Double>());
		setVcorCorrectedNorm(new ArrayList<Double>());
		setVcorCorrectedQT(new ArrayList<Double>());
		setMassLoss(new ArrayList<Double>());
		fco2 = new ArrayList<Double>();
		liqHold = new ArrayList<Double>();
		liqVel =  new ArrayList<Double>();
		gasVel =  new ArrayList<Double>();
	}
	
	public void clear() {
		depth.clear();
		tubingl.clear();
		temperature.clear();
		pHactual.clear();
		pressure.clear();
		waterRate.clear();
		waterCut.clear();
		vcorNorm.clear();
		vcorQT.clear();
		vcorCorrectedNorm.clear();
		vcorCorrectedQT.clear();
		massLoss.clear();
		fco2.clear();
		liqHold.clear();
		liqVel.clear();
		gasVel.clear();
	}
	
	//cacl temporary out
	
	public void calcTemporary() {
		gasFlowrate = 35.31*getProduction().getGasFlowrate();
		wbreak = (-0.0166*getProduction().getAPIGravity()+0.83)*100;
		innerd = getStell().getTubingOutDiameter()-2*getStell().getTubingThick()/1000;
		innerPipeArea = Math.PI*innerd*innerd/4;
		setLiqFlowRate(getProduction().getWaterFlowrate() + getProduction().getGasFlowrate());
		setZfactor(0.988831629795888);
		contInhibitEff = (getAddInfo().getInhibitorAvail()/100)*(getAddInfo().getInhibitorEff()/100)*100;
		setSeqInhibitEff(0);
		
		if(getStell().getChromium() == 0) {
			fcr = 0;
		} else {
			fcr = 1.6337*getStell().getChromium()+0.9711;
		}
		
		if(getStell().getCarbon() == 0) {
			fc = 0;
		} else {
			fc = 2.4872*getStell().getCarbon()+0.8211;
		}
	}
	
	//hitung depth
	public void calcDepth() {
		for(int i = 0; i < getSize(); i++) {
			if(i == 0) {
				getDepth().add(new Double(0));
			} else {
				getDepth().add(getDepth().get(i-1) + getStell().getTubingDepth() / getSize());
			}
		}
	}
	
	// hitung tubing length
	public void calcTubingLength() {
		for(int i = 0; i < getSize(); i++) {
			getTubingl().add(getDepth().get(i)*3.2808);
		}
	}
	
	// hitung temperature
	public void calcTemp(){
		double scale = (getConditions().getTempBottomhole() - getConditions().getTempWellhead()) / getSize();
		double cval;
		cval = getConditions().getTempWellhead();
		getTemperature().add(new Temperature(cval, 'C'));
		for(int i = 1; i < getSize(); i++) {
			cval = getTemperature().get(i-1).getC() + scale;
			getTemperature().add(new Temperature(cval, 'C'));
		}
	}
	
	//hitung  tekanan
	public void calcPressure() {
		double scale = (getConditions().getPressBottomhole() - getConditions().getPressWellhead()) / getSize();
		double bval = getConditions().getPressWellhead();
		getPressure().add(new Pressure(bval, "bara"));
		for(int i = 1; i < getSize(); i++) {
			bval = getPressure().get(i-1).getBara() + scale;
			getPressure().add(new Pressure(bval, "bara"));
		}
	}
	
	public void calcpHactual() {
		for(int i = 0; i < getSize(); i++) {
			pHactual.add(new Double(getpHactual(i)));
		}
	}
	
	
	public void calcFCO2() {
		for(int i = 0; i < getSize(); i++) {
			fco2.add(new Double(getFCO2(i)));
		}
	}
	
	public void calcWaterRate() {
		Double wrate =  new Double(getProduction().getWaterFlowrate());
		getWaterRate().add(wrate);
		
		for(int i = 1; i < getSize() ; i++) {
			double dM3d = getM3PerD(i) - getM3PerD(i-1);
			if ((getWaterRate().get(i-1) - (dM3d)) < 0) {
				wrate = 0.001;
			} else {
				wrate = getWaterRate().get(i-1)- dM3d;
			}
			getWaterRate().add(wrate);
		}
	}
	
	public void calcWaterCut() {
		for(int i = 0; i < getSize(); i++) {
			getWaterCut().add(getWaterRate().get(i)/(getProduction().getOilFlowrate()+getWaterRate().get(i))*100);
		}
	}
	
	public void calcVcorNorm() {
		for (int i = 0; i < getSize(); i++) {
			double res = 1/(fcr/fc/getVrNormal(i) + 1/getVmNormal(i));
			getVcorNorm().add(new Double(res));
		}
	}
	
	public void calcVcorQT() {
		for (int i = 0; i < getSize(); i++) {
			double res = 1/(fcr/getVrQT(i)+1/getVmQT(i));
			getVcorQT().add(new Double(res));
		}
	}
	
	public void caclVcorCNOrm() {
		for (int i = 0; i < getSize(); i++) {
			double res = getVcorNorm().get(i)* getFScale(i) * getFoil(i)* getFH2S(i);
			getVcorCorrectedNorm().add(new Double(res));
		}
	}
	
	public void caclVcorCQT() {
		for (int i = 0; i < getSize(); i++) {
			double res = getVcorQT().get(i)*getFScale(i)*getFoil(i)* getFH2S(i);
			if (getAddInfo().getInhibitionStat() == Additional.INHIB_NONE) {
				res *= 1;
			} else if(getAddInfo().getInhibitionStat() == Additional.INHIB_CONT) {
				res *= getFinhibitCont(i);
			} else {
				res *= this.getFinhibitSeq(i);
			}
			getVcorCorrectedQT().add(new Double(res));
		}
	}
	
	public void calcMassLoss() {
		double massloss;
		for(int i = 0; i <= Vars.EXPORSURE_TIME; i++) {
			massloss = getPercentMassLoss(i);
			getMassLoss().add(massloss);
		}
	}
	
	public void calcDetailOut() {
		double bottomWatercut = getWaterCut().get(getWaterCut().size()-1)/(getProduction().getOilFlowrate()+getWaterCut().get(getWaterCut().size()-1))*100;
		getProduction().setWatercut(bottomWatercut);
		
		double densStell =  getStell().getWeight()/((Math.PI*((Math.pow((getStell().getTubingOutDiameter()/2),2))-Math.pow((innerd/2),2)))*(3.28084*3.28084))/62.428;
		getDetailOut().setDensStell(densStell);
	}
	
	public void calcFluid() {
		
		for(int i = 0; i < getSize(); i++) {
			liqHold.add(this.getLiqHoldupCh(i));
			liqVel.add(this.getLiqVel(i));
			gasVel.add(this.getGasVel(i));
		}
	}
	
	public void calcAll() {
		clear();
		calcDepth();
		calcTubingLength();
		calcTemp();
		calcPressure();
		calcFCO2();
		calcpHactual();
		calcWaterRate();
		calcWaterCut();
		calcVcorNorm();
		calcVcorQT();
		caclVcorCNOrm();
		caclVcorCQT();
		calcFluid();
		calcDetailOut();
		calcMassLoss();
		
	}
	//intermediate calculations
	
	public double getPCO2(int idx) {
		double press = getPressure().get(idx).getBara();
		return getConditions().getGasCO2()/100*press;
	}
	
	public double getFCO2(int idx) {
		double press = getPressure().get(idx).getBara();
		double temp = getTemperature().get(idx).getC();
		double pco2 = getPCO2(idx);
		double res = Math.log10(pco2) + (0.0031-1.4/(temp + 273)) * press;
		return Math.pow(10, res);
	}
	
	public double getPH2S(int idx) {
		double press = getPressure().get(idx).getBara();
		return getConditions().getGasH2S()/100*press;
	}
	
	public double getHCO3(int idx) {
		return getConditions().getDisolveBicarbon()/930583.202931128*(-1/getTemperature().get(idx).getK()+1/getTemperature().get(0).getK())+getConditions().getDisolveBicarbon()/45584.5;
	}
	
	public double getpHactual(int idx) {
		double hco3 = getHCO3(idx);
		double tempF = getTemperature().get(idx).getF();
		double press = getPressure().get(idx).getBara();
		return Math.log10(hco3/fco2.get(idx)/14.5037738) + 8.68 + 0.00405*tempF + 0.000000458* Math.pow(tempF,2)-0.0000307*press*14.5;
	}
	
	public double getpHCO2(int idx) {
		double temp = getTemperature().get(idx).getC();
		return 3.71 + 0.00417*temp - 0.5*Math.log10(fco2.get(idx));
	}
	
	public double getVrNormal(int idx) {
		double temp = getTemperature().get(idx).getC();
		return Math.pow(10,(4.84-1119/(temp+273)+0.58*Math.log10(fco2.get(idx))-0.34*(getpHactual().get(idx)-getpHCO2(idx))));
	}
	
	public double getVrQT(int idx) {
		double temp = getTemperature().get(idx).getC();
		return Math.pow(10,(5.07-1119/(temp+273)+0.58*Math.log10(fco2.get(idx))-0.34*(getpHactual().get(idx)-getpHCO2(idx))));
	}
	
	public double getVmNormal(int idx) {
		return 2.8* Math.pow(getLiqVel(idx),0.8)/Math.pow(innerd,0.2)*fco2.get(idx);
	}
	
	public double getVmQT(int idx) {
		double res = 2.7* Math.pow(getLiqVel(idx),0.8)/Math.pow(innerd,0.2)*fco2.get(idx);
		return res;
	}
	
	//Water condensation calculation //
	public double getTFactor(int idx) {
		double tempF = getTemperature().get(idx).getF();
		return 1-((tempF-32)*5/9+273.15)/647.096;
	}
	
	public double getWaterVP(int idx) {
		double tempF = getTemperature().get(idx).getF();
		double tfac = getTFactor(idx);
		double res =  0.1450377*22064*Math.exp(647.096/((tempF-32)*5/9+273.15)*(-7.85951783 * tfac + 1.84408259 *
				Math.pow(tfac,1.5) - 11.7866479 *
				Math.pow(tfac,3) + 22.6807411 * Math.pow(tfac,3.5) - 15.9618719 *
				Math.pow(tfac,4) + 1.80122502 * Math.pow(tfac,7.5)));
		return res;
	}
	
	public double getGasZFactor(int idx) {
		double tempF = getTemperature().get(idx).getF();
		double press = getPressure().get(idx).getPsia();
		return Calc.HallYarbGasZG(tempF, press, Vars.GasGravity, getConditions().getN2(), getConditions().getGasCO2(), getConditions().getGasH2S());
	}
	
	public double getACorrelation(int idx) {
		return getWaterVP(idx)*18*1000000*getConditions().getStandardPress() / (10.73*(459.6+ getConditions().getStandardTemp())*getGasZFactor(idx));
	}
	
	public double getLogBCor(int idx) {
		double res =  -3083.87/(104+460)+6.6944;//3083.87/(getTemperature().get(idx).getF()+460)+6.6944;
		return res;
	}
	
	public double getGasGravCorrection(int idx) {
		double tempF = getTemperature().get(idx).getF();
		double res = 1 + (Vars.GasGravity - 0.55)/(15500 * Vars.GasGravity * Math.pow(tempF,(-1.446)) - 18300 * Math.pow(tempF,(-1.288)));
		return res;
	}
	
	public double getBrineCorrection(int idx) {
		double equivNacl = getConditions().getEqNaCl();
		return 1-0.00000000392* Math.pow(equivNacl,1.44);
	}
	
	public double getWaterContentGas(int idx) {
		double acor = getACorrelation(idx);
		double lbcor = getLogBCor(idx);
		double psia = getPressure().get(idx).getPsia();
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
		return getProduction().getOilFlowrate() + getWaterRate().get(idx);
	}
	
	public double getBG(int idx) {
		return 0.350958*getGasZFactor(idx)*getTemperature().get(idx).getK()/getPressure().get(idx).getKpa();
	}
	
	public double getVsl(int idx) {
		return getLiquidFrate(idx)/24/60/60/innerPipeArea;
	}
	
	public double getVsg(int idx) {
		return getProduction().getGasFlowrate()*1000000/24/60/60*getBG(idx)/innerPipeArea;
	}
	
	public double getVm(int idx) {
		return getVsl(idx)+ getVsg(idx);
	}
	
	public double getLiqHoldup(int idx) {
		return getVsl(idx)/getVm(idx) * 100;
	}
	
	public double getLiqHoldupCh(int idx) {
		return getLiqHoldup(idx) + getProduction().getHoldupLiquid();
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
		return 0.7*(273+15)/getTemperature().get(idx).getK()*getPressure().get(idx).getBara()/1*1.226;
	}
	
	public double getDensMix(int idx) {
		return Vars.DesLiq*getLiqHoldup(idx)/100 + getDensGas(idx)*(1-getLiqHoldup(idx)/100);
	}
	
	public double getVerosion(int idx) {
		return 122/Math.sqrt(getDensMix(idx));
	}
	
	public double getFoil(int idx) {
		if(getWaterCut().get(idx) < 75) {
			double res = 0.059*(getWaterCut().get(idx)/100)/(wbreak/100)*getLiqVel(idx)+
					1.1* Math.pow(10,(-4))/Math.pow((wbreak/100),2)*getAngle().getAngleAt(idx)/90+
					0.059*(getWaterCut().get(idx)/100)/(wbreak/100)*getLiqVel(idx)*getAngle().getAngleAt(idx)/90; 
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
				res = Math.pow(10, (2400/(getTemperature().get(idx).getC()+273)- 0.6 * Math.log10(fco2.get(idx))- 6.7));
			}
		} else {
			if(Math.pow(10,(2400/(getTemperature().get(idx).getC()+273)- 0.6* Math.log10(fco2.get(idx))-6.7))>1) {
				res = 1;
			} else {
				res = Math.pow(10,(2400/(getTemperature().get(idx).getC()+273)-0.6 * Math.log10(fco2.get(idx))-6.7));
			}
		}
		return res;
	}
	
	
	public double areaOfSpeciment(int idx) {
		return Math.PI*((getStell().getTubingOutDiameter()-(getStell().getTubingThick()/1000))*3.28024)*(getTubingl().get(idx)-getTubingl().get(idx-1));
	}
	
	public double areaOfSpecimentCm(int idx) {
		return areaOfSpeciment(idx)*929.03;
	}
	
	public double getDesStell() {
		return getDetailOut().getDensStell();
	}
	
	public double getwl(int idx, int expTime) {
		double res = (getVcorCorrectedQT().get(idx-1)*getDesStell()*areaOfSpecimentCm(idx)*expTime)/87.6;
		return res/453592;
	}
	
	public double getPercentMassLoss(int time) {
		ArrayList<Double> wlArr = new ArrayList<Double>(); 
		for(int i = 1; i < getSize(); i++) {
			wlArr.add(getwl(i, time));
		}
		double sum = Calc.sum(wlArr);
		return sum/62748*100;
	}
	
	
	
	//GETTER AND SETTER//
	public ArrayList<Double> getDepth() {
		return depth;
	}

	public void setDepth(ArrayList<Double> depth) {
		this.depth = depth;
	}

	public ArrayList<Double> getTubingl() {
		return tubingl;
	}

	public void setTubingl(ArrayList<Double> tubingl) {
		this.tubingl = tubingl;
	}

	public ArrayList<Temperature> getTemperature() {
		return temperature;
	}

	public void setTemperature(ArrayList<Temperature> temperature) {
		this.temperature = temperature;
	}

	public ArrayList<Double> getpHactual() {
		return pHactual;
	}

	public void setpHactual(ArrayList<Double> pHactual) {
		this.pHactual = pHactual;
	}

	public ArrayList<Pressure> getPressure() {
		return pressure;
	}

	public void setPressure(ArrayList<Pressure> pressure) {
		this.pressure = pressure;
	}

	public ArrayList<Double> getMassLoss() {
		return massLoss;
	}

	public void setMassLoss(ArrayList<Double> massLoss) {
		this.massLoss = massLoss;
	}

	public ArrayList<Double> getVcorCorrectedQT() {
		return vcorCorrectedQT;
	}

	public void setVcorCorrectedQT(ArrayList<Double> vcorCorrectedQT) {
		this.vcorCorrectedQT = vcorCorrectedQT;
	}

	public ArrayList<Double> getVcorCorrectedNorm() {
		return vcorCorrectedNorm;
	}

	public void setVcorCorrectedNorm(ArrayList<Double> vcorCorrectedNorm) {
		this.vcorCorrectedNorm = vcorCorrectedNorm;
	}

	public ArrayList<Double> getVcorQT() {
		return vcorQT;
	}

	public void setVcorQT(ArrayList<Double> vcorQT) {
		this.vcorQT = vcorQT;
	}

	public ArrayList<Double> getLiqHold() {
		return liqHold;
	}

	public void setLiqHold(ArrayList<Double> liqHold) {
		this.liqHold = liqHold;
	}

	public ArrayList<Double> getLiqVel() {
		return liqVel;
	}

	public void setLiqVel(ArrayList<Double> liqVel) {
		this.liqVel = liqVel;
	}

	public ArrayList<Double> getGasVel() {
		return gasVel;
	}

	public void setGasVel(ArrayList<Double> gasVel) {
		this.gasVel = gasVel;
	}

	public ArrayList<Double> getWaterRate() {
		return waterRate;
	}

	public void setWaterRate(ArrayList<Double> waterRate) {
		this.waterRate = waterRate;
	}

	public ArrayList<Double> getWaterCut() {
		return waterCut;
	}

	public void setWaterCut(ArrayList<Double> waterCut) {
		this.waterCut = waterCut;
	}

	public ArrayList<Double> getVcorNorm() {
		return vcorNorm;
	}

	public void setVcorNorm(ArrayList<Double> vcorNorm) {
		this.vcorNorm = vcorNorm;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Additional getAddInfo() {
		return addInfo;
	}

	public void setAddInfo(Additional addInfo) {
		this.addInfo = addInfo;
	}

	public Conditions getConditions() {
		return conditions;
	}

	public void setConditions(Conditions conditions) {
		this.conditions = conditions;
	}

	public ProductionData getProduction() {
		return production;
	}

	public void setProduction(ProductionData production) {
		this.production = production;
	}

	private StellInfo getStell() {
		return stell;
	}

	private void setStell(StellInfo stell) {
		this.stell = stell;
	}

	public Angle getAngle() {
		return angle;
	}

	public void setAngle(Angle angle) {
		this.angle = angle;
	}

	public DetailOutput getDetailOut() {
		return detailOut;
	}

	public void setDetailOut(DetailOutput detailOut) {
		this.detailOut = detailOut;
	}

	public double getLiqFlowRate() {
		return liqFlowRate;
	}

	public void setLiqFlowRate(double liqFlowRate) {
		this.liqFlowRate = liqFlowRate;
	}

	public double getZfactor() {
		return Zfactor;
	}

	public void setZfactor(double zfactor) {
		Zfactor = zfactor;
	}

	public double getSeqInhibitEff() {
		return seqInhibitEff;
	}

	public void setSeqInhibitEff(double seqInhibitEff) {
		this.seqInhibitEff = seqInhibitEff;
	}
}
