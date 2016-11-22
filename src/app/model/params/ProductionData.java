package app.model.params;

public class ProductionData {
	private double oilFlowrate;
	private double APIGravity;
	private double gasFlowrate;
	private double waterFlowrate;
	private double holdupLiquid;
	private double watercut;
	
	public ProductionData() {
		setOilFlowrate(0);
		setAPIGravity(40);
		setGasFlowrate(2.4);
		setWaterFlowrate(20);
		setHoldupLiquid(2);
		setWatercut(100);
	}

	public double getOilFlowrate() {
		return oilFlowrate;
	}

	public void setOilFlowrate(double oilFlowrate) {
		this.oilFlowrate = oilFlowrate;
	}

	public double getAPIGravity() {
		return APIGravity;
	}

	public void setAPIGravity(double aPIGravity) {
		APIGravity = aPIGravity;
	}

	public double getGasFlowrate() {
		return gasFlowrate;
	}

	public void setGasFlowrate(double gasFlowrate) {
		this.gasFlowrate = gasFlowrate;
	}

	public double getWaterFlowrate() {
		return waterFlowrate;
	}

	public void setWaterFlowrate(double waterFlowrate) {
		this.waterFlowrate = waterFlowrate;
	}

	public double getHoldupLiquid() {
		return holdupLiquid;
	}

	public void setHoldupLiquid(double holdupLiquid) {
		this.holdupLiquid = holdupLiquid;
	}

	public double getWatercut() {
		return watercut;
	}

	public void setWatercut(double watercut) {
		this.watercut = watercut;
	}
}
