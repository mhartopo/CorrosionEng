package app.model.params;

public class StellInfo {
	private double tubingDepth;
	private double tubingOutDiameter;
	private double tubingThick;
	private boolean taperedStats;
	private double lwTubingDepth;
	private double lwTubingOutDiameter;
	private double lwTubingThick;
	private int stellStats;
	private double chromium;
	private double carbon;
	private double weight;
	
	public StellInfo() {
		setTubingDepth(1518);
		setTubingOutDiameter(0.178);
		setTubingThick(9.19);
		setTaperedStats(false);
		setLwTubingDepth(0);
		setLwTubingOutDiameter(0);
		setLwTubingThick(0);
		setStellStats(0);
		setChromium(0.5);
		setCarbon(0.3);
		setWeight(12.6);
	}

	public double getTubingDepth() {
		return tubingDepth;
	}

	public void setTubingDepth(double tubingDepth) {
		this.tubingDepth = tubingDepth;
	}

	public double getTubingOutDiameter() {
		return tubingOutDiameter;
	}

	public void setTubingOutDiameter(double tubingOutDiameter) {
		this.tubingOutDiameter = tubingOutDiameter;
	}

	public double getTubingThick() {
		return tubingThick;
	}

	public void setTubingThick(double tubingThick) {
		this.tubingThick = tubingThick;
	}

	public boolean isTaperedStats() {
		return taperedStats;
	}

	public void setTaperedStats(boolean taperedStats) {
		this.taperedStats = taperedStats;
	}

	public double getLwTubingDepth() {
		return lwTubingDepth;
	}

	public void setLwTubingDepth(double lwTubingDepth) {
		this.lwTubingDepth = lwTubingDepth;
	}

	public double getLwTubingOutDiameter() {
		return lwTubingOutDiameter;
	}

	public void setLwTubingOutDiameter(double lwTubingOutDiameter) {
		this.lwTubingOutDiameter = lwTubingOutDiameter;
	}

	public double getLwTubingThick() {
		return lwTubingThick;
	}

	public void setLwTubingThick(double lwTubingThick) {
		this.lwTubingThick = lwTubingThick;
	}

	public int getStellStats() {
		return stellStats;
	}

	public void setStellStats(int stellStats) {
		this.stellStats = stellStats;
	}

	public double getChromium() {
		return chromium;
	}

	public void setChromium(double chromium) {
		this.chromium = chromium;
	}

	public double getCarbon() {
		return carbon;
	}

	public void setCarbon(double carbon) {
		this.carbon = carbon;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
}
