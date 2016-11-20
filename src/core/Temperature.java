package core;

import calc.Converter;

public class Temperature {
	private double cval;
	
	public Temperature(double val, char unit) {
		if(unit == 'k' || unit == 'K') {
			cval = Converter.tempKtoC(val);
		} else if(unit == 'f' || unit == 'F') {
			cval = Converter.tempFtoC(val);
		} else {
			cval = val;
		}
	}
	
	public double getC(){
		return cval;
	}
	
	public double getF() {
		return Converter.tempCtoF(cval);
	}
	
	public double getK() {
		return Converter.tempCtoK(cval);
	}
}
