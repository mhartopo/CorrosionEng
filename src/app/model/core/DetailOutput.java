package app.model.core;

public class DetailOutput {
	private double GOR; //GOR at wellhead
	private double watercut; //Watercut at wellhead
	private double gasVelocity; //Gas velocity at wellhead
	private double liqVelocity; //Liq. Velocity at wellhead
	private double erosionalVel; //Erosional velocity at wellhead
	private double waterRate; //Water rate in bottomhole
	private double flowPattern; //Flow Pattern at wellhead
	private double CO2Press; //CO2 partial pres. At wellhead
	private double densStell; //Density of Steel
	
	public DetailOutput() {
		GOR = 0;
		watercut = 0;
		gasVelocity = 0;
		liqVelocity = 0;
		erosionalVel = 0;
		waterRate = 0;
		flowPattern = 0;
		CO2Press = 0;
		densStell = 0;
	}
	
	public double getGOR() {
		return GOR;
	}
	public void setGOR(double gOR) {
		GOR = gOR;
	}
	public double getWatercut() {
		return watercut;
	}
	public void setWatercut(double watercut) {
		this.watercut = watercut;
	}
	public double getGasVelocity() {
		return gasVelocity;
	}
	public void setGasVelocity(double gasVelocity) {
		this.gasVelocity = gasVelocity;
	}
	public double getLiqVelocity() {
		return liqVelocity;
	}
	public void setLiqVelocity(double liqVelocity) {
		this.liqVelocity = liqVelocity;
	}
	public double getErosionalVel() {
		return erosionalVel;
	}
	public void setErosionalVel(double erosionalVel) {
		this.erosionalVel = erosionalVel;
	}
	public double getWaterRate() {
		return waterRate;
	}
	public void setWaterRate(double waterRate) {
		this.waterRate = waterRate;
	}
	public double getFlowPattern() {
		return flowPattern;
	}
	public void setFlowPattern(double flowPattern) {
		this.flowPattern = flowPattern;
	}
	public double getCO2Press() {
		return CO2Press;
	}
	public void setCO2Press(double cO2Press) {
		CO2Press = cO2Press;
	}
	public double getDensStell() {
		return densStell;
	}
	public void setDensStell(double densStell) {
		this.densStell = densStell;
	}


}
