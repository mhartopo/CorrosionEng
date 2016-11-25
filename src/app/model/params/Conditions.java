package app.model.params;

public class Conditions {
	private double tempWellhead;
	private double tempBottomhole;
	private double pressWellhead;
	private double pressBottomhole;
	private double gasCO2;
	private double gasH2S;
	private double N2;
	private double eqNaCl;
	private double disolveBicarbon;
	private double standardTemp;
	private double standardPress;
	
	public Conditions() {
		setTempWellhead(40);
		setTempBottomhole(82.22);
		setPressWellhead(100);
		setPressBottomhole(234.42);
		setGasCO2(99.5);
		setGasH2S(0.00005);
		setN2(0);
		setEqNaCl(0);
		setDisolveBicarbon(5000);
		setStandardTemp(60);
		setStandardPress(15.025);
	}

	public double getTempWellhead() {
		return tempWellhead;
	}

	public void setTempWellhead(double tempWellhead) {
		this.tempWellhead = tempWellhead;
	}

	public double getTempBottomhole() {
		return tempBottomhole;
	}

	public void setTempBottomhole(double tempBottomhole) {
		this.tempBottomhole = tempBottomhole;
	}

	public double getPressWellhead() {
		return pressWellhead;
	}

	public void setPressWellhead(double pressWellhead) {
		this.pressWellhead = pressWellhead;
	}

	public double getPressBottomhole() {
		return pressBottomhole;
	}

	public void setPressBottomhole(double pressBottomhole) {
		this.pressBottomhole = pressBottomhole;
	}

	public double getGasCO2() {
		return gasCO2;
	}

	public void setGasCO2(double gasCO2) {
		this.gasCO2 = gasCO2;
	}

	public double getGasH2S() {
		return gasH2S;
	}

	public void setGasH2S(double gasH2S) {
		this.gasH2S = gasH2S;
	}

	public double getN2() {
		return N2;
	}

	public void setN2(double n2) {
		N2 = n2;
	}

	public double getEqNaCl() {
		return eqNaCl;
	}

	public void setEqNaCl(double eqNaCl) {
		this.eqNaCl = eqNaCl;
	}

	public double getDisolveBicarbon() {
		return disolveBicarbon;
	}

	public void setDisolveBicarbon(double disolveBicarbon) {
		this.disolveBicarbon = disolveBicarbon;
	}

	public double getStandardTemp() {
		return standardTemp;
	}

	public void setStandardTemp(double standardTemp) {
		this.standardTemp = standardTemp;
	}

	public double getStandardPress() {
		return standardPress;
	}

	public void setStandardPress(double standardPress) {
		this.standardPress = standardPress;
	}
}
