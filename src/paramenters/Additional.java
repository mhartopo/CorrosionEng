package paramenters;

public class Additional {
	private int inhibitionStat;
	private double inhibitorAvail;
	private int inhibitionPeriod;
	private int FeStatus;
	private double aceticAcid;
	private double erosionalVel;
	
	public Additional() {
		setInhibitionStat(0);
		setInhibitorAvail(0);
		setInhibitionPeriod(0);
		setFeStatus(0);
		setAceticAcid(0);
		setErosionalVel(122);
	}

	public int getInhibitionStat() {
		return inhibitionStat;
	}

	public void setInhibitionStat(int inhibitionStat) {
		this.inhibitionStat = inhibitionStat;
	}

	public double getInhibitorAvail() {
		return inhibitorAvail;
	}

	public void setInhibitorAvail(double inhibitorAvail) {
		this.inhibitorAvail = inhibitorAvail;
	}

	public int getInhibitionPeriod() {
		return inhibitionPeriod;
	}

	public void setInhibitionPeriod(int inhibitionPeriod) {
		this.inhibitionPeriod = inhibitionPeriod;
	}

	public int getFeStatus() {
		return FeStatus;
	}

	public void setFeStatus(int feStatus) {
		FeStatus = feStatus;
	}

	public double getAceticAcid() {
		return aceticAcid;
	}

	public void setAceticAcid(double aceticAcid) {
		this.aceticAcid = aceticAcid;
	}

	public double getErosionalVel() {
		return erosionalVel;
	}

	public void setErosionalVel(double erosionalVel) {
		this.erosionalVel = erosionalVel;
	}
}
